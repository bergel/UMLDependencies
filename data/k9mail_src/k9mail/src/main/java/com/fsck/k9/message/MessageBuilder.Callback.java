package com.fsck.k9.message;


import android.app.PendingIntent;
import com.fsck.k9.mail.MessagingException;
import com.fsck.k9.mail.internet.MimeMessage;
public interface Callback {
        void onMessageBuildSuccess(MimeMessage message, boolean isDraft);
        void onMessageBuildCancel();
        void onMessageBuildException(MessagingException exception);
        void onMessageBuildReturnPendingIntent(PendingIntent pendingIntent, int requestCode);
    }