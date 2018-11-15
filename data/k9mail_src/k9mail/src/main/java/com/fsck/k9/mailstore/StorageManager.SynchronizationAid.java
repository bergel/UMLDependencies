package com.fsck.k9.mailstore;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Manager for different {@link StorageProvider} -classes that abstract access
 * to sd-cards, additional internal memory and other storage-locations.
 */
public static class SynchronizationAid {
        /**
         * {@link Lock} has a thread semantic so it can't be released from
         * another thread - this flags act as a holder for the unmount state
         */
        public boolean unmounting = false;

        public final Lock readLock;

        public final Lock writeLock;

        {
            final ReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);
            readLock = readWriteLock.readLock();
            writeLock = readWriteLock.writeLock();
        }
    }