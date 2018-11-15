package com.fsck.k9.mailstore;

/**
 * Manager for different {@link StorageProvider} -classes that abstract access
 * to sd-cards, additional internal memory and other storage-locations.
 */
public static interface StorageListener {
        /**
         * Invoked on storage mount (with read/write access).
         *
         * @param providerId
         *            Identifier (as returned by {@link StorageProvider#getId()}
         *            of the newly mounted storage. Never <code>null</code>.
         */
        void onMount(String providerId);

        /**
         * Invoked when a storage is about to be unmounted.
         *
         * @param providerId
         *            Identifier (as returned by {@link StorageProvider#getId()}
         *            of the to-be-unmounted storage. Never <code>null</code>.
         */
        void onUnmount(String providerId);
    }