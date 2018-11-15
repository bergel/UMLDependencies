package com.fsck.k9.activity.compose;

import com.fsck.k9.Account;
import com.fsck.k9.Identity;

/**
 * Adapter for the <em>Choose identity</em> list view.
 *
 * <p>
 * Account names are displayed as section headers, identities as selectable list items.
 * </p>
 */
public static class IdentityContainer {
        public final Identity identity;
        public final Account account;

        IdentityContainer(Identity identity, Account account) {
            this.identity = identity;
            this.account = account;
        }
    }