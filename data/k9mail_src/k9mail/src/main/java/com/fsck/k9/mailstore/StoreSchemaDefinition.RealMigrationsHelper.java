package com.fsck.k9.mailstore;


import android.content.Context;
import com.fsck.k9.Account;
import com.fsck.k9.mail.Flag;
import com.fsck.k9.mailstore.migrations.MigrationsHelper;
import com.fsck.k9.preferences.Storage;

import java.util.List;
private static class RealMigrationsHelper implements MigrationsHelper {
        private final LocalStore localStore;


        public RealMigrationsHelper(LocalStore localStore) {
            this.localStore = localStore;
        }

        @Override
        public LocalStore getLocalStore() {
            return localStore;
        }

        @Override
        public Storage getStorage() {
            return localStore.getStorage();
        }

        @Override
        public Account getAccount() {
            return localStore.getAccount();
        }

        @Override
        public Context getContext() {
            return localStore.getContext();
        }

        @Override
        public String serializeFlags(List<Flag> flags) {
            return LocalStore.serializeFlags(flags);
        }
    }