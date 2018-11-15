package com.fsck.k9.helper;

public interface FileBrowserFailOverCallback {
        /**
         * the user has entered a path
         * @param path the path as String
         */
        public void onPathEntered(String path);
        /**
         * the user has cancel the inputtext dialog
         */
        public void onCancel();
    }