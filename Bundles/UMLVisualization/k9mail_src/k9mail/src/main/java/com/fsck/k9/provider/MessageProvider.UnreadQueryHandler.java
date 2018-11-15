package com.fsck.k9.provider;


import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Binder;
import com.fsck.k9.Account;
import com.fsck.k9.AccountStats;
import com.fsck.k9.Preferences;
import com.fsck.k9.mail.MessagingException;
import timber.log.Timber;

import java.util.List;
protected class UnreadQueryHandler implements QueryHandler {

        @Override
        public String getPath() {
            return "account_unread/#";
        }

        @Override
        public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
                throws Exception {
            List<String> segments = uri.getPathSegments();
            int accountId = Integer.parseInt(segments.get(1));

            /*
             * This method below calls Account.getStats() which uses EmailProvider to do its work.
             * For this to work we need to clear the calling identity. Otherwise accessing
             * EmailProvider will fail because it's not exported so third-party apps can't access it
             * directly.
             */
            long identityToken = Binder.clearCallingIdentity();
            try {
                return getAccountUnread(accountId);
            } finally {
                Binder.restoreCallingIdentity(identityToken);
            }
        }

        private Cursor getAccountUnread(int accountNumber) {

            MatrixCursor cursor = new MatrixCursor(UNREAD_PROJECTION);

            Account myAccount;
            AccountStats myAccountStats;

            Object[] values = new Object[2];

            for (Account account : Preferences.getPreferences(getContext()).getAvailableAccounts()) {
                if (account.getAccountNumber() == accountNumber) {
                    myAccount = account;
                    try {
                        myAccountStats = account.getStats(getContext());
                        values[0] = myAccount.getDescription();
                        if (myAccountStats == null) {
                            values[1] = 0;
                        } else {
                            values[1] = myAccountStats.unreadMessageCount;
                        }
                    } catch (MessagingException e) {
                        Timber.e(e.getMessage());
                        values[0] = "Unknown";
                        values[1] = 0;
                    }
                    cursor.addRow(values);
                }
            }

            return cursor;
        }
    }