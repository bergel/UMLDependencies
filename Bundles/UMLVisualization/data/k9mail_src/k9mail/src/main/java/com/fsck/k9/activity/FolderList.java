package com.fsck.k9.activity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import com.fsck.k9.Account;
import com.fsck.k9.Account.FolderMode;
import com.fsck.k9.FontSizes;
import com.fsck.k9.K9;
import com.fsck.k9.Preferences;
import com.fsck.k9.R;
import com.fsck.k9.activity.compose.MessageActions;
import com.fsck.k9.activity.setup.AccountSettings;
import com.fsck.k9.activity.setup.FolderSettings;
import com.fsck.k9.activity.setup.Prefs;
import com.fsck.k9.controller.MessagingController;
import com.fsck.k9.controller.MessagingListener;
import com.fsck.k9.controller.SimpleMessagingListener;
import com.fsck.k9.mail.power.TracingPowerManager;
import com.fsck.k9.mail.power.TracingPowerManager.TracingWakeLock;
import com.fsck.k9.search.LocalSearch;
import com.fsck.k9.service.MailService;
import de.cketti.library.changelog.ChangeLog;
import timber.log.Timber;

import java.util.ArrayList;
import java.util.Collections;

/**
 * FolderList is the primary user interface for the program. This
 * Activity shows list of the Account's folders
 */
public class FolderList extends K9ListActivity {
    private static final String EXTRA_ACCOUNT = "account";

    private static final String EXTRA_FROM_SHORTCUT = "fromShortcut";

    private static final boolean REFRESH_REMOTE = true;

    private ListView mListView;

    private FolderListAdapter mAdapter;

    private LayoutInflater mInflater;

    private Account mAccount;

    private FolderListHandler mHandler = new FolderListHandler();

    private int mUnreadMessageCount;

    private FontSizes mFontSizes = K9.getFontSizes();
    private Context context;

    private MenuItem mRefreshMenuItem;
    private View mActionBarProgressView;
    private ActionBar mActionBar;

    private TextView mActionBarTitle;
    private TextView mActionBarSubTitle;
    private TextView mActionBarUnread;

    

    /**
    * This class is responsible for reloading the list of local messages for a
    * given folder, notifying the adapter that the message have been loaded and
    * queueing up a remote update of the folder.
     */

