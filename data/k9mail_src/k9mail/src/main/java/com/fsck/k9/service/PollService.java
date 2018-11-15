package com.fsck.k9.service;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import com.fsck.k9.*;
import com.fsck.k9.controller.MessagingController;
import timber.log.Timber;
public class PollService extends CoreService {
    private static String START_SERVICE = "com.fsck.k9.service.PollService.startService";
    private static String STOP_SERVICE = "com.fsck.k9.service.PollService.stopService";

    private Listener mListener = new Listener();

    public static void startService(Context context) {
        Intent i = new Intent();
        i.setClass(context, PollService.class);
        i.setAction(PollService.START_SERVICE);
        addWakeLock(context, i);
        context.startService(i);
    }

    public static void stopService(Context context) {
        Intent i = new Intent();
        i.setClass(context, PollService.class);
        i.setAction(PollService.STOP_SERVICE);
        addWakeLock(context, i);
        context.startService(i);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setAutoShutdown(false);
    }

    @Override
    public int startService(Intent intent, int startId) {
        if (START_SERVICE.equals(intent.getAction())) {
            Timber.i("PollService started with startId = %d", startId);

            MessagingController controller = MessagingController.getInstance(getApplication());
            Listener listener = (Listener)controller.getCheckMailListener();
            if (listener == null) {
                Timber.i("***** PollService *****: starting new check");
                mListener.setStartId(startId);
                mListener.wakeLockAcquire();
                controller.setCheckMailListener(mListener);
                controller.checkMail(this, null, false, false, mListener);
            } else {
                Timber.i("***** PollService *****: renewing WakeLock");
                listener.setStartId(startId);
                listener.wakeLockAcquire();
            }
        } else if (STOP_SERVICE.equals(intent.getAction())) {
            Timber.i("PollService stopping");
            stopSelf();
        }

        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    

}