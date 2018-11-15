package com.fsck.k9.activity.setup;

import android.os.AsyncTask;
import com.fsck.k9.*;
import com.fsck.k9.controller.MessagingController;
import com.fsck.k9.mail.AuthenticationFailedException;
import com.fsck.k9.mail.CertificateValidationException;
import com.fsck.k9.mail.MessagingException;
import com.fsck.k9.mail.Store;
import com.fsck.k9.mail.Transport;
import com.fsck.k9.mail.TransportProvider;
import com.fsck.k9.mail.store.webdav.WebDavStore;
import timber.log.Timber;

/**
 * Checks the given settings to make sure that they can be used to send and
 * receive mail.
 * 
 * XXX NOTE: The manifest for this app has it ignore config changes, because
 * it doesn't correctly deal with restarting while its thread is running.
 */
private class CheckAccountTask extends AsyncTask<CheckDirection, Integer, Void> {
        private final Account account;

        private CheckAccountTask(Account account) {
            this.account = account;
        }

        @Override
        protected Void doInBackground(CheckDirection... params) {
            final CheckDirection direction = params[0];
            try {
                /*
                 * This task could be interrupted at any point, but network operations can block,
                 * so relying on InterruptedException is not enough. Instead, check after
                 * each potentially long-running operation.
                 */
                if (cancelled()) {
                    return null;
                }

                clearCertificateErrorNotifications(direction);

                checkServerSettings(direction);

                if (cancelled()) {
                    return null;
                }

                setResult(RESULT_OK);
                finish();

            } catch (AuthenticationFailedException afe) {
                Timber.e(afe, "Error while testing settings");
                showErrorDialog(
                        R.string.account_setup_failed_dlg_auth_message_fmt,
                        afe.getMessage() == null ? "" : afe.getMessage());
            } catch (CertificateValidationException cve) {
                handleCertificateValidationException(cve);
            } catch (Exception e) {
                Timber.e(e, "Error while testing settings");
                String message = e.getMessage() == null ? "" : e.getMessage();
                showErrorDialog(R.string.account_setup_failed_dlg_server_message_fmt, message);
            }
            return null;
        }

        private void clearCertificateErrorNotifications(CheckDirection direction) {
            final MessagingController ctrl = MessagingController.getInstance(getApplication());
            ctrl.clearCertificateErrorNotifications(account, direction);
        }

        private boolean cancelled() {
            if (mDestroyed) {
                return true;
            }
            if (mCanceled) {
                finish();
                return true;
            }
            return false;
        }

        private void checkServerSettings(CheckDirection direction) throws MessagingException {
            switch (direction) {
                case INCOMING: {
                    checkIncoming();
                    break;
                }
                case OUTGOING: {
                    checkOutgoing();
                    break;
                }
            }
        }

        private void checkOutgoing() throws MessagingException {
            if (!(account.getRemoteStore() instanceof WebDavStore)) {
                publishProgress(R.string.account_setup_check_settings_check_outgoing_msg);
            }
            Transport transport = TransportProvider.getInstance().getTransport(K9.app, account);
            transport.close();
            try {
                transport.open();
            } finally {
                transport.close();
            }
        }

        private void checkIncoming() throws MessagingException {
            Store store = account.getRemoteStore();
            if (store instanceof WebDavStore) {
                publishProgress(R.string.account_setup_check_settings_authenticate);
            } else {
                publishProgress(R.string.account_setup_check_settings_check_incoming_msg);
            }
            store.checkSettings();

            if (store instanceof WebDavStore) {
                publishProgress(R.string.account_setup_check_settings_fetch);
            }
            MessagingController.getInstance(getApplication()).listFoldersSynchronous(account, true, null);
            MessagingController.getInstance(getApplication())
                    .synchronizeMailbox(account, account.getInboxFolderName(), null, null);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            setMessage(values[0]);
        }
    }