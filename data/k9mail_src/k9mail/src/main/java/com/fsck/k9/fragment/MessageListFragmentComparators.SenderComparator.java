package com.fsck.k9.fragment;

import android.database.Cursor;

import java.util.Comparator;

/**
 * A set of {@link Comparator} classes used for {@link Cursor} data comparison.
 */
public static class SenderComparator implements Comparator<Cursor> {

        @Override
        public int compare(Cursor cursor1, Cursor cursor2) {
            String sender1 = MlfUtils.getSenderAddressFromCursor(cursor1);
            String sender2 = MlfUtils.getSenderAddressFromCursor(cursor2);

            if (sender1 == null && sender2 == null) {
                return 0;
            } else if (sender1 == null) {
                return 1;
            } else if (sender2 == null) {
                return -1;
            } else {
                return sender1.compareToIgnoreCase(sender2);
            }
        }
    }