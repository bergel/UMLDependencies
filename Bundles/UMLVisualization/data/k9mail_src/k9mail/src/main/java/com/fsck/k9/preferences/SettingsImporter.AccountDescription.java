package com.fsck.k9.preferences;


public static class AccountDescription {
        public final String name;
        public final String uuid;

        private AccountDescription(String name, String uuid) {
            this.name = name;
            this.uuid = uuid;
        }
    }