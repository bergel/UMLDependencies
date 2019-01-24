package com.fsck.k9.activity.compose;


import com.fsck.k9.activity.misc.Attachment;
public interface AttachmentMvpView {
        void showWaitingForAttachmentDialog(WaitingAction waitingAction);
        void dismissWaitingForAttachmentDialog();
        void showPickAttachmentDialog(int requestCode);

        void addAttachmentView(Attachment attachment);
        void removeAttachmentView(Attachment attachment);
        void updateAttachmentView(Attachment attachment);

        // TODO these should not really be here :\
        void performSendAfterChecks();
        void performSaveAfterChecks();

        void showMissingAttachmentsPartialMessageWarning();
    }