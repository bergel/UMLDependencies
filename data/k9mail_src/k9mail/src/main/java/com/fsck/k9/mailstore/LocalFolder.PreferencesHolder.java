package com.fsck.k9.mailstore;


class PreferencesHolder {
        FolderClass displayClass = LocalFolder.this.displayClass;
        FolderClass syncClass = LocalFolder.this.syncClass;
        FolderClass notifyClass = LocalFolder.this.notifyClass;
        FolderClass pushClass = LocalFolder.this.pushClass;
        boolean inTopGroup = isInTopGroup;
        boolean integrate = isIntegrate;
    }