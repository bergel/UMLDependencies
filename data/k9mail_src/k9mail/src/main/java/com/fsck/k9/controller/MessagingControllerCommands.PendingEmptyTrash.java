package com.fsck.k9.controller;


import com.fsck.k9.Account;
import com.fsck.k9.mail.MessagingException;
public static class PendingEmptyTrash extends PendingCommand {
        public static PendingEmptyTrash create() {
            return new PendingEmptyTrash();
        }

        @Override
        public String getCommandName() {
            return COMMAND_EMPTY_TRASH;
        }

        @Override
        public void execute(MessagingController controller, Account account) throws MessagingException {
            controller.processPendingEmptyTrash(account);
        }
    }