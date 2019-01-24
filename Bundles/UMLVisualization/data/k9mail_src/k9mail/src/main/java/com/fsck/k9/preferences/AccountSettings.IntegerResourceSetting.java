package com.fsck.k9.preferences;


import com.fsck.k9.K9;
import com.fsck.k9.preferences.Settings.InvalidSettingValueException;
import com.fsck.k9.preferences.Settings.PseudoEnumSetting;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
private static class IntegerResourceSetting extends PseudoEnumSetting<Integer> {
        private final Map<Integer, String> mapping;

        IntegerResourceSetting(int defaultValue, int resId) {
            super(defaultValue);

            Map<Integer, String> mapping = new HashMap<>();
            String[] values = K9.app.getResources().getStringArray(resId);
            for (String value : values) {
                int intValue = Integer.parseInt(value);
                mapping.put(intValue, value);
            }
            this.mapping = Collections.unmodifiableMap(mapping);
        }

        @Override
        protected Map<Integer, String> getMapping() {
            return mapping;
        }

        @Override
        public Integer fromString(String value) throws InvalidSettingValueException {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                throw new InvalidSettingValueException();
            }
        }
    }