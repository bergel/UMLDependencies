package com.fsck.k9.activity;

import android.text.TextUtils.TruncateAt;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.fsck.k9.Account;
import com.fsck.k9.Account.FolderMode;
import com.fsck.k9.AccountStats;
import com.fsck.k9.BaseAccount;
import com.fsck.k9.K9;
import com.fsck.k9.R;
import com.fsck.k9.controller.MessagingController;
import com.fsck.k9.mail.Folder;
import com.fsck.k9.mail.Message;
import com.fsck.k9.mailstore.LocalFolder;
import com.fsck.k9.search.LocalSearch;
import com.fsck.k9.search.SearchSpecification.Attribute;
import com.fsck.k9.search.SearchSpecification.SearchField;
import timber.log.Timber;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * FolderList is the primary user interface for the program. This
 * Activity shows list of the Account's folders
 */
class FolderListAdapter extends BaseAdapter implements Filterable {
        private List<FolderInfoHolder> mFolders = new ArrayList<FolderInfoHolder>();
        private List<FolderInfoHolder> mFilteredFolders = Collections.unmodifiableList(mFolders);
        private Filter mFilter = new FolderListFilter();

        public Object getItem(long position) {
            return getItem((int)position);
        }

        public Object getItem(int position) {
            return mFilteredFolders.get(position);
        }


        public long getItemId(int position) {
            return mFilteredFolders.get(position).folder.getName().hashCode() ;
        }

        public int getCount() {
            return mFilteredFolders.size();
        }

        @Override
        public boolean isEnabled(int item) {
            return true;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return true;
        }

