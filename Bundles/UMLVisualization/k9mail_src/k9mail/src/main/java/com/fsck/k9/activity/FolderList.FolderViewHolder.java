package com.fsck.k9.activity;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * FolderList is the primary user interface for the program. This
 * Activity shows list of the Account's folders
 */
static class FolderViewHolder {
        public TextView folderName;

        public TextView folderStatus;

        public TextView newMessageCount;
        public TextView flaggedMessageCount;
        public View newMessageCountIcon;
        public View flaggedMessageCountIcon;
        public View newMessageCountWrapper;
        public View flaggedMessageCountWrapper;

        public RelativeLayout activeIcons;
        public String rawFolderName;
        public View chip;
        public LinearLayout folderListItemLayout;
    }