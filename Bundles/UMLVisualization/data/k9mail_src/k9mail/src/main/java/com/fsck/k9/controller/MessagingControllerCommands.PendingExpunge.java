package com.fsck.k9.controller;


import com.fsck.k9.Account;
import com.fsck.k9.mail.MessagingException;
public static class PendingExpunge extends PendingCommand {
        public final String folder;


        public static PendingExpunge create(String folderName) {
            return new PendingExpunge(folderName);
        }

        private PendingExpunge(String folder) {
            this.folder = folder;
        }

        @Override
        public String getCommandName() {
            return COMMAND_EXPUNGE;
        }

        @Override
        public void execute(MessagingController controller, Account account) throws MessagingException {
            controller.processPendingExpunge(this, account);
        }
    }