package com.fsck.k9.activity;

import android.view.View;
import android.view.View.OnClickListener;
import com.fsck.k9.search.LocalSearch;

/**
 * FolderList is the primary user interface for the program. This
 * Activity shows list of the Account's folders
 */
private class FolderClickListener implements OnClickListener {

        final LocalSearch search;

        FolderClickListener(LocalSearch search) {
            this.search = search;
        }

        @Override
        public void onClick(View v) {
            MessageList.actionDisplaySearch(FolderList.this, search, true, false);
        }
    }