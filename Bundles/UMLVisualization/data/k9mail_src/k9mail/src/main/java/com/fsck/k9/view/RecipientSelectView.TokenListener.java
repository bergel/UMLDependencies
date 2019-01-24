package com.fsck.k9.view;


import com.tokenautocomplete.TokenCompleteTextView;
public interface TokenListener<T> extends TokenCompleteTextView.TokenListener<T> {
        void onTokenChanged(T token);
    }