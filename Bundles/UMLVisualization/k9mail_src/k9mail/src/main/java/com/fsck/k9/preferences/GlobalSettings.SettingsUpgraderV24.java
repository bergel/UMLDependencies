package com.fsck.k9.preferences;


import com.fsck.k9.K9;
import com.fsck.k9.K9.Theme;
import com.fsck.k9.preferences.Settings.SettingsUpgrader;

import java.util.Map;
import java.util.Set;
private static class SettingsUpgraderV24 implements SettingsUpgrader {

        @Override
        public Set<String> upgrade(Map<String, Object> settings) {
            K9.Theme messageViewTheme = (K9.Theme) settings.get("messageViewTheme");
            K9.Theme theme = (K9.Theme) settings.get("theme");
            if (theme != null && messageViewTheme != null && theme == messageViewTheme) {
                settings.put("messageViewTheme", K9.Theme.USE_GLOBAL);
            }

            return null;
        }
    }