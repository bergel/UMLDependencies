package com.fsck.k9.mail.store.imap;


import android.content.Context;
import android.os.PowerManager;
import com.fsck.k9.mail.K9MailLib;
import com.fsck.k9.mail.MessagingException;
import com.fsck.k9.mail.PushReceiver;
import com.fsck.k9.mail.power.TracingPowerManager;
import com.fsck.k9.mail.power.TracingPowerManager.TracingWakeLock;
import timber.log.Timber;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.fsck.k9.mail.K9MailLib.PUSH_WAKE_LOCK_TIMEOUT;
import static com.fsck.k9.mail.store.imap.ImapResponseParser.equalsIgnoreCase;
class ImapFolderPusher extends ImapFolder {
    private static final int IDLE_READ_TIMEOUT_INCREMENT = 5 * 60 * 1000;
    private static final int IDLE_FAILURE_COUNT_LIMIT = 10;
    private static final int MAX_DELAY_TIME = 5 * 60 * 1000; // 5 minutes
    private static final int NORMAL_DELAY_TIME = 5000;


    private final PushReceiver pushReceiver;
    private final Object threadLock = new Object();
    private final IdleStopper idleStopper = new IdleStopper();
    private final TracingWakeLock wakeLock;
    private final List<ImapResponse> storedUntaggedResponses = new ArrayList<ImapResponse>();
    private Thread listeningThread;
    private volatile boolean stop = false;
    private volatile boolean idling = false;


    public ImapFolderPusher(ImapStore store, String name, PushReceiver pushReceiver) {
        super(store, name);
        this.pushReceiver = pushReceiver;

        Context context = pushReceiver.getContext();
        TracingPowerManager powerManager = TracingPowerManager.getPowerManager(context);
        String tag = "ImapFolderPusher " + store.getStoreConfig().toString() + ":" + getName();
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, tag);
        wakeLock.setReferenceCounted(false);
    }

    public void start() {
        synchronized (threadLock) {
            if (listeningThread != null) {
                throw new IllegalStateException("start() called twice");
            }

            listeningThread = new Thread(new PushRunnable());
            listeningThread.start();
        }
    }

    public void refresh() throws IOException, MessagingException {
        if (idling) {
            wakeLock.acquire(PUSH_WAKE_LOCK_TIMEOUT);
            idleStopper.stopIdle();
        }
    }

    public void stop() {
        synchronized (threadLock) {
            if (listeningThread == null) {
                throw new IllegalStateException("stop() called twice");
            }

            stop = true;

            listeningThread.interrupt();
            listeningThread = null;
        }

        ImapConnection conn = connection;
        if (conn != null) {
            if (K9MailLib.isDebug()) {
                Timber.v("Closing connection to stop pushing for %s", getLogId());
            }

            conn.close();
        } else {
            Timber.w("Attempt to interrupt null connection to stop pushing on folderPusher for %s", getLogId());
        }
    }

    @Override
    protected void handleUntaggedResponse(ImapResponse response) {
        if (response.getTag() == null && response.size() > 1) {
            Object responseType = response.get(1);
            if (equalsIgnoreCase(responseType, "FETCH") || equalsIgnoreCase(responseType, "EXPUNGE") ||
                    equalsIgnoreCase(responseType, "EXISTS")) {

                if (K9MailLib.isDebug()) {
                    Timber.d("Storing response %s for later processing", response);
                }

                synchronized (storedUntaggedResponses) {
                    storedUntaggedResponses.add(response);
                }
            }

            handlePossibleUidNext(response);
        }
    }

    private void superHandleUntaggedResponse(ImapResponse response) {
        super.handleUntaggedResponse(response);
    }


    

    /**
     * Ensure the DONE continuation is only sent when the IDLE command was sent and hasn't completed yet.
     */
    
}