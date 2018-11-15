package com.fsck.k9.activity;

import android.os.Handler;
import android.view.View;
import android.widget.Toast;
import com.fsck.k9.R;
import com.fsck.k9.helper.SizeFormatter;

import java.util.List;

/**
 * FolderList is the primary user interface for the program. This
 * Activity shows list of the Account's folders
 */
class FolderListHandler extends Handler {

        public void refreshTitle() {
            runOnUiThread(new Runnable() {
                public void run() {
                    mActionBarTitle.setText(getString(R.string.folders_title));

                    if (mUnreadMessageCount == 0) {
                        mActionBarUnread.setVisibility(View.GONE);
                    } else {
                        mActionBarUnread.setText(String.format("%d", mUnreadMessageCount));
                        mActionBarUnread.setVisibility(View.VISIBLE);
                    }

                    String operation = mAdapter.mListener.getOperation(FolderList.this);
                    if (operation.length() < 1) {
                        mActionBarSubTitle.setText(mAccount.getEmail());
                    } else {
                        mActionBarSubTitle.setText(operation);
                    }
                }
            });
        }


        public void newFolders(final List<FolderInfoHolder> newFolders) {
            runOnUiThread(new Runnable() {
                public void run() {
                    mAdapter.mFolders.clear();
                    mAdapter.mFolders.addAll(newFolders);
                    mAdapter.mFilteredFolders = mAdapter.mFolders;
                    mHandler.dataChanged();
                }
            });
        }

        public void workingAccount(final int res) {
            runOnUiThread(new Runnable() {
                public void run() {
                    String toastText = getString(res, mAccount.getDescription());
                    Toast toast = Toast.makeText(getApplication(), toastText, Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
        }

        public void accountSizeChanged(final long oldSize, final long newSize) {
            runOnUiThread(new Runnable() {
                public void run() {
                    String toastText = getString(R.string.account_size_changed, mAccount.getDescription(), SizeFormatter.formatSize(getApplication(), oldSize), SizeFormatter.formatSize(getApplication(), newSize));

                    Toast toast = Toast.makeText(getApplication(), toastText, Toast.LENGTH_LONG);
                    toast.show();
                }
            });
        }

        public void folderLoading(final String folder, final boolean loading) {
            runOnUiThread(new Runnable() {
                public void run() {
                    FolderInfoHolder folderHolder = mAdapter.getFolder(folder);


                    if (folderHolder != null) {
                        folderHolder.loading = loading;
                    }

                }
            });
        }

        public void progress(final boolean progress) {
            // Make sure we don't try this before the menu is initialized
            // this could happen while the activity is initialized.
            if (mRefreshMenuItem == null) {
                return;
            }

            runOnUiThread(new Runnable() {
                public void run() {
                    if (progress) {
                        mRefreshMenuItem.setActionView(mActionBarProgressView);
                    } else {
                        mRefreshMenuItem.setActionView(null);
                    }
                }
            });

        }

        public void dataChanged() {
            runOnUiThread(new Runnable() {
                public void run() {
                    mAdapter.notifyDataSetChanged();
                }
            });
        }
    }