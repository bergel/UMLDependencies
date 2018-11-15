package com.fsck.k9.mail.internet;

import java.util.*;
private static class Field {
        private final String name;
        private final String value;
        private final String raw;

        public static Field newNameValueField(String name, String value) {
            if (value == null) {
                throw new NullPointerException("Argument 'value' cannot be null");
            }

            return new Field(name, value, null);
        }

        public static Field newRawField(String name, String raw) {
            if (raw == null) {
                throw new NullPointerException("Argument 'raw' cannot be null");
            }

            return new Field(name, null, raw);
        }

        private Field(String name, String value, String raw) {
            if (name == null) {
                throw new NullPointerException("Argument 'name' cannot be null");
            }

            this.name = name;
            this.value = value;
            this.raw = raw;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            if (value != null) {
                return value;
            }

            int delimiterIndex = raw.indexOf(':');
            if (delimiterIndex == raw.length() - 1) {
                return "";
            }

            return raw.substring(delimiterIndex + 1).trim();
        }

        public String getRaw() {
            return raw;
        }

        public boolean hasRawData() {
            return raw != null;
        }

        @Override
        public String toString() {
            return (hasRawData()) ? getRaw() : getName() + ": " + getValue();
        }
    }