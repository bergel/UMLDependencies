package com.fsck.k9.preferences;


import com.fsck.k9.K9.NotificationHideSubject;
import com.fsck.k9.preferences.Settings.SettingsUpgrader;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
private static class SettingsUpgraderV12 implements SettingsUpgrader {

        @Override
        public Set<String> upgrade(Map<String, Object> settings) {
            Boolean keyguardPrivacy = (Boolean) settings.get("keyguardPrivacy");
            if (keyguardPrivacy != null && keyguardPrivacy) {
                // current setting: only show subject when unlocked
                settings.put("notificationHideSubject", NotificationHideSubject.WHEN_LOCKED);
            } else {
                // always show subject [old default]
                settings.put("notificationHideSubject", NotificationHideSubject.NEVER);
            }
            return new HashSet<>(Collections.singletonList("keyguardPrivacy"));
        }
    }