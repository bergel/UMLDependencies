package com.fsck.k9.fragment;

import android.database.Cursor;

import java.util.Comparator;

/**
 * A set of {@link Comparator} classes used for {@link Cursor} data comparison.
 */
public static class FlaggedComparator implements Comparator<Cursor> {

        @Override
        public int compare(Cursor cursor1, Cursor cursor2) {
            int o1IsFlagged = (cursor1.getInt(MLFProjectionInfo.FLAGGED_COLUMN) == 1) ? 0 : 1;
            int o2IsFlagged = (cursor2.getInt(MLFProjectionInfo.FLAGGED_COLUMN) == 1) ? 0 : 1;
            return o1IsFlagged - o2IsFlagged;
        }
    }