        private ActivityListener mListener = new ActivityListener() {
            @Override
            public void informUserOfStatus() {
                mHandler.refreshTitle();
                mHandler.dataChanged();
            }
            @Override
            public void accountStatusChanged(BaseAccount account, AccountStats stats) {
                if (!account.equals(mAccount)) {
                    return;
                }
                if (stats == null) {
                    return;
                }
                mUnreadMessageCount = stats.unreadMessageCount;
                mHandler.refreshTitle();
            }

            @Override
            public void listFoldersStarted(Account account) {
                if (account.equals(mAccount)) {
                    mHandler.progress(true);
                }
                super.listFoldersStarted(account);

            }

            @Override
            public void listFoldersFailed(Account account, String message) {
                if (account.equals(mAccount)) {
                    mHandler.progress(false);
                    Toast.makeText(context, R.string.fetching_folders_failed, Toast.LENGTH_SHORT).show();
                }
                super.listFoldersFailed(account, message);
            }

            @Override
            public void listFoldersFinished(Account account) {
                if (account.equals(mAccount)) {

                    mHandler.progress(false);
                    MessagingController.getInstance(getApplication()).refreshListener(mAdapter.mListener);
                    mHandler.dataChanged();
                }
                super.listFoldersFinished(account);

            }

            @Override
            public void listFolders(Account account, List<LocalFolder> folders) {
                if (account.equals(mAccount)) {

                    List<FolderInfoHolder> newFolders = new LinkedList<FolderInfoHolder>();
                    List<FolderInfoHolder> topFolders = new LinkedList<FolderInfoHolder>();

                    Account.FolderMode aMode = account.getFolderDisplayMode();
                    for (LocalFolder folder : folders) {
                        Folder.FolderClass fMode = folder.getDisplayClass();

                        if ((aMode == FolderMode.FIRST_CLASS && fMode != Folder.FolderClass.FIRST_CLASS)
                                || (aMode == FolderMode.FIRST_AND_SECOND_CLASS &&
                                    fMode != Folder.FolderClass.FIRST_CLASS &&
                                    fMode != Folder.FolderClass.SECOND_CLASS)
                        || (aMode == FolderMode.NOT_SECOND_CLASS && fMode == Folder.FolderClass.SECOND_CLASS)) {
                            continue;
                        }

                        FolderInfoHolder holder = null;

                        int folderIndex = getFolderIndex(folder.getName());
                        if (folderIndex >= 0) {
                            holder = (FolderInfoHolder) getItem(folderIndex);
                        }

                        if (holder == null) {
                            holder = new FolderInfoHolder(context, folder, mAccount, -1);
                        } else {
                            holder.populate(context, folder, mAccount, -1);

                        }
                        if (folder.isInTopGroup()) {
                            topFolders.add(holder);
                        } else {
                            newFolders.add(holder);
                        }
                    }
                    Collections.sort(newFolders);
                    Collections.sort(topFolders);
                    topFolders.addAll(newFolders);
                    mHandler.newFolders(topFolders);
                }
                super.listFolders(account, folders);
            }

            @Override
            public void synchronizeMailboxStarted(Account account, String folder) {
                super.synchronizeMailboxStarted(account, folder);
                if (account.equals(mAccount)) {

                    mHandler.progress(true);
                    mHandler.folderLoading(folder, true);
                    mHandler.dataChanged();
                }

            }

            @Override
            public void synchronizeMailboxFinished(Account account, String folder, int totalMessagesInMailbox, int numNewMessages) {
                super.synchronizeMailboxFinished(account, folder, totalMessagesInMailbox, numNewMessages);
                if (account.equals(mAccount)) {
                    mHandler.progress(false);
                    mHandler.folderLoading(folder, false);

                    refreshFolder(account, folder);
                }

            }

            private void refreshFolder(Account account, String folderName) {
                // There has to be a cheaper way to get at the localFolder object than this
                LocalFolder localFolder = null;
                try {
                    if (account != null && folderName != null) {
                        if (!account.isAvailable(FolderList.this)) {
                            Timber.i("not refreshing folder of unavailable account");
                            return;
                        }
                        localFolder = account.getLocalStore().getFolder(folderName);
                        FolderInfoHolder folderHolder = getFolder(folderName);
                        if (folderHolder != null) {
                            folderHolder.populate(context, localFolder, mAccount, -1);
                            folderHolder.flaggedMessageCount = -1;

                            mHandler.dataChanged();
                        }
                    }
                } catch (Exception e) {
                    Timber.e(e, "Exception while populating folder");
                } finally {
                    if (localFolder != null) {
                        localFolder.close();
                    }
                }

            }

            @Override
            public void synchronizeMailboxFailed(Account account, String folder, String message) {
                super.synchronizeMailboxFailed(account, folder, message);
                if (!account.equals(mAccount)) {
                    return;
                }


                mHandler.progress(false);

                mHandler.folderLoading(folder, false);

                //   String mess = truncateStatus(message);

                //   mHandler.folderStatus(folder, mess);
                FolderInfoHolder holder = getFolder(folder);

                if (holder != null) {
                    holder.lastChecked = 0;
                }

                mHandler.dataChanged();

            }

            @Override
            public void setPushActive(Account account, String folderName, boolean enabled) {
                if (!account.equals(mAccount)) {
                    return;
                }
                FolderInfoHolder holder = getFolder(folderName);

                if (holder != null) {
                    holder.pushActive = enabled;

                    mHandler.dataChanged();
                }
            }


            @Override
            public void messageDeleted(Account account, String folder, Message message) {
                synchronizeMailboxRemovedMessage(account, folder, message);
            }

            @Override
            public void emptyTrashCompleted(Account account) {
                if (account.equals(mAccount)) {
                    refreshFolder(account, mAccount.getTrashFolderName());
                }
            }

            @Override
            public void folderStatusChanged(Account account, String folderName, int unreadMessageCount) {
                if (account.equals(mAccount)) {
                    refreshFolder(account, folderName);
                    informUserOfStatus();
                }
            }

            @Override
            public void sendPendingMessagesCompleted(Account account) {
                super.sendPendingMessagesCompleted(account);
                if (account.equals(mAccount)) {
                    refreshFolder(account, mAccount.getOutboxFolderName());
                }
            }

            @Override
            public void sendPendingMessagesStarted(Account account) {
                super.sendPendingMessagesStarted(account);

                if (account.equals(mAccount)) {
                    mHandler.dataChanged();
                }
            }

            @Override
            public void sendPendingMessagesFailed(Account account) {
                super.sendPendingMessagesFailed(account);
                if (account.equals(mAccount)) {
                    refreshFolder(account, mAccount.getOutboxFolderName());
                }
            }

            @Override
            public void accountSizeChanged(Account account, long oldSize, long newSize) {
                if (account.equals(mAccount)) {
                    mHandler.accountSizeChanged(oldSize, newSize);
                }
            }
        };


        public int getFolderIndex(String folder) {
            FolderInfoHolder searchHolder = new FolderInfoHolder();
            searchHolder.name = folder;
            return   mFilteredFolders.indexOf(searchHolder);
        }

