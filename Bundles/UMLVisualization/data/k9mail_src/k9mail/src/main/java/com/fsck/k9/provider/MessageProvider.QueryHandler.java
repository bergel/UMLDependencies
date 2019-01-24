package com.fsck.k9.provider;


import android.database.Cursor;
import android.net.Uri;
protected interface QueryHandler {
        /**
         * The path this instance is able to respond to.
         */
        String getPath();

        Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
                throws Exception;
    }