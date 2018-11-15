package com.fsck.k9.fragment;

import com.fsck.k9.Account;
import com.fsck.k9.activity.MessageReference;
public interface MessageListFragmentListener {
        void enableActionBarProgress(boolean enable);
        void setMessageListProgress(int level);
        void showThread(Account account, String folderName, long rootId);
        void showMoreFromSameSender(String senderAddress);
        void onResendMessage(MessageReference message);
        void onForward(MessageReference message);
        void onReply(MessageReference message);
        void onReplyAll(MessageReference message);
        void openMessage(MessageReference messageReference);
        void setMessageListTitle(String title);
        void setMessageListSubTitle(String subTitle);
        void setUnreadCount(int unread);
        void onCompose(Account account);
        boolean startSearch(Account account, String folderName);
        void remoteSearchStarted();
        void goBack();
        void updateMenu();
    }