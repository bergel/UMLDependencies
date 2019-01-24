package com.fsck.k9.preferences;


import java.util.Map;
import java.util.Map.Entry;

/*
 * TODO:
 * - use the default values defined in GlobalSettings and AccountSettings when creating new
 *   accounts
 * - think of a better way to validate enums than to use the resource arrays (i.e. get rid of
 *   ResourceArrayValidator); maybe even use the settings description for the settings UI
 * - add unit test that validates the default values are actually valid according to the validator
 */
abstract static class PseudoEnumSetting<T> extends SettingsDescription<T> {
        PseudoEnumSetting(T defaultValue) {
            super(defaultValue);
        }

        protected abstract Map<T, String> getMapping();

        @Override
        public String toPrettyString(T value) {
            return getMapping().get(value);
        }

        @Override
        public T fromPrettyString(String value) throws InvalidSettingValueException {
            for (Entry<T, String> entry : getMapping().entrySet()) {
                if (entry.getValue().equals(value)) {
                    return entry.getKey();
                }
            }

            throw new InvalidSettingValueException();
        }
    }