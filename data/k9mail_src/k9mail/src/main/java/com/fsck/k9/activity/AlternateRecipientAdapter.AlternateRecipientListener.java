package com.fsck.k9.activity;


import com.fsck.k9.view.RecipientSelectView.Recipient;
public interface AlternateRecipientListener {
        void onRecipientRemove(Recipient currentRecipient);
        void onRecipientChange(Recipient currentRecipient, Recipient alternateRecipient);
    }