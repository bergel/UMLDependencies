package com.fsck.k9.preferences;


import android.support.annotation.VisibleForTesting;

import java.util.List;
@VisibleForTesting
    static class ImportedAccount {
        public String uuid;
        public String name;
        public ImportedServer incoming;
        public ImportedServer outgoing;
        public ImportedSettings settings;
        public List<ImportedIdentity> identities;
        public List<ImportedFolder> folders;
    }