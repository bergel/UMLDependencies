package com.fsck.k9.mail.store.imap;


import com.fsck.k9.mail.store.RemoteStore;

import java.io.IOException;
private static class IdleStopper {
        private boolean acceptDoneContinuation = false;
        private ImapConnection imapConnection;


        public synchronized void startAcceptingDoneContinuation(ImapConnection connection) {
            if (connection == null) {
                throw new NullPointerException("connection must not be null");
            }

            acceptDoneContinuation = true;
            imapConnection = connection;
        }

        public synchronized void stopAcceptingDoneContinuation() {
            acceptDoneContinuation = false;
            imapConnection = null;
        }

        public synchronized void stopIdle() {
            if (acceptDoneContinuation) {
                acceptDoneContinuation = false;
                sendDone();
            }
        }

        private void sendDone() {
            try {
                imapConnection.setReadTimeout(RemoteStore.SOCKET_READ_TIMEOUT);
                imapConnection.sendContinuation("DONE");
            } catch (IOException e) {
                imapConnection.close();
            }
        }
    }