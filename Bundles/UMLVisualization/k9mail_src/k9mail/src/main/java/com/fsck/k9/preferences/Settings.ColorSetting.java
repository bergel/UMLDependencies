package com.fsck.k9.preferences;


/*
 * TODO:
 * - use the default values defined in GlobalSettings and AccountSettings when creating new
 *   accounts
 * - think of a better way to validate enums than to use the resource arrays (i.e. get rid of
 *   ResourceArrayValidator); maybe even use the settings description for the settings UI
 * - add unit test that validates the default values are actually valid according to the validator
 */
static class ColorSetting extends SettingsDescription<Integer> {
        ColorSetting(int defaultValue) {
            super(defaultValue);
        }

        @Override
        public Integer fromString(String value) throws InvalidSettingValueException {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                throw new InvalidSettingValueException();
            }
        }

        @Override
        public String toPrettyString(Integer value) {
            int color = value & 0x00FFFFFF;
            return String.format("#%06x", color);
        }

        @Override
        public Integer fromPrettyString(String value) throws InvalidSettingValueException {
            try {
                if (value.length() == 7) {
                    return Integer.parseInt(value.substring(1), 16) | 0xFF000000;
                }
            } catch (NumberFormatException e) { /* do nothing */ }

            throw new InvalidSettingValueException();
        }
    }