package com.fsck.k9.fragment;

import android.app.Activity;
import android.view.Window;
import android.widget.Toast;
import com.fsck.k9.Account;
import com.fsck.k9.R;
import com.fsck.k9.activity.ActivityListener;
import com.fsck.k9.helper.Utility;
import com.fsck.k9.mail.Message;

import java.util.List;
class MessageListActivityListener extends ActivityListener {
        @Override
        public void remoteSearchFailed(String folder, final String err) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Activity activity = getActivity();
                    if (activity != null) {
                        Toast.makeText(activity, R.string.remote_search_error,
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        @Override
        public void remoteSearchStarted(String folder) {
            handler.progress(true);
            handler.updateFooter(context.getString(R.string.remote_search_sending_query));
        }

        @Override
        public void enableProgressIndicator(boolean enable) {
            handler.progress(enable);
        }

        @Override
        public void remoteSearchFinished(String folder, int numResults, int maxResults, List<Message> extraResults) {
            handler.progress(false);
            handler.remoteSearchFinished();
            extraSearchResults = extraResults;
            if (extraResults != null && extraResults.size() > 0) {
                handler.updateFooter(String.format(context.getString(R.string.load_more_messages_fmt), maxResults));
            } else {
                handler.updateFooter(null);
            }
            fragmentListener.setMessageListProgress(Window.PROGRESS_END);

        }

        @Override
        public void remoteSearchServerQueryComplete(String folderName, int numResults, int maxResults) {
            handler.progress(true);
            if (maxResults != 0 && numResults > maxResults) {
                handler.updateFooter(context.getResources().getQuantityString(R.plurals.remote_search_downloading_limited,
                        maxResults, maxResults, numResults));
            } else {
                handler.updateFooter(context.getResources().getQuantityString(R.plurals.remote_search_downloading, numResults));
            }
            fragmentListener.setMessageListProgress(Window.PROGRESS_START);
        }

        @Override
        public void informUserOfStatus() {
            handler.refreshTitle();
        }

        @Override
        public void synchronizeMailboxStarted(Account account, String folder) {
            if (updateForMe(account, folder)) {
                handler.progress(true);
                handler.folderLoading(folder, true);
            }
            super.synchronizeMailboxStarted(account, folder);
        }

        @Override
        public void synchronizeMailboxFinished(Account account, String folder,
        int totalMessagesInMailbox, int numNewMessages) {

            if (updateForMe(account, folder)) {
                handler.progress(false);
                handler.folderLoading(folder, false);
            }
            super.synchronizeMailboxFinished(account, folder, totalMessagesInMailbox, numNewMessages);
        }

        @Override
        public void synchronizeMailboxFailed(Account account, String folder, String message) {

            if (updateForMe(account, folder)) {
                handler.progress(false);
                handler.folderLoading(folder, false);
            }
            super.synchronizeMailboxFailed(account, folder, message);
        }

        @Override
        public void folderStatusChanged(Account account, String folder, int unreadMessageCount) {
            if (isSingleAccountMode() && isSingleFolderMode() && MessageListFragment.this.account.equals(account) &&
                    folderName.equals(folder)) {
                MessageListFragment.this.unreadMessageCount = unreadMessageCount;
            }
            super.folderStatusChanged(account, folder, unreadMessageCount);
        }

        private boolean updateForMe(Account account, String folder) {
            if (account == null || folder == null) {
                return false;
            }

            if (!Utility.arrayContains(accountUuids, account.getUuid())) {
                return false;
            }

            List<String> folderNames = search.getFolderNames();
            return (folderNames.isEmpty() || folderNames.contains(folder));
        }
    }