package com.fsck.k9.controller;


import com.fsck.k9.Account;
import com.fsck.k9.mail.Flag;
import com.fsck.k9.mail.MessagingException;

import java.util.List;
public static class PendingSetFlag extends PendingCommand {
        public final String folder;
        public final boolean newState;
        public final Flag flag;
        public final List<String> uids;


        public static PendingSetFlag create(String folder, boolean newState, Flag flag, List<String> uids) {
            return new PendingSetFlag(folder, newState, flag, uids);
        }

        private PendingSetFlag(String folder, boolean newState, Flag flag, List<String> uids) {
            this.folder = folder;
            this.newState = newState;
            this.flag = flag;
            this.uids = uids;
        }

        @Override
        public String getCommandName() {
            return COMMAND_SET_FLAG;
        }

        @Override
        public void execute(MessagingController controller, Account account) throws MessagingException {
            controller.processPendingSetFlag(this, account);
        }
    }