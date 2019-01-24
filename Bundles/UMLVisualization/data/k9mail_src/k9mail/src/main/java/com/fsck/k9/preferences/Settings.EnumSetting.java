package com.fsck.k9.preferences;


/*
 * TODO:
 * - use the default values defined in GlobalSettings and AccountSettings when creating new
 *   accounts
 * - think of a better way to validate enums than to use the resource arrays (i.e. get rid of
 *   ResourceArrayValidator); maybe even use the settings description for the settings UI
 * - add unit test that validates the default values are actually valid according to the validator
 */
static class EnumSetting<T extends Enum<T>> extends SettingsDescription<T> {
        private Class<T> enumClass;

        EnumSetting(Class<T> enumClass, T defaultValue) {
            super(defaultValue);
            this.enumClass = enumClass;
        }

        @Override
        public T fromString(String value) throws InvalidSettingValueException {
            try {
                return Enum.valueOf(enumClass, value);
            } catch (Exception e) {
                throw new InvalidSettingValueException();
            }
        }
    }