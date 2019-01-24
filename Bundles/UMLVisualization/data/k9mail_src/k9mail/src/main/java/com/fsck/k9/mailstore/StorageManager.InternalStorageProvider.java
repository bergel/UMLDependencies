package com.fsck.k9.mailstore;

import android.content.Context;
import com.fsck.k9.R;

import java.io.File;

/**
 * Manager for different {@link StorageProvider} -classes that abstract access
 * to sd-cards, additional internal memory and other storage-locations.
 */
public static class InternalStorageProvider implements StorageProvider {

        public static final String ID = "InternalStorage";

        private File mRoot;

        @Override
        public String getId() {
            return ID;
        }

        @Override
        public void init(Context context) {
            // XXX
            mRoot = new File("/");
        }

        @Override
        public String getName(Context context) {
            return context.getString(R.string.local_storage_provider_internal_label);
        }

        @Override
        public boolean isSupported(Context context) {
            return true;
        }

        @Override
        public File getDatabase(Context context, String id) {
            return context.getDatabasePath(id + ".db");
        }

        @Override
        public File getAttachmentDirectory(Context context, String id) {
            // we store attachments in the database directory
            return context.getDatabasePath(id + ".db_att");
        }

        @Override
        public boolean isReady(Context context) {
            return true;
        }

        @Override
        public File getRoot(Context context) {
            return mRoot;
        }
    }