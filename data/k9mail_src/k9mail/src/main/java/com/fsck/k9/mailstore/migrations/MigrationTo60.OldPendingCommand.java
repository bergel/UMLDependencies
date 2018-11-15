package com.fsck.k9.mailstore.migrations;


import android.support.annotation.VisibleForTesting;
@VisibleForTesting
    static class OldPendingCommand {
        public String command;
        public String[] arguments;
    }