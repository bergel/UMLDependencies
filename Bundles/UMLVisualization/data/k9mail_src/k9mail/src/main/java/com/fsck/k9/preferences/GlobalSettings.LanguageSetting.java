package com.fsck.k9.preferences;


import com.fsck.k9.K9;
import com.fsck.k9.R;
import com.fsck.k9.preferences.Settings.InvalidSettingValueException;
import com.fsck.k9.preferences.Settings.PseudoEnumSetting;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
private static class LanguageSetting extends PseudoEnumSetting<String> {
        private final Map<String, String> mapping;

        LanguageSetting() {
            super("");

            Map<String, String> mapping = new HashMap<>();
            String[] values = K9.app.getResources().getStringArray(R.array.settings_language_values);
            for (String value : values) {
                if (value.length() == 0) {
                    mapping.put("", "default");
                } else {
                    mapping.put(value, value);
                }
            }
            this.mapping = Collections.unmodifiableMap(mapping);
        }

        @Override
        protected Map<String, String> getMapping() {
            return mapping;
        }

        @Override
        public String fromString(String value) throws InvalidSettingValueException {
            if (mapping.containsKey(value)) {
                return value;
            }

            throw new InvalidSettingValueException();
        }
    }