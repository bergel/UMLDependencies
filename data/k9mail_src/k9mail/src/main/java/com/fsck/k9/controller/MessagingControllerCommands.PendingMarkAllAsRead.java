package com.fsck.k9.controller;


import com.fsck.k9.Account;
import com.fsck.k9.mail.MessagingException;
public static class PendingMarkAllAsRead extends PendingCommand {
        public final String folder;


        public static PendingMarkAllAsRead create(String folder) {
            return new PendingMarkAllAsRead(folder);
        }

        private PendingMarkAllAsRead(String folder) {
            this.folder = folder;
        }

        @Override
        public String getCommandName() {
            return COMMAND_MARK_ALL_AS_READ;
        }

        @Override
        public void execute(MessagingController controller, Account account) throws MessagingException {
            controller.processPendingMarkAllAsRead(this, account);
        }
    }