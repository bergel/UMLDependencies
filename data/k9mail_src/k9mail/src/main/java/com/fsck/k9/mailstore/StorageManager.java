package com.fsck.k9.mailstore;

import android.content.Context;
import android.os.Environment;
import com.fsck.k9.K9;
import timber.log.Timber;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Manager for different {@link StorageProvider} -classes that abstract access
 * to sd-cards, additional internal memory and other storage-locations.
 */
public class StorageManager {

    /**
     * Provides entry points (File objects) to an underlying storage,
     * alleviating the caller from having to know where that storage is located.
     *
     * <p>
     * Allow checking for the denoted storage availability since its lifecycle
     * can evolving (a storage might become unavailable at some time and be back
     * online later).
     * </p>
     */
    

    /**
     * Interface for components wanting to be notified of storage availability
     * events.
     */
    

    /**
     * Base provider class for providers that rely on well-known path to check
     * for storage availability.
     *
     * <p>
     * Since solely checking for paths can be unsafe, this class allows to check
     * for device compatibility using {@link #supportsVendor()}. If the vendor
     * specific check fails, the provider won't be able to provide any valid
     * File handle, regardless of the path existence.
     * </p>
     *
     * <p>
     * Moreover, this class validates the denoted storage path against mount
     * points using {@link StorageManager#isMountPoint(File)}.
     * </p>
     */
    

    /**
     * Strategy to access the always available internal storage.
     *
     * <p>
     * This implementation is expected to work on every device since it's based
     * on the regular Android API {@link Context#getDatabasePath(String)} and
     * uses the result to retrieve the DB path and the attachment directory path.
     * </p>
     *
     * <p>
     * The underlying storage has always been used by K-9.
     * </p>
     */
    

    /**
     * Strategy for accessing the storage as returned by
     * {@link Environment#getExternalStorageDirectory()}. In order to be
     * compliant with Android recommendation regarding application uninstalling
     * and to prevent from cluttering the storage root, the chosen directory
     * will be
     * <code>&lt;STORAGE_ROOT&gt;/Android/data/&lt;APPLICATION_PACKAGE_NAME&gt;/files/</code>
     *
     * <p>
     * The denoted storage is usually a SD card.
     * </p>
     *
     * <p>
     * This provider is expected to work on all devices but the returned
     * underlying storage might not be always available, due to
     * mount/unmount/USB share events.
     * </p>
     */
    

    /**
     * Storage provider to allow access the /emmc directory on a HTC Incredible.
     *
     * <p>
     * This implementation is experimental and _untested_.
     * </p>
     *
     * See http://groups.google.com/group/android-developers/browse_frm/thread/96f15e57150ed173
     *
     * @see FixedStorageProviderBase
     */
    

    /**
     * Storage provider to allow access the Samsung Galaxy S 'internal SD card'.
     *
     * <p>
     * This implementation is experimental and _untested_.
     * </p>
     *
     * See http://groups.google.com/group/android-developers/browse_frm/thread/a1adf7122a75a657
     *
     * @see FixedStorageProviderBase
     */
    

    /**
     * Stores storage provider locking information
     */
    

    /**
     * The active storage providers.
     */
    private final Map<String, StorageProvider> mProviders = new LinkedHashMap<String, StorageProvider>();

    /**
     * Locking data for the active storage providers.
     */
    private final Map<StorageProvider, SynchronizationAid> mProviderLocks = new IdentityHashMap<StorageProvider, SynchronizationAid>();

    protected final Context context;

    /**
     * Listener to be notified for storage related events.
     */
    private List<StorageListener> mListeners = new ArrayList<StorageListener>();

    private static transient StorageManager instance;

    public static synchronized StorageManager getInstance(final Context context) {
        if (instance == null) {
            instance = new StorageManager(context.getApplicationContext());
        }
        return instance;
    }

