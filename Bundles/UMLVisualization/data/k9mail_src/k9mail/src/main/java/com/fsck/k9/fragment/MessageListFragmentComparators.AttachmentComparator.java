package com.fsck.k9.fragment;

import android.database.Cursor;

import java.util.Comparator;

/**
 * A set of {@link Comparator} classes used for {@link Cursor} data comparison.
 */
public static class AttachmentComparator implements Comparator<Cursor> {

        @Override
        public int compare(Cursor cursor1, Cursor cursor2) {
            int o1HasAttachment = (cursor1.getInt(MLFProjectionInfo.ATTACHMENT_COUNT_COLUMN) > 0) ? 0 : 1;
            int o2HasAttachment = (cursor2.getInt(MLFProjectionInfo.ATTACHMENT_COUNT_COLUMN) > 0) ? 0 : 1;
            return o1HasAttachment - o2HasAttachment;
        }
    }