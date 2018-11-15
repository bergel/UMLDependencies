package com.fsck.k9.controller;


import com.fsck.k9.Account;
import com.fsck.k9.mail.MessagingException;
public static class PendingAppend extends PendingCommand {
        public final String folder;
        public final String uid;


        public static PendingAppend create(String folderName, String uid) {
            return new PendingAppend(folderName, uid);
        }

        private PendingAppend(String folder, String uid) {
            this.folder = folder;
            this.uid = uid;
        }

        @Override
        public String getCommandName() {
            return COMMAND_APPEND;
        }

        @Override
        public void execute(MessagingController controller, Account account) throws MessagingException {
            controller.processPendingAppend(this, account);
        }
    }