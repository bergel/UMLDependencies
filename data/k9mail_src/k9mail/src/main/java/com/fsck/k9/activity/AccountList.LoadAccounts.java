package com.fsck.k9.activity;

import android.os.AsyncTask;
import com.fsck.k9.Account;
import com.fsck.k9.BaseAccount;
import com.fsck.k9.Preferences;

import java.util.List;


/**
 * Activity displaying the list of accounts.
 *
 * <p>
 * Classes extending this abstract class have to provide an {@link #onAccountSelected(BaseAccount)}
 * method to perform an action when an account is selected.
 * </p>
 */
class LoadAccounts extends AsyncTask<Void, Void, List<Account>> {
        @Override
        protected List<Account> doInBackground(Void... params) {
            List<Account> accounts = Preferences.getPreferences(getApplicationContext()).getAccounts();
            return accounts;
        }

        @Override
        protected void onPostExecute(List<Account> accounts) {
            populateListView(accounts);
        }
    }