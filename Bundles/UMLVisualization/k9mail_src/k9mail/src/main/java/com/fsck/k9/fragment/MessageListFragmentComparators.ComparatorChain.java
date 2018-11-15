package com.fsck.k9.fragment;

import android.database.Cursor;

import java.util.Comparator;
import java.util.List;

/**
 * A set of {@link Comparator} classes used for {@link Cursor} data comparison.
 */
public static class ComparatorChain<T> implements Comparator<T> {
        private List<Comparator<T>> mChain;

        /**
         * @param chain
         *         Comparator chain. Never {@code null}.
         */
        public ComparatorChain(final List<Comparator<T>> chain) {
            mChain = chain;
        }

        @Override
        public int compare(T object1, T object2) {
            int result = 0;
            for (final Comparator<T> comparator : mChain) {
                result = comparator.compare(object1, object2);
                if (result != 0) {
                    break;
                }
            }
            return result;
        }
    }