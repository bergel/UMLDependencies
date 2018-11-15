package com.fsck.k9.provider;


import android.database.CrossProcessCursor;
import android.database.Cursor;
import android.net.Uri;
import timber.log.Timber;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;
protected class ThrottlingQueryHandler implements QueryHandler {
        private QueryHandler delegate;


        public ThrottlingQueryHandler(QueryHandler delegate) {
            this.delegate = delegate;
        }

        @Override
        public String getPath() {
            return delegate.getPath();
        }

        @Override
        public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
                throws Exception {
            semaphore.acquire();

            Cursor cursor = null;
            try {
                cursor = delegate.query(uri, projection, selection, selectionArgs, sortOrder);
            } finally {
                if (cursor == null) {
                    semaphore.release();
                }
            }

            // Android content resolvers can only process CrossProcessCursor instances
            if (!(cursor instanceof CrossProcessCursor)) {
                Timber.w("Unsupported cursor, returning null: %s", cursor);
                semaphore.release();
                return null;
            }

            MonitoredCursor wrapped = new MonitoredCursor((CrossProcessCursor) cursor, semaphore);

            // Use a weak reference not to actively prevent garbage collection
            final WeakReference<MonitoredCursor> weakReference = new WeakReference<MonitoredCursor>(wrapped);

            // Make sure the cursor is closed after 30 seconds
            scheduledPool.schedule(new Runnable() {

                @Override
                public void run() {
                    MonitoredCursor monitored = weakReference.get();
                    if (monitored != null && !monitored.isClosed()) {
                        Timber.w("Forcibly closing remotely exposed cursor");
                        try {
                            monitored.close();
                        } catch (Exception e) {
                            Timber.w(e, "Exception while forcibly closing cursor");
                        }
                    }
                }
            }, 30, TimeUnit.SECONDS);

            return wrapped;
        }
    }