package com.fsck.k9.message;


import android.app.PendingIntent;
import android.support.annotation.VisibleForTesting;
public static class RecipientAutocryptStatus {
        public final RecipientAutocryptStatusType type;
        public final PendingIntent intent;

        @VisibleForTesting
        public RecipientAutocryptStatus(RecipientAutocryptStatusType type, PendingIntent intent) {
            this.type = type;
            this.intent = intent;
        }

        public boolean hasPendingIntent() {
            return intent != null;
        }
    }