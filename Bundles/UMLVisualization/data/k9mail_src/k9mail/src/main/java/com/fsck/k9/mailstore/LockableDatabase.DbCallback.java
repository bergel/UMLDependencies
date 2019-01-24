package com.fsck.k9.mailstore;

import android.database.sqlite.SQLiteDatabase;
import com.fsck.k9.mail.MessagingException;
public interface DbCallback<T> {
        /**
         * @param db
         *            The locked database on which the work should occur. Never
         *            <code>null</code>.
         * @return Any relevant data. Can be <code>null</code>.
         * @throws WrappedException
         * @throws com.fsck.k9.mail.MessagingException
         * @throws com.fsck.k9.mailstore.UnavailableStorageException
         */
        T doDbWork(SQLiteDatabase db) throws WrappedException, MessagingException;
    }