package com.fsck.k9.fragment;

import android.database.Cursor;

import java.util.Comparator;

/**
 * A set of {@link Comparator} classes used for {@link Cursor} data comparison.
 */
public static class ReverseComparator<T> implements Comparator<T> {
        private Comparator<T> mDelegate;

        /**
         * @param delegate
         *         Never {@code null}.
         */
        public ReverseComparator(final Comparator<T> delegate) {
            mDelegate = delegate;
        }

        @Override
        public int compare(final T object1, final T object2) {
            // arg1 & 2 are mixed up, this is done on purpose
            return mDelegate.compare(object2, object1);
        }
    }