package com.fsck.k9.fragment;

import android.database.Cursor;

import java.util.Comparator;

/**
 * A set of {@link Comparator} classes used for {@link Cursor} data comparison.
 */
public static class ReverseIdComparator implements Comparator<Cursor> {
        private int mIdColumn = -1;

        @Override
        public int compare(Cursor cursor1, Cursor cursor2) {
            if (mIdColumn == -1) {
                mIdColumn = cursor1.getColumnIndex("_id");
            }
            long o1Id = cursor1.getLong(mIdColumn);
            long o2Id = cursor2.getLong(mIdColumn);
            return (o1Id > o2Id) ? -1 : 1;
        }
    }