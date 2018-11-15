package com.fsck.k9.view;


import android.view.View;
import com.fsck.k9.view.RecipientSelectView.Recipient;
private class RecipientTokenSpan extends TokenImageSpan {
        private final View view;


        public RecipientTokenSpan(View view, Recipient recipient, int token) {
            super(view, recipient, token);
            this.view = view;
        }
    }