package com.fsck.k9.mail.internet;


import timber.log.Timber;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * A Body that is backed by a temp file. The Body exposes a getOutputStream method that allows
 * the user to write to the temp file. After the write the body is available via getInputStream
 * and writeTo one time. After writeTo is called, or the InputStream returned from
 * getInputStream is closed the file is deleted and the Body should be considered disposed of.
 */
class BinaryTempFileBodyInputStream extends FilterInputStream {
        public BinaryTempFileBodyInputStream(InputStream in) {
            super(in);
        }

        @Override
        public void close() throws IOException {
            try {
                super.close();
            } finally {
                Timber.d("Deleting temporary binary file: %s", mFile.getName());
                boolean fileSuccessfullyDeleted = mFile.delete();
                if (!fileSuccessfullyDeleted) {
                    Timber.i("Failed to delete temporary binary file: %s", mFile.getName());
                }
            }
        }

        public void closeWithoutDeleting() throws IOException {
            super.close();
        }
    }