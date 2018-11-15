package com.fsck.k9.fragment;

import android.database.Cursor;

import java.util.Comparator;

/**
 * A set of {@link Comparator} classes used for {@link Cursor} data comparison.
 */
public static class UnreadComparator implements Comparator<Cursor> {

        @Override
        public int compare(Cursor cursor1, Cursor cursor2) {
            int o1IsUnread = cursor1.getInt(MLFProjectionInfo.READ_COLUMN);
            int o2IsUnread = cursor2.getInt(MLFProjectionInfo.READ_COLUMN);
            return o1IsUnread - o2IsUnread;
        }
    }