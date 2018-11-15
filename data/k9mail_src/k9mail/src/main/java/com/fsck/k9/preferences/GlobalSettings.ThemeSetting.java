package com.fsck.k9.preferences;


import com.fsck.k9.K9;
import com.fsck.k9.K9.Theme;
import com.fsck.k9.R;
import com.fsck.k9.preferences.Settings.InvalidSettingValueException;
import com.fsck.k9.preferences.Settings.SettingsDescription;
static class ThemeSetting extends SettingsDescription<K9.Theme> {
        private static final String THEME_LIGHT = "light";
        private static final String THEME_DARK = "dark";

        ThemeSetting(K9.Theme defaultValue) {
            super(defaultValue);
        }

        @Override
        public K9.Theme fromString(String value) throws InvalidSettingValueException {
            try {
                Integer theme = Integer.parseInt(value);
                if (theme == K9.Theme.LIGHT.ordinal() ||
                        // We used to store the resource ID of the theme in the preference storage,
                        // but don't use the database upgrade mechanism to update the values. So
                        // we have to deal with the old format here.
                        theme == android.R.style.Theme_Light) {
                    return K9.Theme.LIGHT;
                } else if (theme == K9.Theme.DARK.ordinal() || theme == android.R.style.Theme) {
                    return K9.Theme.DARK;
                }
            } catch (NumberFormatException e) { /* do nothing */ }

            throw new InvalidSettingValueException();
        }

        @Override
        public K9.Theme fromPrettyString(String value) throws InvalidSettingValueException {
            if (THEME_LIGHT.equals(value)) {
                return K9.Theme.LIGHT;
            } else if (THEME_DARK.equals(value)) {
                return K9.Theme.DARK;
            }

            throw new InvalidSettingValueException();
        }

        @Override
        public String toPrettyString(K9.Theme value) {
            switch (value) {
                case DARK: {
                    return THEME_DARK;
                }
                default: {
                    return THEME_LIGHT;
                }
            }
        }

        @Override
        public String toString(K9.Theme value) {
            return Integer.toString(value.ordinal());
        }
    }