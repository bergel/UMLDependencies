package com.fsck.k9.provider;


import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import com.fsck.k9.Account;
import com.fsck.k9.Preferences;
protected class AccountsQueryHandler implements QueryHandler {


        @Override
        public String getPath() {
            return "accounts";
        }

        @Override
        public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
                throws Exception {
            return getAllAccounts(projection);
        }

        public Cursor getAllAccounts(String[] projection) {
            if (projection == null) {
                projection = DEFAULT_ACCOUNT_PROJECTION;
            }

            MatrixCursor cursor = new MatrixCursor(projection);

            for (Account account : Preferences.getPreferences(getContext()).getAccounts()) {
                Object[] values = new Object[projection.length];

                int fieldIndex = 0;
                for (String field : projection) {
                    if (AccountColumns.ACCOUNT_NUMBER.equals(field)) {
                        values[fieldIndex] = account.getAccountNumber();
                    } else if (AccountColumns.ACCOUNT_NAME.equals(field)) {
                        values[fieldIndex] = account.getDescription();
                    } else if (AccountColumns.ACCOUNT_UUID.equals(field)) {
                        values[fieldIndex] = account.getUuid();
                    } else if (AccountColumns.ACCOUNT_COLOR.equals(field)) {
                        values[fieldIndex] = account.getChipColor();
                    } else {
                        values[fieldIndex] = null;
                    }
                    ++fieldIndex;
                }

                cursor.addRow(values);
            }

            return cursor;
        }
    }