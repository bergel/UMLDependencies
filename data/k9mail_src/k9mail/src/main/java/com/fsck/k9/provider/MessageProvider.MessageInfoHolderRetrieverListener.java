package com.fsck.k9.provider;


import android.content.Context;
import com.fsck.k9.Account;
import com.fsck.k9.AccountStats;
import com.fsck.k9.activity.FolderInfoHolder;
import com.fsck.k9.activity.MessageInfoHolder;
import com.fsck.k9.controller.SimpleMessagingListener;
import com.fsck.k9.mailstore.LocalFolder;
import com.fsck.k9.mailstore.LocalMessage;
import timber.log.Timber;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
protected class MessageInfoHolderRetrieverListener extends SimpleMessagingListener {
        private final BlockingQueue<List<MessageInfoHolder>> queue;
        private List<MessageInfoHolder> holders = new ArrayList<MessageInfoHolder>();


        public MessageInfoHolderRetrieverListener(BlockingQueue<List<MessageInfoHolder>> queue) {
            this.queue = queue;
        }

        @Override
        public void listLocalMessagesAddMessages(Account account, String folderName, List<LocalMessage> messages) {
            Context context = getContext();

            for (LocalMessage message : messages) {
                MessageInfoHolder messageInfoHolder = new MessageInfoHolder();
                LocalFolder messageFolder = message.getFolder();
                Account messageAccount = message.getAccount();

                FolderInfoHolder folderInfoHolder = new FolderInfoHolder(context, messageFolder, messageAccount);
                messageHelper.populate(messageInfoHolder, message, folderInfoHolder, messageAccount);

                holders.add(messageInfoHolder);
            }
        }

        @Override
        public void searchStats(AccountStats stats) {
            try {
                queue.put(holders);
            } catch (InterruptedException e) {
                Timber.e(e, "Unable to return message list back to caller");
            }
        }
    }