    /**
     * @param file
     *            Canonical file to match. Never <code>null</code>.
     * @return Whether the specified file matches a filesystem root.
     * @throws IOException
     */
    public static boolean isMountPoint(final File file) {
        for (final File root : File.listRoots()) {
            if (root.equals(file)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param context
     *            Never <code>null</code>.
     * @throws NullPointerException
     *             If <tt>context</tt> is <code>null</code>.
     */
    protected StorageManager(final Context context) throws NullPointerException {
        if (context == null) {
            throw new NullPointerException("No Context given");
        }

        this.context = context;

        /*
         * 20101113/fiouzy:
         *
         * Here is where we define which providers are used, currently we only
         * allow the internal storage and the regular external storage.
         *
         * HTC Incredible storage and Samsung Galaxy S are omitted on purpose
         * (they're experimental and I don't have those devices to test).
         *
         *
         * !!! Make sure InternalStorageProvider is the first provider as it'll
         * be considered as the default provider !!!
         */
        final List<StorageProvider> allProviders = Arrays.asList(new InternalStorageProvider(),
                new ExternalStorageProvider());
        for (final StorageProvider provider : allProviders) {
            // check for provider compatibility
            if (provider.isSupported(context)) {
                // provider is compatible! proceeding

                provider.init(context);
                mProviders.put(provider.getId(), provider);
                mProviderLocks.put(provider, new SynchronizationAid());
            }
        }

    }

    /**
     * @return Never <code>null</code>.
     */
    public String getDefaultProviderId() {
        // assume there is at least 1 provider defined
        return mProviders.keySet().iterator().next();
    }

    /**
     * @param providerId
     *            Never <code>null</code>.
     * @return <code>null</code> if not found.
     */
    protected StorageProvider getProvider(final String providerId) {
        return mProviders.get(providerId);
    }

    /**
     * @param dbName
     *            Never <code>null</code>.
     * @param providerId
     *            Never <code>null</code>.
     * @return The resolved database file for the given provider ID.
     */
    public File getDatabase(final String dbName, final String providerId) {
        StorageProvider provider = getProvider(providerId);
        // TODO fallback to internal storage if no provider
        return provider.getDatabase(context, dbName);
    }

    /**
     * @param dbName
     *            Never <code>null</code>.
     * @param providerId
     *            Never <code>null</code>.
     * @return The resolved attachment directory for the given provider ID.
     */
    public File getAttachmentDirectory(final String dbName, final String providerId) {
        StorageProvider provider = getProvider(providerId);
        // TODO fallback to internal storage if no provider
        return provider.getAttachmentDirectory(context, dbName);
    }

    /**
     * @param providerId
     *            Never <code>null</code>.
     * @return Whether the specified provider is ready for read/write operations
     */
    public boolean isReady(final String providerId) {
        StorageProvider provider = getProvider(providerId);
        if (provider == null) {
            Timber.w("Storage-Provider \"%s\" does not exist", providerId);
            return false;
        }
        return provider.isReady(context);
    }

    /**
     * @return A map of available providers names, indexed by their ID. Never
     *         <code>null</code>.
     * @see StorageManager
     * @see StorageProvider#isSupported(Context)
     */
    public Map<String, String> getAvailableProviders() {
        final Map<String, String> result = new LinkedHashMap<String, String>();
        for (final Map.Entry<String, StorageProvider> entry : mProviders.entrySet()) {
            result.put(entry.getKey(), entry.getValue().getName(context));
        }
        return result;
    }

    /**
     * @param path
     */
    public void onBeforeUnmount(final String path) {
        Timber.i("storage path \"%s\" unmounting", path);
        final StorageProvider provider = resolveProvider(path);
        if (provider == null) {
            return;
        }
        for (final StorageListener listener : mListeners) {
            try {
                listener.onUnmount(provider.getId());
            } catch (Exception e) {
                Timber.w(e, "Error while notifying StorageListener");
            }
        }
        final SynchronizationAid sync = mProviderLocks.get(resolveProvider(path));
        sync.writeLock.lock();
        sync.unmounting = true;
        sync.writeLock.unlock();
    }

    public void onAfterUnmount(final String path) {
        Timber.i("storage path \"%s\" unmounted", path);
        final StorageProvider provider = resolveProvider(path);
        if (provider == null) {
            return;
        }
        final SynchronizationAid sync = mProviderLocks.get(resolveProvider(path));
        sync.writeLock.lock();
        sync.unmounting = false;
        sync.writeLock.unlock();

        K9.setServicesEnabled(context);
    }

    /**
     * @param path
     * @param readOnly
     */
    public void onMount(final String path, final boolean readOnly) {
        Timber.i("storage path \"%s\" mounted readOnly=%s", path, readOnly);
        if (readOnly) {
            return;
        }

        final StorageProvider provider = resolveProvider(path);
        if (provider == null) {
            return;
        }
        for (final StorageListener listener : mListeners) {
            try {
                listener.onMount(provider.getId());
            } catch (Exception e) {
                Timber.w(e, "Error while notifying StorageListener");
            }
        }

        // XXX we should reset mail service ONLY if there are accounts using the storage (this is not done in a regular listener because it has to be invoked afterward)
        K9.setServicesEnabled(context);
    }

    /**
     * @param path
     *            Never <code>null</code>.
     * @return The corresponding provider. <code>null</code> if no match.
     */
    protected StorageProvider resolveProvider(final String path) {
        for (final StorageProvider provider : mProviders.values()) {
            if (path.equals(provider.getRoot(context).getAbsolutePath())) {
                return provider;
            }
        }
        return null;
    }

    public void addListener(final StorageListener listener) {
        mListeners.add(listener);
    }

    public void removeListener(final StorageListener listener) {
        mListeners.remove(listener);
    }

    /**
     * Try to lock the underlying storage to prevent concurrent unmount.
     *
     * <p>
     * You must invoke {@link #unlockProvider(String)} when you're done with the
     * storage.
     * </p>
     *
     * @param providerId
     * @throws UnavailableStorageException
     *             If the storage can't be locked.
     */
    public void lockProvider(final String providerId) throws UnavailableStorageException {
        final StorageProvider provider = getProvider(providerId);
        if (provider == null) {
            throw new UnavailableStorageException("StorageProvider not found: " + providerId);
        }
        // lock provider
        final SynchronizationAid sync = mProviderLocks.get(provider);
        final boolean locked = sync.readLock.tryLock();
        if (!locked || (locked && sync.unmounting)) {
            if (locked) {
                sync.readLock.unlock();
            }
            throw new UnavailableStorageException("StorageProvider is unmounting");
        } else if (locked && !provider.isReady(context)) {
            sync.readLock.unlock();
            throw new UnavailableStorageException("StorageProvider not ready");
        }
    }

    public void unlockProvider(final String providerId) {
        final StorageProvider provider = getProvider(providerId);
        final SynchronizationAid sync = mProviderLocks.get(provider);
        sync.readLock.unlock();
    }
}