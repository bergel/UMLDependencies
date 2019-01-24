package com.fsck.k9.provider;


import android.app.Application;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import com.fsck.k9.Account;
import com.fsck.k9.BuildConfig;
import com.fsck.k9.K9;
import com.fsck.k9.Preferences;
import com.fsck.k9.activity.MessageInfoHolder;
import com.fsck.k9.activity.MessageReference;
import com.fsck.k9.controller.MessagingController;
import com.fsck.k9.controller.SimpleMessagingListener;
import com.fsck.k9.helper.MessageHelper;
import com.fsck.k9.mail.Message;
import com.fsck.k9.mailstore.LocalMessage;
import timber.log.Timber;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
public class MessageProvider extends ContentProvider {
    public static final String AUTHORITY = BuildConfig.APPLICATION_ID + ".messageprovider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    private static final String[] DEFAULT_MESSAGE_PROJECTION = new String[] {
            MessageColumns._ID,
            MessageColumns.SEND_DATE,
            MessageColumns.SENDER,
            MessageColumns.SUBJECT,
            MessageColumns.PREVIEW,
            MessageColumns.ACCOUNT,
            MessageColumns.URI,
            MessageColumns.DELETE_URI,
            MessageColumns.SENDER_ADDRESS
    };
    private static final String[] DEFAULT_ACCOUNT_PROJECTION = new String[] {
            AccountColumns.ACCOUNT_NUMBER,
            AccountColumns.ACCOUNT_NAME,
    };
    private static final String[] UNREAD_PROJECTION = new String[] {
            UnreadColumns.ACCOUNT_NAME,
            UnreadColumns.UNREAD
    };


    private UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private List<QueryHandler> queryHandlers = new ArrayList<QueryHandler>();
    private MessageHelper messageHelper;

    /**
     * How many simultaneous cursors we can afford to expose at once
     */
    Semaphore semaphore = new Semaphore(1);

    ScheduledExecutorService scheduledPool = Executors.newScheduledThreadPool(1);


    @Override
    public boolean onCreate() {
        messageHelper = MessageHelper.getInstance(getContext());

        registerQueryHandler(new ThrottlingQueryHandler(new AccountsQueryHandler()));
        registerQueryHandler(new ThrottlingQueryHandler(new MessagesQueryHandler()));
        registerQueryHandler(new ThrottlingQueryHandler(new UnreadQueryHandler()));

        K9.registerApplicationAware(new K9.ApplicationAware() {
            @Override
            public void initializeComponent(final Application application) {
                Timber.v("Registering content resolver notifier");

                MessagingController.getInstance(application).addListener(new SimpleMessagingListener() {
                    @Override
                    public void folderStatusChanged(Account account, String folderName, int unreadMessageCount) {
                        application.getContentResolver().notifyChange(CONTENT_URI, null);
                    }
                });
            }
        });

        return true;
    }

    @Override
    public String getType(Uri uri) {
        if (K9.app == null) {
            return null;
        }

        Timber.v("MessageProvider/getType: %s", uri);

        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if (K9.app == null) {
            return null;
        }

        Timber.v("MessageProvider/query: %s", uri);

        int code = uriMatcher.match(uri);
        if (code == -1) {
            throw new IllegalStateException("Unrecognized URI: " + uri);
        }

        Cursor cursor;
        try {
            QueryHandler handler = queryHandlers.get(code);
            cursor = handler.query(uri, projection, selection, selectionArgs, sortOrder);
        } catch (Exception e) {
            Timber.e(e, "Unable to execute query for URI: %s", uri);
            return null;
        }

        return cursor;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if (K9.app == null) {
            return 0;
        }

        Timber.v("MessageProvider/delete: %s", uri);

        // Note: can only delete a message

        List<String> segments = uri.getPathSegments();
        int accountId = Integer.parseInt(segments.get(1));
        String folderName = segments.get(2);
        String msgUid = segments.get(3);

        // get account
        Account myAccount = null;
        for (Account account : Preferences.getPreferences(getContext()).getAccounts()) {
            if (account.getAccountNumber() == accountId) {
                myAccount = account;
                if (!account.isAvailable(getContext())) {
                    Timber.w("not deleting messages because account is unavailable at the moment");
                    return 0;
                }
            }
        }

        if (myAccount == null) {
            Timber.e("Could not find account with id %d", accountId);
        }

        if (myAccount != null) {
            MessageReference messageReference = new MessageReference(myAccount.getUuid(), folderName, msgUid, null);
            MessagingController controller = MessagingController.getInstance(getContext());
            controller.deleteMessage(messageReference, null);
        }

        // FIXME return the actual number of deleted messages
        return 0;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (K9.app == null) {
            return null;
        }

        Timber.v("MessageProvider/insert: %s", uri);

        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (K9.app == null) {
            return 0;
        }

        Timber.v("MessageProvider/update: %s", uri);

        // TBD

        return 0;
    }

    /**
     * Register a {@link QueryHandler} to handle a certain {@link Uri} for
     * {@link #query(Uri, String[], String, String[], String)}
     */
    protected void registerQueryHandler(QueryHandler handler) {
        if (queryHandlers.contains(handler)) {
            return;
        }
        queryHandlers.add(handler);

        int code = queryHandlers.indexOf(handler);
        uriMatcher.addURI(AUTHORITY, handler.getPath(), code);
    }


    

    

    

    

    

    /**
     * Extracts a value from an object.
     */
    

    /**
     * Extracts the {@link LocalMessage#getDatabaseId() ID} from the given {@link MessageInfoHolder}. The underlying
     * {@link Message} is expected to be a {@link LocalMessage}.
     */
    

    

    

    

    

    

    

    

    

    

    

    

    

    

    

    /**
     * @deprecated having an incremental value has no real interest, implemented for compatibility only
     */
    

    /**
     * Retrieve messages from the integrated inbox.
     */
    

    /**
     * Retrieve the account list.
     */
    

    /**
     * Retrieve the unread message count for a given account specified by its {@link Account#getAccountNumber() number}.
     */
    

    /**
     * Cursor wrapper that release a semaphore on close. Close is also triggered on {@link #finalize()}.
     */
    

    

    /**
     * Synchronized listener used to retrieve {@link MessageInfoHolder}s using a given {@link BlockingQueue}.
     */
    
}