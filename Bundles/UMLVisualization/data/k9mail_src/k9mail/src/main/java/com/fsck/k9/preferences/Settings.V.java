package com.fsck.k9.preferences;


/*
 * TODO:
 * - use the default values defined in GlobalSettings and AccountSettings when creating new
 *   accounts
 * - think of a better way to validate enums than to use the resource arrays (i.e. get rid of
 *   ResourceArrayValidator); maybe even use the settings description for the settings UI
 * - add unit test that validates the default values are actually valid according to the validator
 */
public static class V {
        public final Integer version;
        public final SettingsDescription description;

        V(Integer version, SettingsDescription description) {
            this.version = version;
            this.description = description;
        }
    }