package com.fsck.k9;


import android.app.Application;
public static interface ApplicationAware {
        /**
         * Called when the Application instance is available and ready.
         *
         * @param application
         *            The application instance. Never <code>null</code>.
         * @throws Exception
         */
        void initializeComponent(Application application);
    }