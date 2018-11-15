package com.fsck.k9.preferences;


/*
 * TODO:
 * - use the default values defined in GlobalSettings and AccountSettings when creating new
 *   accounts
 * - think of a better way to validate enums than to use the resource arrays (i.e. get rid of
 *   ResourceArrayValidator); maybe even use the settings description for the settings UI
 * - add unit test that validates the default values are actually valid according to the validator
 */
static class BooleanSetting extends SettingsDescription<Boolean> {
        BooleanSetting(boolean defaultValue) {
            super(defaultValue);
        }

        @Override
        public Boolean fromString(String value) throws InvalidSettingValueException {
            if (Boolean.TRUE.toString().equals(value)) {
                return true;
            } else if (Boolean.FALSE.toString().equals(value)) {
                return false;
            }
            throw new InvalidSettingValueException();
        }
    }