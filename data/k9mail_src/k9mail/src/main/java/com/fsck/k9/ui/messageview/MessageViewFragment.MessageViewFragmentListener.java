package com.fsck.k9.ui.messageview;


import android.os.Parcelable;
import com.fsck.k9.activity.MessageReference;
import com.fsck.k9.view.MessageHeader;
public interface MessageViewFragmentListener {
        void onForward(MessageReference messageReference, Parcelable decryptionResultForReply);
        void disableDeleteAction();
        void onReplyAll(MessageReference messageReference, Parcelable decryptionResultForReply);
        void onReply(MessageReference messageReference, Parcelable decryptionResultForReply);
        void displayMessageSubject(String title);
        void setProgress(boolean b);
        void showNextMessageOrReturn();
        void messageHeaderViewAvailable(MessageHeader messageHeaderView);
        void updateMenu();
    }