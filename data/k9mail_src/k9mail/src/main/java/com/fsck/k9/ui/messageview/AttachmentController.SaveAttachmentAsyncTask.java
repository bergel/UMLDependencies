package com.fsck.k9.ui.messageview;


import android.os.AsyncTask;
import timber.log.Timber;

import java.io.File;
import java.io.IOException;
private class SaveAttachmentAsyncTask extends AsyncTask<File, Void, File> {

        @Override
        protected void onPreExecute() {
            messageViewFragment.disableAttachmentButtons(attachment);
        }

        @Override
        protected File doInBackground(File... params) {
            try {
                File directory = params[0];
                return saveAttachmentWithUniqueFileName(directory);
            } catch (IOException e) {
                Timber.e(e, "Error saving attachment");
                return null;
            }
        }

        @Override
        protected void onPostExecute(File file) {
            messageViewFragment.enableAttachmentButtons(attachment);
            if (file == null) {
                displayAttachmentNotSavedMessage();
            }
        }
    }