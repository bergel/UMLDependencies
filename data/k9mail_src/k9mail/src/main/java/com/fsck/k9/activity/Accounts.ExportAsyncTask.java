package com.fsck.k9.activity;


import android.app.ProgressDialog;
import android.net.Uri;
import com.fsck.k9.R;
import com.fsck.k9.activity.misc.ExtendedAsyncTask;
import com.fsck.k9.preferences.SettingsExporter;
import com.fsck.k9.preferences.SettingsImportExportException;
import timber.log.Timber;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
private static class ExportAsyncTask extends ExtendedAsyncTask<Void, Void, Boolean> {
        private boolean mIncludeGlobals;
        private Set<String> mAccountUuids;
        private String mFileName;
        private Uri mUri;


        private ExportAsyncTask(Accounts activity, boolean includeGlobals,
                                List<String> accountUuids, Uri uri) {
            super(activity);
            mIncludeGlobals = includeGlobals;
            mUri = uri;
            if (accountUuids != null) {
                mAccountUuids = new HashSet<>(accountUuids);
            }
        }

        @Override
        protected void showProgressDialog() {
            String title = mContext.getString(R.string.settings_export_dialog_title);
            String message = mContext.getString(R.string.settings_exporting);
            mProgressDialog = ProgressDialog.show(mActivity, title, message, true);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                if (mUri == null) {
                    mFileName = SettingsExporter.exportToFile(mContext, mIncludeGlobals,
                            mAccountUuids);
                } else {
                    SettingsExporter.exportToUri(mContext, mIncludeGlobals, mAccountUuids, mUri);
                }

            } catch (SettingsImportExportException e) {
                Timber.w(e, "Exception during export");
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            Accounts activity = (Accounts) mActivity;

            // Let the activity know that the background task is complete
            activity.setNonConfigurationInstance(null);

            removeProgressDialog();

            if (success) {
                if (mFileName != null) {
                    activity.showSimpleDialog(R.string.settings_export_success_header,
                            R.string.settings_export_success, mFileName);
                } else {
                    activity.showSimpleDialog(R.string.settings_export_success_header,
                            R.string.settings_export_success_generic);
                }
            } else {
                //TODO: better error messages
                activity.showSimpleDialog(R.string.settings_export_failed_header,
                                          R.string.settings_export_failure);
            }
        }
    }