        public FolderInfoHolder getFolder(String folder) {
            FolderInfoHolder holder = null;

            int index = getFolderIndex(folder);
            if (index >= 0) {
                holder = (FolderInfoHolder) getItem(index);
                if (holder != null) {
                    return holder;
                }
            }
            return null;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if (position <= getCount()) {
                return  getItemView(position, convertView, parent);
            } else {
                Timber.e("getView with illegal position=%d called! count is only %d", position, getCount());
                return null;
            }
        }

        public View getItemView(int itemPosition, View convertView, ViewGroup parent) {
            FolderInfoHolder folder = (FolderInfoHolder) getItem(itemPosition);
            View view;
            if (convertView != null) {
                view = convertView;
            } else {
                view = mInflater.inflate(R.layout.folder_list_item, parent, false);
            }

            FolderViewHolder holder = (FolderViewHolder) view.getTag();

            if (holder == null) {
                holder = new FolderViewHolder();
                holder.folderName = (TextView) view.findViewById(R.id.folder_name);
                holder.newMessageCount = (TextView) view.findViewById(R.id.new_message_count);
                holder.flaggedMessageCount = (TextView) view.findViewById(R.id.flagged_message_count);
                holder.newMessageCountWrapper = view.findViewById(R.id.new_message_count_wrapper);
                holder.flaggedMessageCountWrapper = view.findViewById(R.id.flagged_message_count_wrapper);
                holder.newMessageCountIcon = view.findViewById(R.id.new_message_count_icon);
                holder.flaggedMessageCountIcon = view.findViewById(R.id.flagged_message_count_icon);

                holder.folderStatus = (TextView) view.findViewById(R.id.folder_status);
                holder.activeIcons = (RelativeLayout) view.findViewById(R.id.active_icons);
                holder.chip = view.findViewById(R.id.chip);
                holder.folderListItemLayout = (LinearLayout)view.findViewById(R.id.folder_list_item_layout);
                holder.rawFolderName = folder.name;

                view.setTag(holder);
            }

            if (folder == null) {
                return view;
            }

            final String folderStatus;

            if (folder.loading) {
                folderStatus = getString(R.string.status_loading);
            } else if (folder.status != null) {
                folderStatus = folder.status;
            } else if (folder.lastChecked != 0) {
                long now = System.currentTimeMillis();
                int flags = DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR;
                CharSequence formattedDate;

                if (Math.abs(now - folder.lastChecked) > DateUtils.WEEK_IN_MILLIS) {
                    formattedDate = getString(R.string.preposition_for_date,
                            DateUtils.formatDateTime(context, folder.lastChecked, flags));
                } else {
                    formattedDate = DateUtils.getRelativeTimeSpanString(folder.lastChecked,
                            now, DateUtils.MINUTE_IN_MILLIS, flags);
                }

                folderStatus = getString(folder.pushActive
                        ? R.string.last_refresh_time_format_with_push
                        : R.string.last_refresh_time_format,
                        formattedDate);
            } else {
                folderStatus = null;
            }

            holder.folderName.setText(folder.displayName);
            if (folderStatus != null) {
                holder.folderStatus.setText(folderStatus);
                holder.folderStatus.setVisibility(View.VISIBLE);
            } else {
                holder.folderStatus.setVisibility(View.GONE);
            }

            if(folder.unreadMessageCount == -1) {
               folder.unreadMessageCount = 0;
                try {
                    folder.unreadMessageCount  = folder.folder.getUnreadMessageCount();
                } catch (Exception e) {
                    Timber.e("Unable to get unreadMessageCount for %s:%s", mAccount.getDescription(), folder.name);
                }
            }
            if (folder.unreadMessageCount > 0) {
                holder.newMessageCount.setText(String.format("%d", folder.unreadMessageCount));
                holder.newMessageCountWrapper.setOnClickListener(
                        createUnreadSearch(mAccount, folder));
                holder.newMessageCountWrapper.setVisibility(View.VISIBLE);
                holder.newMessageCountIcon.setBackgroundDrawable(
                        mAccount.generateColorChip(false, false).drawable());
            } else {
                holder.newMessageCountWrapper.setVisibility(View.GONE);
            }

            if (folder.flaggedMessageCount == -1) {
                folder.flaggedMessageCount = 0;
                try {
                    folder.flaggedMessageCount = folder.folder.getFlaggedMessageCount();
                } catch (Exception e) {
                    Timber.e("Unable to get flaggedMessageCount for %s:%s", mAccount.getDescription(), folder.name);
                }
            }

            if (K9.messageListStars() && folder.flaggedMessageCount > 0) {
                holder.flaggedMessageCount.setText(String.format("%d", folder.flaggedMessageCount));
                holder.flaggedMessageCountWrapper.setOnClickListener(
                        createFlaggedSearch(mAccount, folder));
                holder.flaggedMessageCountWrapper.setVisibility(View.VISIBLE);
                holder.flaggedMessageCountIcon.setBackgroundDrawable(
                        mAccount.generateColorChip(false, true).drawable());
            } else {
                holder.flaggedMessageCountWrapper.setVisibility(View.GONE);
            }

            holder.activeIcons.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    Toast toast = Toast.makeText(getApplication(), getString(R.string.tap_hint), Toast.LENGTH_SHORT);
                    toast.show();
                }
            });

            holder.chip.setBackgroundColor(mAccount.getChipColor());


            mFontSizes.setViewTextSize(holder.folderName, mFontSizes.getFolderName());

            if (K9.wrapFolderNames()) {
                holder.folderName.setEllipsize(null);
                holder.folderName.setSingleLine(false);
            }
            else {
                holder.folderName.setEllipsize(TruncateAt.START);
                holder.folderName.setSingleLine(true);
            }
            mFontSizes.setViewTextSize(holder.folderStatus, mFontSizes.getFolderStatus());

            return view;
        }

        private OnClickListener createFlaggedSearch(Account account, FolderInfoHolder folder) {
            String searchTitle = getString(R.string.search_title,
                    getString(R.string.message_list_title, account.getDescription(),
                            folder.displayName),
                    getString(R.string.flagged_modifier));

            LocalSearch search = new LocalSearch(searchTitle);
            search.and(SearchField.FLAGGED, "1", Attribute.EQUALS);

            search.addAllowedFolder(folder.name);
            search.addAccountUuid(account.getUuid());

            return new FolderClickListener(search);
        }

        private OnClickListener createUnreadSearch(Account account, FolderInfoHolder folder) {
            String searchTitle = getString(R.string.search_title,
                    getString(R.string.message_list_title, account.getDescription(),
                            folder.displayName),
                    getString(R.string.unread_modifier));

            LocalSearch search = new LocalSearch(searchTitle);
            search.and(SearchField.READ, "1", Attribute.NOT_EQUALS);

            search.addAllowedFolder(folder.name);
            search.addAccountUuid(account.getUuid());

            return new FolderClickListener(search);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        public boolean isItemSelectable(int position) {
            return true;
        }

        public void setFilter(final Filter filter) {
            this.mFilter = filter;
        }

        public Filter getFilter() {
            return mFilter;
        }

        /**
         * Filter to search for occurrences of the search-expression in any place of the
         * folder-name instead of doing just a prefix-search.
         *
         * @author Marcus@Wolschon.biz
         */
        public class FolderListFilter extends Filter {
            private CharSequence mSearchTerm;

            public CharSequence getSearchTerm() {
                return mSearchTerm;
            }

            /**
             * Do the actual search.
             * {@inheritDoc}
             *
             * @see #publishResults(CharSequence, FilterResults)
             */
            @Override
            protected FilterResults performFiltering(CharSequence searchTerm) {
                mSearchTerm = searchTerm;
                FilterResults results = new FilterResults();

                Locale locale = Locale.getDefault();
                if ((searchTerm == null) || (searchTerm.length() == 0)) {
                    List<FolderInfoHolder> list = new ArrayList<FolderInfoHolder>(mFolders);
                    results.values = list;
                    results.count = list.size();
                } else {
                    final String searchTermString = searchTerm.toString().toLowerCase(locale);
                    final String[] words = searchTermString.split(" ");
                    final int wordCount = words.length;

                    final List<FolderInfoHolder> newValues = new ArrayList<FolderInfoHolder>();

                    for (final FolderInfoHolder value : mFolders) {
                        if (value.displayName == null) {
                            continue;
                        }
                        final String valueText = value.displayName.toLowerCase(locale);

                        for (int k = 0; k < wordCount; k++) {
                            if (valueText.contains(words[k])) {
                                newValues.add(value);
                                break;
                            }
                        }
                    }

                    results.values = newValues;
                    results.count = newValues.size();
                }

                return results;
            }

            /**
             * Publish the results to the user-interface.
             * {@inheritDoc}
             */
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                //noinspection unchecked
                mFilteredFolders = Collections.unmodifiableList((ArrayList<FolderInfoHolder>) results.values);
                // Send notification that the data set changed now
                notifyDataSetChanged();
            }
        }
    }