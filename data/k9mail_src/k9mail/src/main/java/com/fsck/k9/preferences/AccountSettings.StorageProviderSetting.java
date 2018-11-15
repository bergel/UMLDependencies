package com.fsck.k9.preferences;


import com.fsck.k9.K9;
import com.fsck.k9.mailstore.StorageManager;
import com.fsck.k9.preferences.Settings.SettingsDescription;

import java.util.Map;
private static class StorageProviderSetting extends SettingsDescription<String> {
        StorageProviderSetting() {
            super(null);
        }

        @Override
        public String getDefaultValue() {
            return StorageManager.getInstance(K9.app).getDefaultProviderId();
        }

        @Override
        public String fromString(String value) {
            StorageManager storageManager = StorageManager.getInstance(K9.app);
            Map<String, String> providers = storageManager.getAvailableProviders();
            if (providers.containsKey(value)) {
                return value;
            }
            throw new RuntimeException("Validation failed");
        }
    }