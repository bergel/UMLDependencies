package com.fsck.k9.activity;


import android.content.Context;
import com.fsck.k9.Account;
import com.fsck.k9.Preferences;
import com.fsck.k9.R;
import com.fsck.k9.preferences.SettingsImporter.AccountDescriptionPair;
import com.fsck.k9.preferences.SettingsImporter.ImportResults;

import java.util.ArrayList;
import java.util.List;
private static class AccountsImportedDialog extends SimpleDialog {
        private ImportResults mImportResults;
        private String mFilename;

        AccountsImportedDialog(ImportResults importResults, String filename) {
            super(R.string.settings_import_success_header, R.string.settings_import_success);
            mImportResults = importResults;
            mFilename = filename;
        }

        @Override
        protected String generateMessage(Accounts activity) {
            StringBuilder result = new StringBuilder();
            for (AccountDescriptionPair account : mImportResults.importedAccounts) {
                result.append(activity.getString(R.string.settings_import_account_imported_as,
                        account.original.name, account.imported.name));
                result.append('\n');
            }
            result.append('\n');

            int imported = mImportResults.importedAccounts.size();
            String accounts = activity.getResources().getQuantityString(
                                  R.plurals.settings_import_accounts, imported, imported);
            result.append(activity.getString(R.string.settings_import_success, accounts, mFilename));
            return result.toString();
        }

        @Override
        protected void okayAction(Accounts activity) {
            Context context = activity.getApplicationContext();
            Preferences preferences = Preferences.getPreferences(context);
            List<Account> disabledAccounts = new ArrayList<Account>();
            for (AccountDescriptionPair accountPair : mImportResults.importedAccounts) {
                Account account = preferences.getAccount(accountPair.imported.uuid);
                if (account != null && !account.isEnabled()) {
                    disabledAccounts.add(account);
                }
            }
            if (disabledAccounts.size() > 0) {
                activity.promptForServerPasswords(disabledAccounts);
            } else {
                activity.setNonConfigurationInstance(null);
            }
        }
    }