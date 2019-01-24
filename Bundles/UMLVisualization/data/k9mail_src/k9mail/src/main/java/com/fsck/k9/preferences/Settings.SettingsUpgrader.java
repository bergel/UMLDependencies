package com.fsck.k9.preferences;


import java.util.Map;
import java.util.Set;

/*
 * TODO:
 * - use the default values defined in GlobalSettings and AccountSettings when creating new
 *   accounts
 * - think of a better way to validate enums than to use the resource arrays (i.e. get rid of
 *   ResourceArrayValidator); maybe even use the settings description for the settings UI
 * - add unit test that validates the default values are actually valid according to the validator
 */
interface SettingsUpgrader {
        /**
         * Upgrade the provided settings.
         *
         * @param settings
         *         The settings to upgrade.  This map is modified and contains the upgraded
         *         settings when this method returns.
         *
         * @return A set of setting names that were removed during the upgrade process or
         *         {@code null} if none were removed.
         */
        Set<String> upgrade(Map<String, Object> settings);
    }