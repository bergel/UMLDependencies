package com.fsck.k9.mailstore;

import timber.log.Timber;
private class StorageListener implements StorageManager.StorageListener {
        @Override
        public void onUnmount(final String providerId) {
            if (!providerId.equals(mStorageProviderId)) {
                return;
            }

            Timber.d("LockableDatabase: Closing DB %s due to unmount event on StorageProvider: %s", uUid, providerId);

            try {
                lockWrite();
                try {
                    mDb.close();
                } finally {
                    unlockWrite();
                }
            } catch (UnavailableStorageException e) {
                Timber.w(e, "Unable to writelock on unmount");
            }
        }

        @Override
        public void onMount(final String providerId) {
            if (!providerId.equals(mStorageProviderId)) {
                return;
            }

            Timber.d("LockableDatabase: Opening DB %s due to mount event on StorageProvider: %s", uUid, providerId);

            try {
                openOrCreateDataspace();
            } catch (UnavailableStorageException e) {
                Timber.e(e, "Unable to open DB on mount");
            }
        }
    }