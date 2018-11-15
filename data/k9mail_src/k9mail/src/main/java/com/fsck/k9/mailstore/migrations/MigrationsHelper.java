package com.fsck.k9.mailstore.migrations;


import android.content.Context;
import com.fsck.k9.Account;
import com.fsck.k9.mail.Flag;
import com.fsck.k9.mailstore.LocalStore;
import com.fsck.k9.preferences.Storage;

import java.util.List;


/**
 * Helper to allow accessing classes and methods that aren't visible or accessible to the 'migrations' package
 */
public interface MigrationsHelper {
    LocalStore getLocalStore();
    Storage getStorage();
    Account getAccount();
    Context getContext();
    String serializeFlags(List<Flag> flags);
}