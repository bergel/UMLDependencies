package com.fsck.k9.mailstore;

import android.content.Context;
import android.os.Environment;
import timber.log.Timber;

import java.io.File;
import java.io.IOException;

/**
 * Manager for different {@link StorageProvider} -classes that abstract access
 * to sd-cards, additional internal memory and other storage-locations.
 */
public abstract static class FixedStorageProviderBase implements StorageProvider {
        /**
         * The root of the denoted storage. Used for mount points checking.
         */
        private File mRoot;

        /**
         * Chosen base directory
         */
        private File mApplicationDir;

        @Override
        public void init(final Context context) {
            mRoot = computeRoot(context);
            // use <STORAGE_ROOT>/k9
            mApplicationDir = new File(mRoot, "k9");
        }

        /**
         * Vendor specific checks
         *
         * @return Whether this provider supports the underlying vendor specific
         *         storage
         */
        protected abstract boolean supportsVendor();

        @Override
        public boolean isReady(Context context) {
            try {
                final File root = mRoot.getCanonicalFile();
                return isMountPoint(root)
                       && Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
            } catch (IOException e) {
                Timber.w(e, "Specified root isn't ready: %s", mRoot);
                return false;
            }
        }

        @Override
        public final boolean isSupported(Context context) {
            return mRoot.isDirectory() && supportsVendor();
        }

        @Override
        public File getDatabase(Context context, String id) {
            return new File(mApplicationDir, id + ".db");
        }

        @Override
        public File getAttachmentDirectory(Context context, String id) {
            return new File(mApplicationDir, id + ".db_att");
        }

        @Override
        public final File getRoot(Context context) {
            return mRoot;
        }

        /**
         * Retrieve the well-known storage root directory from the actual
         * implementation.
         *
         * @param context
         *            Never <code>null</code>.
         * @return Never <code>null</code>.
         */
        protected abstract File computeRoot(Context context);
    }