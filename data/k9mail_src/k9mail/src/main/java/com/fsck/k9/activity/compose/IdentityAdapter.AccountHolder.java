package com.fsck.k9.activity.compose;

import android.view.View;
import android.widget.TextView;

/**
 * Adapter for the <em>Choose identity</em> list view.
 *
 * <p>
 * Account names are displayed as section headers, identities as selectable list items.
 * </p>
 */
static class AccountHolder {
        public TextView name;
        public View chip;
    }