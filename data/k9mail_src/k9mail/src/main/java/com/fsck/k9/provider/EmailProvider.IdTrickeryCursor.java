package com.fsck.k9.provider;


import android.database.Cursor;
import android.database.CursorWrapper;


/**
 * Content Provider used to display the message list etc.
 *
 * <p>
 * For now this content provider is for internal use only. In the future we may allow third-party
 * apps to access K-9 Mail content using this content provider.
 * </p>
 */
/*
 * TODO:
 * - add support for account list and folder list
 */
static class IdTrickeryCursor extends CursorWrapper {
        public IdTrickeryCursor(Cursor cursor) {
            super(cursor);
        }

        @Override
        public int getColumnIndex(String columnName) {
            if ("_id".equals(columnName)) {
                return super.getColumnIndex("id");
            }

            return super.getColumnIndex(columnName);
        }

        @Override
        public int getColumnIndexOrThrow(String columnName) {
            if ("_id".equals(columnName)) {
                return super.getColumnIndexOrThrow("id");
            }

            return super.getColumnIndexOrThrow(columnName);
        }
    }