package com.fsck.k9.preferences;


import android.support.annotation.VisibleForTesting;

import java.util.Map;
@VisibleForTesting
    static class Imported {
        public int contentVersion;
        public ImportedSettings globalSettings;
        public Map<String, ImportedAccount> accounts;
    }