package com.fsck.k9.fragment;

import android.database.Cursor;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.fsck.k9.Account;
import com.fsck.k9.R;
import com.fsck.k9.activity.MessageReference;
import com.fsck.k9.mail.Flag;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.fsck.k9.fragment.MLFProjectionInfo.ACCOUNT_UUID_COLUMN;
class ActionModeCallback implements ActionMode.Callback {
        private MenuItem mSelectAll;
        private MenuItem mMarkAsRead;
        private MenuItem mMarkAsUnread;
        private MenuItem mFlag;
        private MenuItem mUnflag;

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            mSelectAll = menu.findItem(R.id.select_all);
            mMarkAsRead = menu.findItem(R.id.mark_as_read);
            mMarkAsUnread = menu.findItem(R.id.mark_as_unread);
            mFlag = menu.findItem(R.id.flag);
            mUnflag = menu.findItem(R.id.unflag);

            // we don't support cross account actions atm
            if (!singleAccountMode) {
                // show all
                menu.findItem(R.id.move).setVisible(true);
                menu.findItem(R.id.archive).setVisible(true);
                menu.findItem(R.id.spam).setVisible(true);
                menu.findItem(R.id.copy).setVisible(true);

                Set<String> accountUuids = getAccountUuidsForSelected();

                for (String accountUuid : accountUuids) {
                    Account account = preferences.getAccount(accountUuid);
                    if (account != null) {
                        setContextCapabilities(account, menu);
                    }
                }

            }
            return true;
        }

        /**
         * Get the set of account UUIDs for the selected messages.
         */
        private Set<String> getAccountUuidsForSelected() {
            int maxAccounts = accountUuids.length;
            Set<String> accountUuids = new HashSet<>(maxAccounts);

            for (int position = 0, end = adapter.getCount(); position < end; position++) {
                Cursor cursor = (Cursor) adapter.getItem(position);
                long uniqueId = cursor.getLong(uniqueIdColumn);

                if (selected.contains(uniqueId)) {
                    String accountUuid = cursor.getString(ACCOUNT_UUID_COLUMN);
                    accountUuids.add(accountUuid);

                    if (accountUuids.size() == MessageListFragment.this.accountUuids.length) {
                        break;
                    }
                }
            }

            return accountUuids;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            actionMode = null;
            mSelectAll = null;
            mMarkAsRead = null;
            mMarkAsUnread = null;
            mFlag = null;
            mUnflag = null;
            setSelectionState(false);
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.message_list_context, menu);

            // check capabilities
            setContextCapabilities(account, menu);

            return true;
        }

        /**
         * Disables menu options not supported by the account type or current "search view".
         *
         * @param account
         *         The account to query for its capabilities.
         * @param menu
         *         The menu to adapt.
         */
        private void setContextCapabilities(Account account, Menu menu) {
            if (!singleAccountMode) {
                // We don't support cross-account copy/move operations right now
                menu.findItem(R.id.move).setVisible(false);
                menu.findItem(R.id.copy).setVisible(false);

                //TODO: we could support the archive and spam operations if all selected messages
                // belong to non-POP3 accounts
                menu.findItem(R.id.archive).setVisible(false);
                menu.findItem(R.id.spam).setVisible(false);

            } else {
                // hide unsupported
                if (!messagingController.isCopyCapable(account)) {
                    menu.findItem(R.id.copy).setVisible(false);
                }

                if (!messagingController.isMoveCapable(account)) {
                    menu.findItem(R.id.move).setVisible(false);
                    menu.findItem(R.id.archive).setVisible(false);
                    menu.findItem(R.id.spam).setVisible(false);
                }

                if (!account.hasArchiveFolder()) {
                    menu.findItem(R.id.archive).setVisible(false);
                }

                if (!account.hasSpamFolder()) {
                    menu.findItem(R.id.spam).setVisible(false);
                }
            }
        }

        public void showSelectAll(boolean show) {
            if (actionMode != null) {
                mSelectAll.setVisible(show);
            }
        }

        public void showMarkAsRead(boolean show) {
            if (actionMode != null) {
                mMarkAsRead.setVisible(show);
                mMarkAsUnread.setVisible(!show);
            }
        }

        public void showFlag(boolean show) {
            if (actionMode != null) {
                mFlag.setVisible(show);
                mUnflag.setVisible(!show);
            }
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            /*
             * In the following we assume that we can't move or copy
             * mails to the same folder. Also that spam isn't available if we are
             * in the spam folder,same for archive.
             *
             * This is the case currently so safe assumption.
             */
            switch (item.getItemId()) {
            case R.id.delete: {
                List<MessageReference> messages = getCheckedMessages();
                onDelete(messages);
                selectedCount = 0;
                break;
            }
            case R.id.mark_as_read: {
                setFlagForSelected(Flag.SEEN, true);
                break;
            }
            case R.id.mark_as_unread: {
                setFlagForSelected(Flag.SEEN, false);
                break;
            }
            case R.id.flag: {
                setFlagForSelected(Flag.FLAGGED, true);
                break;
            }
            case R.id.unflag: {
                setFlagForSelected(Flag.FLAGGED, false);
                break;
            }
            case R.id.select_all: {
                selectAll();
                break;
            }

            // only if the account supports this
            case R.id.archive: {
                onArchive(getCheckedMessages());
                selectedCount = 0;
                break;
            }
            case R.id.spam: {
                onSpam(getCheckedMessages());
                selectedCount = 0;
                break;
            }
            case R.id.move: {
                onMove(getCheckedMessages());
                selectedCount = 0;
                break;
            }
            case R.id.copy: {
                onCopy(getCheckedMessages());
                selectedCount = 0;
                break;
            }
            }
            if (selectedCount == 0) {
                actionMode.finish();
            }

            return true;
        }
    }