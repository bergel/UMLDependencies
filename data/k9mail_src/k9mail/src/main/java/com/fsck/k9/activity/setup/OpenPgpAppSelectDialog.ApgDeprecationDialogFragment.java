package com.fsck.k9.activity.setup;


import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import com.fsck.k9.ui.dialog.ApgDeprecationWarningDialog;
public static class ApgDeprecationDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new ApgDeprecationWarningDialog(getActivity());
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            super.onDismiss(dialog);

            ((OpenPgpAppSelectDialog) getActivity()).onDismissApgDialog();
        }
    }