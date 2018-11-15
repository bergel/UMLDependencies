package com.fsck.k9.mailstore;

import android.content.Context;
import android.os.Environment;
import com.fsck.k9.R;

import java.io.File;

/**
 * Manager for different {@link StorageProvider} -classes that abstract access
 * to sd-cards, additional internal memory and other storage-locations.
 */
public static class ExternalStorageProvider implements StorageProvider {

        public static final String ID = "ExternalStorage";

        /**
         * Root of the denoted storage.
         */
        private File mRoot;

        /**
         * Chosen base directory.
         */
        private File mApplicationDirectory;

        @Override
        public String getId() {
            return ID;
        }

        @Override
        public void init(Context context) {
            mRoot = Environment.getExternalStorageDirectory();
            mApplicationDirectory = new File(new File(new File(new File(mRoot, "Android"), "data"),
                                             context.getPackageName()), "files");
        }

        @Override
        public String getName(Context context) {
            return context.getString(R.string.local_storage_provider_external_label);
        }

        @Override
        public boolean isSupported(Context context) {
            return true;
        }

        @Override
        public File getDatabase(Context context, String id) {
            return new File(mApplicationDirectory, id + ".db");
        }

        @Override
        public File getAttachmentDirectory(Context context, String id) {
            return new File(mApplicationDirectory, id + ".db_att");
        }

        @Override
        public boolean isReady(Context context) {
            return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
        }

        @Override
        public File getRoot(Context context) {
            return mRoot;
        }
    }