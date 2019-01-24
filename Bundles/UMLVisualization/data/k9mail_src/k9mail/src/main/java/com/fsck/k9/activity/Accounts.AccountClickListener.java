package com.fsck.k9.activity;


import android.view.View;
import android.view.View.OnClickListener;
import com.fsck.k9.search.LocalSearch;
private class AccountClickListener implements OnClickListener {

        final LocalSearch search;

        AccountClickListener(LocalSearch search) {
            this.search = search;
        }

        @Override
        public void onClick(View v) {
            MessageList.actionDisplaySearch(Accounts.this, search, true, false);
        }

    }