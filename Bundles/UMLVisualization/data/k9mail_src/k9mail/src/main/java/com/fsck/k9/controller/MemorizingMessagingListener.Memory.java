package com.fsck.k9.controller;


import com.fsck.k9.Account;
private static class Memory {
        Account account;
        String folderName;
        MemorizingState syncingState = null;
        MemorizingState sendingState = null;
        MemorizingState pushingState = null;
        MemorizingState processingState = null;
        String failureMessage = null;

        int syncingTotalMessagesInMailbox;
        int syncingNumNewMessages;

        int folderCompleted = 0;
        int folderTotal = 0;
        String processingCommandTitle = null;

        Memory(Account nAccount, String nFolderName) {
            account = nAccount;
            folderName = nFolderName;
        }
    }