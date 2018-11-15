package com.fsck.k9.preferences;


/*
 * TODO:
 * - use the default values defined in GlobalSettings and AccountSettings when creating new
 *   accounts
 * - think of a better way to validate enums than to use the resource arrays (i.e. get rid of
 *   ResourceArrayValidator); maybe even use the settings description for the settings UI
 * - add unit test that validates the default values are actually valid according to the validator
 */
abstract static class SettingsDescription<T> {
        /**
         * The setting's default value (internal representation).
         */
        T defaultValue;

        SettingsDescription(T defaultValue) {
            this.defaultValue = defaultValue;
        }

        /**
         * Get the default value.
         *
         * @return The internal representation of the default value.
         */
        public T getDefaultValue() {
            return defaultValue;
        }

        /**
         * Convert a setting's value to the string representation.
         *
         * @param value
         *         The internal representation of a setting's value.
         *
         * @return The string representation of {@code value}.
         */
        public String toString(T value) {
            return value.toString();
        }

        /**
         * Parse the string representation of a setting's value .
         *
         * @param value
         *         The string representation of a setting's value.
         *
         * @return The internal representation of the setting's value.
         *
         * @throws InvalidSettingValueException
         *         If {@code value} contains an invalid value.
         */
        public abstract T fromString(String value) throws InvalidSettingValueException;

        /**
         * Convert a setting value to the "pretty" string representation.
         *
         * @param value
         *         The setting's value.
         *
         * @return A pretty-printed version of the setting's value.
         */
        public String toPrettyString(T value) {
            return toString(value);
        }

        /**
         * Convert the pretty-printed version of a setting's value to the internal representation.
         *
         * @param value
         *         The pretty-printed version of the setting's value. See
         *         {@link #toPrettyString(Object)}.
         *
         * @return The internal representation of the setting's value.
         *
         * @throws InvalidSettingValueException
         *         If {@code value} contains an invalid value.
         */
        public T fromPrettyString(String value) throws InvalidSettingValueException {
            return fromString(value);
        }
    }