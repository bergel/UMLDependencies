package com.fsck.k9.preferences;


import com.fsck.k9.K9;
import com.fsck.k9.K9.Theme;
import com.fsck.k9.preferences.Settings.InvalidSettingValueException;
private static class SubThemeSetting extends ThemeSetting {
        private static final String THEME_USE_GLOBAL = "use_global";

        SubThemeSetting(Theme defaultValue) {
            super(defaultValue);
        }

        @Override
        public K9.Theme fromString(String value) throws InvalidSettingValueException {
            try {
                Integer theme = Integer.parseInt(value);
                if (theme == K9.Theme.USE_GLOBAL.ordinal()) {
                    return K9.Theme.USE_GLOBAL;
                }

                return super.fromString(value);
            } catch (NumberFormatException e) {
                throw new InvalidSettingValueException();
            }
        }

        @Override
        public K9.Theme fromPrettyString(String value) throws InvalidSettingValueException {
            if (THEME_USE_GLOBAL.equals(value)) {
                return K9.Theme.USE_GLOBAL;
            }

            return super.fromPrettyString(value);
        }

        @Override
        public String toPrettyString(K9.Theme value) {
            if (value == K9.Theme.USE_GLOBAL) {
                return THEME_USE_GLOBAL;
            }

            return super.toPrettyString(value);
        }
    }