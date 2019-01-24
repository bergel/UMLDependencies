package com.fsck.k9.preferences;


/*
 * TODO:
 * - use the default values defined in GlobalSettings and AccountSettings when creating new
 *   accounts
 * - think of a better way to validate enums than to use the resource arrays (i.e. get rid of
 *   ResourceArrayValidator); maybe even use the settings description for the settings UI
 * - add unit test that validates the default values are actually valid according to the validator
 */
static class IntegerRangeSetting extends SettingsDescription<Integer> {
        private int start;
        private int end;

        IntegerRangeSetting(int start, int end, int defaultValue) {
            super(defaultValue);
            this.start = start;
            this.end = end;
        }

        @Override
        public Integer fromString(String value) throws InvalidSettingValueException {
            try {
                int intValue = Integer.parseInt(value);
                if (start <= intValue && intValue <= end) {
                    return intValue;
                }
            } catch (NumberFormatException e) { /* do nothing */ }

            throw new InvalidSettingValueException();
        }
    }