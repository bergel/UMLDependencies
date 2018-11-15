package com.fsck.k9.mail;
private static class DefaultDebugStatus implements WritableDebugStatus {
        private boolean enabled;
        private boolean sensitive;

        @Override
        public boolean enabled() {
            return enabled;
        }

        @Override
        public boolean debugSensitive() {
            return sensitive;
        }

        @Override
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        @Override
        public void setSensitive(boolean sensitive) {
            this.sensitive = sensitive;
        }
    }