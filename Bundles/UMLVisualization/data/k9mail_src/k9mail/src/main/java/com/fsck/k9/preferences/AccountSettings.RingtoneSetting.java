package com.fsck.k9.preferences;


import com.fsck.k9.preferences.Settings.SettingsDescription;
private static class RingtoneSetting extends SettingsDescription<String> {
        RingtoneSetting(String defaultValue) {
            super(defaultValue);
        }

        @Override
        public String fromString(String value) {
            //TODO: add validation
            return value;
        }
    }