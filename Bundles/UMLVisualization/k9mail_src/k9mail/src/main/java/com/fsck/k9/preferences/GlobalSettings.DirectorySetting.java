package com.fsck.k9.preferences;


import com.fsck.k9.preferences.Settings.InvalidSettingValueException;
import com.fsck.k9.preferences.Settings.SettingsDescription;

import java.io.File;
private static class DirectorySetting extends SettingsDescription<String> {
        DirectorySetting(File defaultPath) {
            super(defaultPath.toString());
        }

        @Override
        public String fromString(String value) throws InvalidSettingValueException {
            try {
                if (new File(value).isDirectory()) {
                    return value;
                }
            } catch (Exception e) { /* do nothing */ }

            throw new InvalidSettingValueException();
        }
    }