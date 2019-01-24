package com.fsck.k9.activity.setup;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.fsck.k9.K9;
import com.fsck.k9.Preferences;
import com.fsck.k9.R;
import com.fsck.k9.preferences.StorageEditor;

import java.util.ArrayList;
public class OpenPgpAppSelectDialog extends Activity {
    private static final String OPENKEYCHAIN_PACKAGE = "org.sufficientlysecure.keychain";
    private static final String APG_PROVIDER_PLACEHOLDER = "apg-placeholder";
    private static final String PACKAGE_NAME_APG = "org.thialfihar.android.apg";

    public static final String FRAG_OPENPGP_SELECT = "openpgp_select";
    public static final String FRAG_APG_DEPRECATE = "apg_deprecate";

    private static final String MARKET_INTENT_URI_BASE = "market://details?id=%s";
    private static final Intent MARKET_INTENT = new Intent(Intent.ACTION_VIEW, Uri.parse(
            String.format(MARKET_INTENT_URI_BASE, OPENKEYCHAIN_PACKAGE)));

    private static final ArrayList<String> PROVIDER_BLACKLIST = new ArrayList<>();

    static {
        // Unfortunately, the current released version of APG includes a broken version of the API
        PROVIDER_BLACKLIST.add(PACKAGE_NAME_APG);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTheme(K9.getK9Theme() == K9.Theme.LIGHT ?
                R.style.Theme_K9_Dialog_Translucent_Light : R.style.Theme_K9_Dialog_Translucent_Dark);

        if (savedInstanceState == null) {
            showOpenPgpSelectDialogFragment();
        }
    }

    private void showOpenPgpSelectDialogFragment() {
        OpenPgpAppSelectFragment fragment = new OpenPgpAppSelectFragment();
        fragment.show(getFragmentManager(), FRAG_OPENPGP_SELECT);
    }

    private void showApgDeprecationDialogFragment() {
        ApgDeprecationDialogFragment fragment = new ApgDeprecationDialogFragment();
        fragment.show(getFragmentManager(), FRAG_APG_DEPRECATE);
    }

    

    

    public void onSelectProvider(String selectedPackage) {
        if (APG_PROVIDER_PLACEHOLDER.equals(selectedPackage)) {
            showApgDeprecationDialogFragment();
            return;
        }

        persistOpenPgpProviderSetting(selectedPackage);
        finish();
    }

    private void persistOpenPgpProviderSetting(String selectedPackage) {
        K9.setOpenPgpProvider(selectedPackage);

        StorageEditor editor = Preferences.getPreferences(this).getStorage().edit();
        K9.save(editor);
        editor.commit();
    }

    public void onDismissApgDialog() {
        showOpenPgpSelectDialogFragment();
    }

    
}