package com.fsck.k9.preferences;


import com.fsck.k9.K9;
import com.fsck.k9.R;
import com.fsck.k9.preferences.Settings.InvalidSettingValueException;
import com.fsck.k9.preferences.Settings.SettingsDescription;
private static class SignatureSetting extends SettingsDescription<String> {
        SignatureSetting() {
            super(null);
        }

        @Override
        public String getDefaultValue() {
            return K9.app.getResources().getString(R.string.default_signature);
        }

        @Override
        public String fromString(String value) throws InvalidSettingValueException {
            return value;
        }
    }