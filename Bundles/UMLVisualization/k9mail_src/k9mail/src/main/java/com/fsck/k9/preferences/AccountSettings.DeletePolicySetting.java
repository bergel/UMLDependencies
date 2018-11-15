package com.fsck.k9.preferences;


import com.fsck.k9.Account.DeletePolicy;
import com.fsck.k9.preferences.Settings.InvalidSettingValueException;
import com.fsck.k9.preferences.Settings.PseudoEnumSetting;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
private static class DeletePolicySetting extends PseudoEnumSetting<Integer> {
        private Map<Integer, String> mapping;

        DeletePolicySetting(DeletePolicy defaultValue) {
            super(defaultValue.setting);
            Map<Integer, String> mapping = new HashMap<>();
            mapping.put(DeletePolicy.NEVER.setting, "NEVER");
            mapping.put(DeletePolicy.ON_DELETE.setting, "DELETE");
            mapping.put(DeletePolicy.MARK_AS_READ.setting, "MARK_AS_READ");
            this.mapping = Collections.unmodifiableMap(mapping);
        }

        @Override
        protected Map<Integer, String> getMapping() {
            return mapping;
        }

        @Override
        public Integer fromString(String value) throws InvalidSettingValueException {
            try {
                Integer deletePolicy = Integer.parseInt(value);
                if (mapping.containsKey(deletePolicy)) {
                    return deletePolicy;
                }
            } catch (NumberFormatException e) { /* do nothing */ }

            throw new InvalidSettingValueException();
        }
    }