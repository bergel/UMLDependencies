package com.fsck.k9.mailstore;

import android.content.Context;

import java.io.File;

/**
 * Manager for different {@link StorageProvider} -classes that abstract access
 * to sd-cards, additional internal memory and other storage-locations.
 */
public static interface StorageProvider {

        /**
         * Retrieve the uniquely identifier for the current implementation.
         *
         * <p>
         * It is expected that the identifier doesn't change over reboots since
         * it'll be used to save settings and retrieve the provider at a later
         * time.
         * </p>
         *
         * <p>
         * The returned identifier doesn't have to be user friendly.
         * </p>
         *
         * @return Never <code>null</code>.
         */
        String getId();

        /**
         * Hook point for provider initialization.
         *
         * @param context
         *            Never <code>null</code>.
         */
        void init(Context context);

        /**
         * @param context
         *            Never <code>null</code>.
         * @return A user displayable, localized name for this provider. Never
         *         <code>null</code>.
         */
        String getName(Context context);

        /**
         * Some implementations may not be able to return valid File handles
         * because the device doesn't provide the denoted storage. You can check
         * the provider compatibility with this method to prevent from having to
         * invoke this provider ever again.
         *
         * @param context
         *            TODO
         * @return Whether this provider supports the current device.
         * @see StorageManager#getAvailableProviders()
         */
        boolean isSupported(Context context);

        /**
         * Return the {@link File} to the chosen email database file. The
         * resulting {@link File} doesn't necessarily match an existing file on
         * the filesystem.
         *
         * @param context
         *            Never <code>null</code>.
         * @param id
         *            Never <code>null</code>.
         * @return Never <code>null</code>.
         */
        File getDatabase(Context context, String id);

        /**
         * Return the {@link File} to the chosen attachment directory. The
         * resulting {@link File} doesn't necessarily match an existing
         * directory on the filesystem.
         *
         * @param context
         *            Never <code>null</code>.
         * @param id
         *            Never <code>null</code>.
         * @return Never <code>null</code>.
         */
        File getAttachmentDirectory(Context context, String id);

        /**
         * Check for the underlying storage availability.
         *
         * @param context
         *            Never <code>null</code>.
         * @return Whether the underlying storage returned by this provider is
         *         ready for read/write operations at the time of invocation.
         */
        boolean isReady(Context context);

        /**
         * Retrieve the root of the underlying storage.
         *
         * @param context
         *            Never <code>null</code>.
         * @return The root directory of the denoted storage. Never
         *         <code>null</code>.
         */
        File getRoot(Context context);
    }