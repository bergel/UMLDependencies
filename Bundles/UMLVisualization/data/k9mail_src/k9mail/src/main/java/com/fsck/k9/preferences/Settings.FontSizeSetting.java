package com.fsck.k9.preferences;


import com.fsck.k9.FontSizes;

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
static class FontSizeSetting extends PseudoEnumSetting<Integer> {
        private final Map<Integer, String> mapping;

        FontSizeSetting(int defaultValue) {
            super(defaultValue);

            Map<Integer, String> mapping = new HashMap<>();
            mapping.put(FontSizes.FONT_10SP, "tiniest");
            mapping.put(FontSizes.FONT_12SP, "tiny");
            mapping.put(FontSizes.SMALL, "smaller");
            mapping.put(FontSizes.FONT_16SP, "small");
            mapping.put(FontSizes.MEDIUM, "medium");
            mapping.put(FontSizes.FONT_20SP, "large");
            mapping.put(FontSizes.LARGE, "larger");
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