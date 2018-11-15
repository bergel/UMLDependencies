package com.fsck.k9.provider;


import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Map;


/**
 * Content Provider used to display the message list etc.
 *
 * <p>
 * For now this content provider is for internal use only. In the future we may allow third-party
 * apps to access K-9 Mail content using this content provider.
 * </p>
 */
/*
 * TODO:
 * - add support for account list and folder list
 */
static class SpecialColumnsCursor extends CursorWrapper {
        private int[] mColumnMapping;
        private String[] mSpecialColumnValues;
        private String[] mColumnNames;

        public SpecialColumnsCursor(Cursor cursor, String[] allColumnNames, Map<String, String> specialColumns) {
            super(cursor);

            mColumnNames = allColumnNames;
            mColumnMapping = new int[allColumnNames.length];
            mSpecialColumnValues = new String[specialColumns.size()];
            for (int i = 0, columnIndex = 0, specialColumnCount = 0, len = allColumnNames.length; i < len; i++) {
                String columnName = allColumnNames[i];

                if (specialColumns.containsKey(columnName)) {
                    // This is a special column name, so save the value in mSpecialColumnValues
                    mSpecialColumnValues[specialColumnCount] = specialColumns.get(columnName);

                    // Write the index into mSpecialColumnValues negated into mColumnMapping
                    mColumnMapping[i] = -(specialColumnCount + 1);
                    specialColumnCount++;
                } else {
                    mColumnMapping[i] = columnIndex++;
                }
            }
        }

        @Override
        public byte[] getBlob(int columnIndex) {
            int realColumnIndex = mColumnMapping[columnIndex];
            if (realColumnIndex < 0) {
                throw new RuntimeException("Special column can only be retrieved as string.");
            }

            return super.getBlob(realColumnIndex);
        }

        @Override
        public int getColumnCount() {
            return mColumnMapping.length;
        }

        @Override
        public int getColumnIndex(String columnName) {
            for (int i = 0, len = mColumnNames.length; i < len; i++) {
                if (mColumnNames[i].equals(columnName)) {
                    return i;
                }
            }

            return super.getColumnIndex(columnName);
        }

        @Override
        public int getColumnIndexOrThrow(String columnName) throws IllegalArgumentException {
            int index = getColumnIndex(columnName);
            if (index == -1) {
                throw new IllegalArgumentException("Unknown column name");
            }

            return index;
        }

        @Override
        public String getColumnName(int columnIndex) {
            return mColumnNames[columnIndex];
        }

        @Override
        public String[] getColumnNames() {
            return mColumnNames.clone();
        }

        @Override
        public double getDouble(int columnIndex) {
            int realColumnIndex = mColumnMapping[columnIndex];
            if (realColumnIndex < 0) {
                throw new RuntimeException("Special column can only be retrieved as string.");
            }

            return super.getDouble(realColumnIndex);
        }

        @Override
        public float getFloat(int columnIndex) {
            int realColumnIndex = mColumnMapping[columnIndex];
            if (realColumnIndex < 0) {
                throw new RuntimeException("Special column can only be retrieved as string.");
            }

            return super.getFloat(realColumnIndex);
        }

        @Override
        public int getInt(int columnIndex) {
            int realColumnIndex = mColumnMapping[columnIndex];
            if (realColumnIndex < 0) {
                throw new RuntimeException("Special column can only be retrieved as string.");
            }

            return super.getInt(realColumnIndex);
        }

        @Override
        public long getLong(int columnIndex) {
            int realColumnIndex = mColumnMapping[columnIndex];
            if (realColumnIndex < 0) {
                throw new RuntimeException("Special column can only be retrieved as string.");
            }

            return super.getLong(realColumnIndex);
        }

        @Override
        public short getShort(int columnIndex) {
            int realColumnIndex = mColumnMapping[columnIndex];
            if (realColumnIndex < 0) {
                throw new RuntimeException("Special column can only be retrieved as string.");
            }

            return super.getShort(realColumnIndex);
        }

        @Override
        public String getString(int columnIndex) {
            int realColumnIndex = mColumnMapping[columnIndex];
            if (realColumnIndex < 0) {
                return mSpecialColumnValues[-realColumnIndex - 1];
            }

            return super.getString(realColumnIndex);
        }

        @Override
        public int getType(int columnIndex) {
            int realColumnIndex = mColumnMapping[columnIndex];
            if (realColumnIndex < 0) {
                return FIELD_TYPE_STRING;
            }

            return super.getType(realColumnIndex);
        }

        @Override
        public boolean isNull(int columnIndex) {
            int realColumnIndex = mColumnMapping[columnIndex];
            if (realColumnIndex < 0) {
                return (mSpecialColumnValues[-realColumnIndex - 1] == null);
            }

            return super.isNull(realColumnIndex);
        }
    }