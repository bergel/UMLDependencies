package com.fsck.k9.preferences;


public static class AccountDescriptionPair {
        public final AccountDescription original;
        public final AccountDescription imported;
        public final boolean overwritten;

        private AccountDescriptionPair(AccountDescription original, AccountDescription imported, boolean overwritten) {
            this.original = original;
            this.imported = imported;
            this.overwritten = overwritten;
        }
    }