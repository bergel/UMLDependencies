package com.fsck.k9.mailstore;

import android.database.sqlite.SQLiteDatabase;
public interface SchemaDefinition {
        int getVersion();

        /**
         * @param db Never <code>null</code>.
         */
        void doDbUpgrade(SQLiteDatabase db);
    }