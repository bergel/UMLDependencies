package com.fsck.k9.controller;


import com.fsck.k9.Account;
import com.fsck.k9.mail.MessagingException;
public abstract static class PendingCommand {
        public long databaseId;


        PendingCommand() { }

        public abstract String getCommandName();
        public abstract void execute(MessagingController controller, Account account) throws MessagingException;
    }