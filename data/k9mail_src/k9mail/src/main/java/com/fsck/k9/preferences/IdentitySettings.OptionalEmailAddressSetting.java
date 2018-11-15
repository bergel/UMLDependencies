package com.fsck.k9.preferences;


import com.fsck.k9.EmailAddressValidator;
import com.fsck.k9.preferences.Settings.InvalidSettingValueException;
import com.fsck.k9.preferences.Settings.SettingsDescription;
private static class OptionalEmailAddressSetting extends SettingsDescription<String> {
        private EmailAddressValidator validator;

        OptionalEmailAddressSetting() {
            super(null);
            validator = new EmailAddressValidator();
        }

        @Override
        public String fromString(String value) throws InvalidSettingValueException {
            if (value != null && !validator.isValidAddressOnly(value)) {
                throw new InvalidSettingValueException();
            }
            return value;
        }

        @Override
        public String toString(String value) {
            return value;
        }

        @Override
        public String toPrettyString(String value) {
            return (value == null) ? "" : value;
        }

        @Override
        public String fromPrettyString(String value) throws InvalidSettingValueException {
            return "".equals(value) ? null : fromString(value);
        }
    }