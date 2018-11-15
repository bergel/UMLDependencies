package com.fsck.k9.provider;


import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.CrossProcessCursor;
import android.database.CursorWindow;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import timber.log.Timber;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
protected static class MonitoredCursor implements CrossProcessCursor {
        /**
         * The underlying cursor implementation that handles regular requests
         */
        private CrossProcessCursor cursor;

        /**
         * Whether {@link #close()} was invoked
         */
        private AtomicBoolean closed = new AtomicBoolean(false);

        private Semaphore semaphore;


        protected MonitoredCursor(CrossProcessCursor cursor, Semaphore semaphore) {
            this.cursor = cursor;
            this.semaphore = semaphore;
        }

        @Override
        public void close() {
            if (closed.compareAndSet(false, true)) {
                cursor.close();
                Timber.d("Cursor closed, null'ing & releasing semaphore");
                cursor = null;
                semaphore.release();
            }
        }

        @Override
        public boolean isClosed() {
            return closed.get() || cursor.isClosed();
        }

        @Override
        protected void finalize() throws Throwable {
            close();
            super.finalize();
        }

        protected void checkClosed() throws IllegalStateException {
            if (closed.get()) {
                throw new IllegalStateException("Cursor was closed");
            }
        }

        @Override
        public void fillWindow(int pos, CursorWindow winow) {
            checkClosed();
            cursor.fillWindow(pos, winow);
        }

        @Override
        public CursorWindow getWindow() {
            checkClosed();
            return cursor.getWindow();
        }

        @Override
        public boolean onMove(int oldPosition, int newPosition) {
            checkClosed();
            return cursor.onMove(oldPosition, newPosition);
        }

        @Override
        public void copyStringToBuffer(int columnIndex, CharArrayBuffer buffer) {
            checkClosed();
            cursor.copyStringToBuffer(columnIndex, buffer);
        }

        @Override
        public void deactivate() {
            checkClosed();
            cursor.deactivate();
        }

        @Override
        public byte[] getBlob(int columnIndex) {
            checkClosed();
            return cursor.getBlob(columnIndex);
        }

        @Override
        public int getColumnCount() {
            checkClosed();
            return cursor.getColumnCount();
        }

        @Override
        public int getColumnIndex(String columnName) {
            checkClosed();
            return cursor.getColumnIndex(columnName);
        }

        @Override
        public int getColumnIndexOrThrow(String columnName) throws IllegalArgumentException {
            checkClosed();
            return cursor.getColumnIndexOrThrow(columnName);
        }

        @Override
        public String getColumnName(int columnIndex) {
            checkClosed();
            return cursor.getColumnName(columnIndex);
        }

        @Override
        public String[] getColumnNames() {
            checkClosed();
            return cursor.getColumnNames();
        }

        @Override
        public int getCount() {
            checkClosed();
            return cursor.getCount();
        }

        @Override
        public double getDouble(int columnIndex) {
            checkClosed();
            return cursor.getDouble(columnIndex);
        }

        @Override
        public Bundle getExtras() {
            checkClosed();
            return cursor.getExtras();
        }

        @Override
        public float getFloat(int columnIndex) {
            checkClosed();
            return cursor.getFloat(columnIndex);
        }

        @Override
        public int getInt(int columnIndex) {
            checkClosed();
            return cursor.getInt(columnIndex);
        }

        @Override
        public long getLong(int columnIndex) {
            checkClosed();
            return cursor.getLong(columnIndex);
        }

        @Override
        public int getPosition() {
            checkClosed();
            return cursor.getPosition();
        }

        @Override
        public short getShort(int columnIndex) {
            checkClosed();
            return cursor.getShort(columnIndex);
        }

        @Override
        public String getString(int columnIndex) {
            checkClosed();
            return cursor.getString(columnIndex);
        }

        @Override
        public boolean getWantsAllOnMoveCalls() {
            checkClosed();
            return cursor.getWantsAllOnMoveCalls();
        }

        @TargetApi(Build.VERSION_CODES.M)
        @Override
        public void setExtras(Bundle extras) {
            cursor.setExtras(extras);
        }

        @Override
        public boolean isAfterLast() {
            checkClosed();
            return cursor.isAfterLast();
        }

        @Override
        public boolean isBeforeFirst() {
            checkClosed();
            return cursor.isBeforeFirst();
        }

        @Override
        public boolean isFirst() {
            checkClosed();
            return cursor.isFirst();
        }

        @Override
        public boolean isLast() {
            checkClosed();
            return cursor.isLast();
        }

        @Override
        public boolean isNull(int columnIndex) {
            checkClosed();
            return cursor.isNull(columnIndex);
        }

        @Override
        public boolean move(int offset) {
            checkClosed();
            return cursor.move(offset);
        }

        @Override
        public boolean moveToFirst() {
            checkClosed();
            return cursor.moveToFirst();
        }

        @Override
        public boolean moveToLast() {
            checkClosed();
            return cursor.moveToLast();
        }

        @Override
        public boolean moveToNext() {
            checkClosed();
            return cursor.moveToNext();
        }

        @Override
        public boolean moveToPosition(int position) {
            checkClosed();
            return cursor.moveToPosition(position);
        }

        @Override
        public boolean moveToPrevious() {
            checkClosed();
            return cursor.moveToPrevious();
        }

        @Override
        public void registerContentObserver(ContentObserver observer) {
            checkClosed();
            cursor.registerContentObserver(observer);
        }

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {
            checkClosed();
            cursor.registerDataSetObserver(observer);
        }

        @SuppressWarnings("deprecation")
        @Override
        public boolean requery() {
            checkClosed();
            return cursor.requery();
        }

        @Override
        public Bundle respond(Bundle extras) {
            checkClosed();
            return cursor.respond(extras);
        }

        @Override
        public void setNotificationUri(ContentResolver cr, Uri uri) {
            checkClosed();
            cursor.setNotificationUri(cr, uri);
        }

        @Override
        public void unregisterContentObserver(ContentObserver observer) {
            checkClosed();
            cursor.unregisterContentObserver(observer);
        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {
            checkClosed();
            cursor.unregisterDataSetObserver(observer);
        }

        @Override
        public int getType(int columnIndex) {
            checkClosed();
            return cursor.getType(columnIndex);
        }

        @Override
        public Uri getNotificationUri() {
            return null;
        }
    }