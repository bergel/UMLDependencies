package com.fsck.k9.fragment;

import android.database.Cursor;

import java.util.Comparator;

/**
 * A set of {@link Comparator} classes used for {@link Cursor} data comparison.
 */
public static class SubjectComparator implements Comparator<Cursor> {

        @Override
        public int compare(Cursor cursor1, Cursor cursor2) {
            String subject1 = cursor1.getString(MLFProjectionInfo.SUBJECT_COLUMN);
            String subject2 = cursor2.getString(MLFProjectionInfo.SUBJECT_COLUMN);

            if (subject1 == null) {
                return (subject2 == null) ? 0 : -1;
            } else if (subject2 == null) {
                return 1;
            }

            return subject1.compareToIgnoreCase(subject2);
        }
    }