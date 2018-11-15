package com.fsck.k9.fragment;

public interface ConfirmationDialogFragmentListener {
        void doPositiveClick(int dialogId);
        void doNegativeClick(int dialogId);
        void dialogCancelled(int dialogId);
    }