    private void checkMail(FolderInfoHolder folder) {
        TracingPowerManager pm = TracingPowerManager.getPowerManager(this);
        final TracingWakeLock wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "FolderList checkMail");
        wakeLock.setReferenceCounted(false);
        wakeLock.acquire(K9.WAKE_LOCK_TIMEOUT);
        MessagingListener listener = new SimpleMessagingListener() {
            @Override
            public void synchronizeMailboxFinished(Account account, String folder, int totalMessagesInMailbox, int numNewMessages) {
                if (!account.equals(mAccount)) {
                    return;
                }
                wakeLock.release();
            }

            @Override
            public void synchronizeMailboxFailed(Account account, String folder,
            String message) {
                if (!account.equals(mAccount)) {
                    return;
                }
                wakeLock.release();
            }
        };
        MessagingController.getInstance(getApplication()).synchronizeMailbox(mAccount, folder.name, listener, null);
        sendMail(mAccount);
    }

    public static Intent actionHandleAccountIntent(Context context, Account account, boolean fromShortcut) {
        Intent intent = new Intent(context, FolderList.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(EXTRA_ACCOUNT, account.getUuid());

        if (fromShortcut) {
            intent.putExtra(EXTRA_FROM_SHORTCUT, true);
        }

        return intent;
    }

    public static void actionHandleAccount(Context context, Account account) {
        Intent intent = actionHandleAccountIntent(context, account, false);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (UpgradeDatabases.actionUpgradeDatabases(this, getIntent())) {
            finish();
            return;
        }

        mActionBarProgressView = getActionBarProgressView();
        mActionBar = getActionBar();
        initializeActionBar();
        setContentView(R.layout.folder_list);
        mListView = getListView();
        mListView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mListView.setLongClickable(true);
        mListView.setFastScrollEnabled(true);
        mListView.setScrollingCacheEnabled(false);
        mListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onOpenFolder(((FolderInfoHolder)mAdapter.getItem(position)).name);
            }
        });
        registerForContextMenu(mListView);

        mListView.setSaveEnabled(true);

        mInflater = getLayoutInflater();

        context = this;

        onNewIntent(getIntent());
        if (isFinishing()) {
            /*
             * onNewIntent() may call finish(), but execution will still continue here.
             * We return now because we don't want to display the changelog which can
             * result in a leaked window error.
             */
            return;
        }

        ChangeLog cl = new ChangeLog(this);
        if (cl.isFirstRun()) {
            cl.getLogDialog().show();
        }
    }

    @SuppressLint("InflateParams")
    private View getActionBarProgressView() {
        return getLayoutInflater().inflate(R.layout.actionbar_indeterminate_progress_actionview, null);
    }

    private void initializeActionBar() {
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setCustomView(R.layout.actionbar_custom);

        View customView = mActionBar.getCustomView();
        mActionBarTitle = (TextView) customView.findViewById(R.id.actionbar_title_first);
        mActionBarSubTitle = (TextView) customView.findViewById(R.id.actionbar_title_sub);
        mActionBarUnread = (TextView) customView.findViewById(R.id.actionbar_unread_count);

        mActionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onNewIntent(Intent intent) {
        setIntent(intent); // onNewIntent doesn't autoset our "internal" intent

        mUnreadMessageCount = 0;
        String accountUuid = intent.getStringExtra(EXTRA_ACCOUNT);
        mAccount = Preferences.getPreferences(this).getAccount(accountUuid);

        if (mAccount == null) {
            /*
             * This can happen when a launcher shortcut is created for an
             * account, and then the account is deleted or data is wiped, and
             * then the shortcut is used.
             */
            finish();
            return;
        }

        if (intent.getBooleanExtra(EXTRA_FROM_SHORTCUT, false) &&
                   !K9.FOLDER_NONE.equals(mAccount.getAutoExpandFolderName())) {
            onOpenFolder(mAccount.getAutoExpandFolderName());
            finish();
        } else {
            initializeActivityView();
        }
    }

    private void initializeActivityView() {
        mAdapter = new FolderListAdapter();
        restorePreviousData();

        setListAdapter(mAdapter);
        getListView().setTextFilterEnabled(mAdapter.getFilter() != null); // should never be false but better safe then sorry
    }

    @SuppressWarnings("unchecked")
    private void restorePreviousData() {
        final Object previousData = getLastNonConfigurationInstance();

        if (previousData != null) {
            mAdapter.mFolders = (ArrayList<FolderInfoHolder>) previousData;
            mAdapter.mFilteredFolders = Collections.unmodifiableList(mAdapter.mFolders);
        }
    }


    @Override public Object onRetainNonConfigurationInstance() {
        return (mAdapter == null) ? null : mAdapter.mFolders;
    }

    @Override public void onPause() {
        super.onPause();
        MessagingController.getInstance(getApplication()).removeListener(mAdapter.mListener);
        mAdapter.mListener.onPause(this);
    }

    /**
    * On resume we refresh the folder list (in the background) and we refresh the
    * messages for any folder that is currently open. This guarantees that things
    * like unread message count and read status are updated.
     */
    @Override public void onResume() {
        super.onResume();

        if (!mAccount.isAvailable(this)) {
            Timber.i("account unavaliabale, not showing folder-list but account-list");
            Accounts.listAccounts(this);
            finish();
            return;
        }
        if (mAdapter == null)
            initializeActivityView();

        mHandler.refreshTitle();

        MessagingController.getInstance(getApplication()).addListener(mAdapter.mListener);
        //mAccount.refresh(Preferences.getPreferences(this));
        MessagingController.getInstance(getApplication()).getAccountStats(this, mAccount, mAdapter.mListener);

        onRefresh(!REFRESH_REMOTE);

        MessagingController.getInstance(getApplication()).cancelNotificationsForAccount(mAccount);
        mAdapter.mListener.onResume(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //Shortcuts that work no matter what is selected
        switch (keyCode) {
        case KeyEvent.KEYCODE_Q: {
            onAccounts();
            return true;
        }

        case KeyEvent.KEYCODE_S: {
            onEditAccount();
            return true;
        }

        case KeyEvent.KEYCODE_H: {
            Toast toast = Toast.makeText(this, R.string.folder_list_help_key, Toast.LENGTH_LONG);
            toast.show();
            return true;
        }

        case KeyEvent.KEYCODE_1: {
            setDisplayMode(FolderMode.FIRST_CLASS);
            return true;
        }
        case KeyEvent.KEYCODE_2: {
            setDisplayMode(FolderMode.FIRST_AND_SECOND_CLASS);
            return true;
        }
        case KeyEvent.KEYCODE_3: {
            setDisplayMode(FolderMode.NOT_SECOND_CLASS);
            return true;
        }
        case KeyEvent.KEYCODE_4: {
            setDisplayMode(FolderMode.ALL);
            return true;
        }
        }//switch


        return super.onKeyDown(keyCode, event);
    }//onKeyDown

    private void setDisplayMode(FolderMode newMode) {
        mAccount.setFolderDisplayMode(newMode);
        mAccount.save(Preferences.getPreferences(this));
        if (mAccount.getFolderPushMode() != FolderMode.NONE) {
            MailService.actionRestartPushers(this, null);
        }
        mAdapter.getFilter().filter(null);
        onRefresh(false);
    }


    private void onRefresh(final boolean forceRemote) {

        MessagingController.getInstance(getApplication()).listFolders(mAccount, forceRemote, mAdapter.mListener);

    }

    private void onEditPrefs() {
        Prefs.actionPrefs(this);
    }
    private void onEditAccount() {
        AccountSettings.actionSettings(this, mAccount);
    }

    private void onAccounts() {
        Accounts.listAccounts(this);
        finish();
    }

    private void onEmptyTrash(final Account account) {
        mHandler.dataChanged();

        MessagingController.getInstance(getApplication()).emptyTrash(account, null);
    }

    private void onClearFolder(Account account, String folderName) {
        MessagingController.getInstance(getApplication()).clearFolder(account, folderName, mAdapter.mListener);
    }

    private void sendMail(Account account) {
        MessagingController.getInstance(getApplication()).sendPendingMessages(account, mAdapter.mListener);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            onAccounts();

            return true;

        case R.id.search:
            onSearchRequested();

            return true;

        case R.id.compose:
            MessageActions.actionCompose(this, mAccount);

            return true;

        case R.id.check_mail:
            MessagingController.getInstance(getApplication()).checkMail(this, mAccount, true, true, mAdapter.mListener);

            return true;

        case R.id.send_messages:
            MessagingController.getInstance(getApplication()).sendPendingMessages(mAccount, null);

            return true;

        case R.id.list_folders:
            onRefresh(REFRESH_REMOTE);

            return true;

        case R.id.account_settings:
            onEditAccount();

            return true;

        case R.id.app_settings:
            onEditPrefs();

            return true;

        case R.id.empty_trash:
            onEmptyTrash(mAccount);

            return true;

        case R.id.compact:
            onCompact(mAccount);

            return true;

        case R.id.display_1st_class: {
            setDisplayMode(FolderMode.FIRST_CLASS);
            return true;
        }
        case R.id.display_1st_and_2nd_class: {
            setDisplayMode(FolderMode.FIRST_AND_SECOND_CLASS);
            return true;
        }
        case R.id.display_not_second_class: {
            setDisplayMode(FolderMode.NOT_SECOND_CLASS);
            return true;
        }
        case R.id.display_all: {
            setDisplayMode(FolderMode.ALL);
            return true;
        }
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSearchRequested() {
         Bundle appData = new Bundle();
         appData.putString(MessageList.EXTRA_SEARCH_ACCOUNT, mAccount.getUuid());
         startSearch(null, false, appData, false);
         return true;
     }

    private void onOpenFolder(String folder) {
        LocalSearch search = new LocalSearch(folder);
        search.addAccountUuid(mAccount.getUuid());
        search.addAllowedFolder(folder);
        MessageList.actionDisplaySearch(this, search, false, false);
    }

    private void onCompact(Account account) {
        mHandler.workingAccount(R.string.compacting_account);
        MessagingController.getInstance(getApplication()).compact(account, null);
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.folder_list_option, menu);
        mRefreshMenuItem = menu.findItem(R.id.check_mail);
        configureFolderSearchView(menu);
        return true;
    }

    private void configureFolderSearchView(Menu menu) {
        final MenuItem folderMenuItem = menu.findItem(R.id.filter_folders);
        final SearchView folderSearchView = (SearchView) folderMenuItem.getActionView();
        folderSearchView.setQueryHint(getString(R.string.folder_list_filter_hint));
        folderSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                folderMenuItem.collapseActionView();
                mActionBarTitle.setText(getString(R.string.filter_folders_action));
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return true;
            }
        });

        folderSearchView.setOnCloseListener(new SearchView.OnCloseListener() {

            @Override
            public boolean onClose() {
                mActionBarTitle.setText(getString(R.string.folders_title));
                return false;
            }
        });
    }

    @Override public boolean onContextItemSelected(android.view.MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item .getMenuInfo();
        FolderInfoHolder folder = (FolderInfoHolder) mAdapter.getItem(info.position);

        switch (item.getItemId()) {
        case R.id.clear_local_folder:
            onClearFolder(mAccount, folder.name);
            break;
        case R.id.refresh_folder:
            checkMail(folder);
            break;
        case R.id.folder_settings:
            FolderSettings.actionSettings(this, mAccount, folder.name);
            break;
        }

        return super.onContextItemSelected(item);
    }

    @Override public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
        getMenuInflater().inflate(R.menu.folder_context, menu);

        FolderInfoHolder folder = (FolderInfoHolder) mAdapter.getItem(info.position);

        menu.setHeaderTitle(folder.displayName);
    }

    

    

    
}