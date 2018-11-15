package com.fsck.k9.preferences;


import java.util.List;
public static class ImportResults {
        public final boolean globalSettings;
        public final List<AccountDescriptionPair> importedAccounts;
        public final List<AccountDescription> erroneousAccounts;

        private ImportResults(boolean globalSettings, List<AccountDescriptionPair> importedAccounts,
                List<AccountDescription> erroneousAccounts) {
            this.globalSettings = globalSettings;
            this.importedAccounts = importedAccounts;
            this.erroneousAccounts = erroneousAccounts;
        }
    }