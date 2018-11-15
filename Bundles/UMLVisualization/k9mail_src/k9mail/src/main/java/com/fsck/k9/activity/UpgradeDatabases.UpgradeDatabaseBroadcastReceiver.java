package com.fsck.k9.activity;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import com.fsck.k9.Account;
import com.fsck.k9.K9;
import com.fsck.k9.R;
import com.fsck.k9.controller.MessagingController;
import com.fsck.k9.mailstore.LocalStore;
import com.fsck.k9.service.DatabaseUpgradeService;


/**
 * This activity triggers a database upgrade if necessary and displays the current upgrade progress.
 *
 * <p>
 * The current upgrade process works as follows:
 * <ol>
 * <li>Activities that access an account's database call
 *     {@link #actionUpgradeDatabases(Context, Intent)} in their {@link Activity#onCreate(Bundle)}
 *     method.</li>
 * <li>{@link #actionUpgradeDatabases(Context, Intent)} will call {@link K9#areDatabasesUpToDate()}
 *     to check if we already know whether the databases have been upgraded.</li>
 * <li>{@link K9#areDatabasesUpToDate()} will compare the last known database version stored in a
 *     {@link SharedPreferences} file to {@link com.fsck.k9.mailstore.LocalStore#DB_VERSION}. This
 *     is done as an optimization because it's faster than opening all of the accounts' databases
 *     one by one.</li>
 * <li>If there was an error reading the cached database version or if it shows the databases need
 *     upgrading this activity ({@code UpgradeDatabases}) is started.</li>
 * <li>This activity will display a spinning progress indicator and start
 *     {@link DatabaseUpgradeService}.</li>
 * <li>{@link DatabaseUpgradeService} will acquire a partial wake lock (with a 10 minute timeout),
 *     start a background thread to perform the database upgrades, and report the progress using
 *     {@link LocalBroadcastManager} to this activity which will update the UI accordingly.</li>
 * <li>Once the upgrade is complete {@link DatabaseUpgradeService} will notify this activity,
 *     release the wake lock, and stop itself.</li>
 * <li>This activity will start the original activity using the intent supplied when calling
 *     {@link #actionUpgradeDatabases(Context, Intent)}.</li>
 * </ol>
 * </p><p>
 * Currently we make no attempts to stop the background code (e.g. {@link MessagingController}) from
 * opening the accounts' databases. If this happens the upgrade is performed in one of the
 * background threads and not by {@link DatabaseUpgradeService}. But this is not a problem. Due to
 * the locking in {@link LocalStore#getInstance(Account, Context)} the upgrade
 * service will block in the {@link Account#getLocalStore()} call and from the outside (especially
 * for this activity) it will appear as if {@link DatabaseUpgradeService} is performing the upgrade.
 * </p>
 */
class UpgradeDatabaseBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, final Intent intent) {
            String action = intent.getAction();

            if (DatabaseUpgradeService.ACTION_UPGRADE_PROGRESS.equals(action)) {
                /*
                 * Information on the current upgrade progress
                 */

                String accountUuid = intent.getStringExtra(
                        DatabaseUpgradeService.EXTRA_ACCOUNT_UUID);

                Account account = mPreferences.getAccount(accountUuid);

                if (account != null) {
                    String formatString = getString(R.string.upgrade_database_format);
                    String upgradeStatus = String.format(formatString, account.getDescription());
                    mUpgradeText.setText(upgradeStatus);
                }

            } else if (DatabaseUpgradeService.ACTION_UPGRADE_COMPLETE.equals(action)) {
                /*
                 * Upgrade complete
                 */

                launchOriginalActivity();
            }
        }
    }