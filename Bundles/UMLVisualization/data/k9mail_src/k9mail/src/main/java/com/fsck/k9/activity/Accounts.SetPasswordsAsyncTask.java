package com.fsck.k9.activity;


import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import com.fsck.k9.Account;
import com.fsck.k9.K9;
import com.fsck.k9.Preferences;
import com.fsck.k9.R;
import com.fsck.k9.activity.misc.ExtendedAsyncTask;
import com.fsck.k9.controller.MessagingController;
import com.fsck.k9.mail.ServerSettings;
import com.fsck.k9.mail.TransportUris;
import com.fsck.k9.mail.store.RemoteStore;
import timber.log.Timber;

import java.util.List;
private static class SetPasswordsAsyncTask extends ExtendedAsyncTask<Void, Void, Void> {
        private Account mAccount;
        private String mIncomingPassword;
        private String mOutgoingPassword;
        private List<Account> mRemainingAccounts;
        private Application mApplication;

        protected SetPasswordsAsyncTask(Activity activity, Account account,
                                        String incomingPassword, String outgoingPassword,
                                        List<Account> remainingAccounts) {
            super(activity);
            mAccount = account;
            mIncomingPassword = incomingPassword;
            mOutgoingPassword = outgoingPassword;
            mRemainingAccounts = remainingAccounts;
            mApplication = mActivity.getApplication();
        }

        @Override
        protected void showProgressDialog() {
            String title = mActivity.getString(R.string.settings_import_activate_account_header);
            int passwordCount = (mOutgoingPassword == null) ? 1 : 2;
            String message = mActivity.getResources().getQuantityString(
                                 R.plurals.settings_import_setting_passwords, passwordCount);
            mProgressDialog = ProgressDialog.show(mActivity, title, message, true);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                if (mIncomingPassword != null) {
                    // Set incoming server password
                    String storeUri = mAccount.getStoreUri();
                    ServerSettings incoming = RemoteStore.decodeStoreUri(storeUri);
                    ServerSettings newIncoming = incoming.newPassword(mIncomingPassword);
                    String newStoreUri = RemoteStore.createStoreUri(newIncoming);
                    mAccount.setStoreUri(newStoreUri);
                }

                if (mOutgoingPassword != null) {
                    // Set outgoing server password
                    String transportUri = mAccount.getTransportUri();
                    ServerSettings outgoing = TransportUris.decodeTransportUri(transportUri);
                    ServerSettings newOutgoing = outgoing.newPassword(mOutgoingPassword);
                    String newTransportUri = TransportUris.createTransportUri(newOutgoing);
                    mAccount.setTransportUri(newTransportUri);
                }

                // Mark account as enabled
                mAccount.setEnabled(true);

                // Save the account settings
                mAccount.save(Preferences.getPreferences(mContext));

                // Start services if necessary
                K9.setServicesEnabled(mContext);

                // Get list of folders from remote server
                MessagingController.getInstance(mApplication).listFolders(mAccount, true, null);
            } catch (Exception e) {
                Timber.e(e, "Something went while setting account passwords");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Accounts activity = (Accounts) mActivity;

            // Let the activity know that the background task is complete
            activity.setNonConfigurationInstance(null);

            activity.refresh();
            removeProgressDialog();

            if (mRemainingAccounts.size() > 0) {
                activity.promptForServerPasswords(mRemainingAccounts);
            }
        }
    }