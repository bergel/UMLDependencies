package com.fsck.k9.mail;
private interface WritableDebugStatus extends DebugStatus {
        void setEnabled(boolean enabled);

        void setSensitive(boolean sensitive);
    }