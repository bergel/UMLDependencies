package com.fsck.k9.preferences;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/*
 * TODO:
 * - use the default values defined in GlobalSettings and AccountSettings when creating new
 *   accounts
 * - think of a better way to validate enums than to use the resource arrays (i.e. get rid of
 *   ResourceArrayValidator); maybe even use the settings description for the settings UI
 * - add unit test that validates the default values are actually valid according to the validator
 */
static class WebFontSizeSetting extends PseudoEnumSetting<Integer> {
        private final Map<Integer, String> mapping;

        WebFontSizeSetting(int defaultValue) {
            super(defaultValue);

            Map<Integer, String> mapping = new HashMap<>();
            mapping.put(1, "smallest");
            mapping.put(2, "smaller");
            mapping.put(3, "normal");
            mapping.put(4, "larger");
            mapping.put(5, "largest");
            this.mapping = Collections.unmodifiableMap(mapping);
        }

        @Override
        protected Map<Integer, String> getMapping() {
            return mapping;
        }

        @Override
        public Integer fromString(String value) throws InvalidSettingValueException {
            try {
                Integer fontSize = Integer.parseInt(value);
                if (mapping.containsKey(fontSize)) {
                    return fontSize;
                }
            } catch (NumberFormatException e) { /* do nothing */ }

            throw new InvalidSettingValueException();
        }
    }