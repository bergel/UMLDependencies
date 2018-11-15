package com.fsck.k9.controller;


import com.fsck.k9.Account;
import com.fsck.k9.mail.MessagingException;

import java.util.List;
import java.util.Map;
public static class PendingMoveOrCopy extends PendingCommand {
        public final String srcFolder;
        public final String destFolder;
        public final boolean isCopy;
        public final List<String> uids;
        public final Map<String, String> newUidMap;


        public static PendingMoveOrCopy create(String srcFolder, String destFolder, boolean isCopy,
                Map<String, String> uidMap) {
            return new PendingMoveOrCopy(srcFolder, destFolder, isCopy, null, uidMap);
        }

        public static PendingMoveOrCopy create(String srcFolder, String destFolder, boolean isCopy, List<String> uids) {
            return new PendingMoveOrCopy(srcFolder, destFolder, isCopy, uids, null);
        }

        private PendingMoveOrCopy(String srcFolder, String destFolder, boolean isCopy, List<String> uids,
                Map<String, String> newUidMap) {
            this.srcFolder = srcFolder;
            this.destFolder = destFolder;
            this.isCopy = isCopy;
            this.uids = uids;
            this.newUidMap = newUidMap;
        }

        @Override
        public String getCommandName() {
            return COMMAND_MOVE_OR_COPY;
        }

        @Override
        public void execute(MessagingController controller, Account account) throws MessagingException {
            controller.processPendingMoveOrCopy(this, account);
        }
    }