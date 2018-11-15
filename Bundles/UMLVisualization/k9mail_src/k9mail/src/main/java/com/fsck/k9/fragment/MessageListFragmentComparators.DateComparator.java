package com.fsck.k9.fragment;

import android.database.Cursor;

import java.util.Comparator;

/**
 * A set of {@link Comparator} classes used for {@link Cursor} data comparison.
 */
public static class DateComparator implements Comparator<Cursor> {

        @Override
        public int compare(Cursor cursor1, Cursor cursor2) {
            long o1Date = cursor1.getLong(MLFProjectionInfo.DATE_COLUMN);
            long o2Date = cursor2.getLong(MLFProjectionInfo.DATE_COLUMN);
            if (o1Date < o2Date) {
                return -1;
            } else if (o1Date == o2Date) {
                return 0;
            } else {
                return 1;
            }
        }
    }