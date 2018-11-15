package com.fsck.k9.activity.setup;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import com.fsck.k9.K9;
import com.fsck.k9.R;
import org.openintents.openpgp.util.OpenPgpApi;
import org.openintents.openpgp.util.OpenPgpAppPreference;

import java.util.ArrayList;
import java.util.List;
public static class OpenPgpAppSelectFragment extends DialogFragment {
        private ArrayList<OpenPgpProviderEntry> openPgpProviderList = new ArrayList<>();
        private String selectedPackage;

        private void populateAppList() {
            openPgpProviderList.clear();

            Context context = getActivity();

            OpenPgpProviderEntry noneEntry = new OpenPgpProviderEntry("",
                    context.getString(R.string.openpgp_list_preference_none),
                    getResources().getDrawable(R.drawable.ic_action_cancel_launchersize));
            openPgpProviderList.add(0, noneEntry);

            if (OpenPgpAppPreference.isApgInstalled(getActivity())) {
                Drawable icon = getResources().getDrawable(R.drawable.ic_apg_small);
                openPgpProviderList.add(new OpenPgpProviderEntry(
                        APG_PROVIDER_PLACEHOLDER, getString(R.string.apg), icon));
            }

            // search for OpenPGP providers...
            Intent intent = new Intent(OpenPgpApi.SERVICE_INTENT_2);
            List<ResolveInfo> resInfo = getActivity().getPackageManager().queryIntentServices(intent, 0);
            boolean hasNonBlacklistedChoices = false;
            if (resInfo != null) {
                for (ResolveInfo resolveInfo : resInfo) {
                    if (resolveInfo.serviceInfo == null) {
                        continue;
                    }

                    String packageName = resolveInfo.serviceInfo.packageName;
                    String simpleName = String.valueOf(resolveInfo.serviceInfo.loadLabel(context.getPackageManager()));
                    Drawable icon = resolveInfo.serviceInfo.loadIcon(context.getPackageManager());

                    if (!PROVIDER_BLACKLIST.contains(packageName)) {
                        openPgpProviderList.add(new OpenPgpProviderEntry(packageName, simpleName, icon));
                        hasNonBlacklistedChoices = true;
                    }
                }
            }

            if (!hasNonBlacklistedChoices) {
                // add install links if provider list is empty
                resInfo = context.getPackageManager().queryIntentActivities(MARKET_INTENT, 0);
                for (ResolveInfo resolveInfo : resInfo) {
                    Intent marketIntent = new Intent(MARKET_INTENT);
                    marketIntent.setPackage(resolveInfo.activityInfo.packageName);
                    Drawable icon = resolveInfo.activityInfo.loadIcon(context.getPackageManager());
                    String marketName = String.valueOf(resolveInfo.activityInfo.applicationInfo
                            .loadLabel(context.getPackageManager()));
                    String simpleName = String.format(context.getString(R.string
                            .openpgp_install_openkeychain_via), marketName);
                    openPgpProviderList.add(new OpenPgpProviderEntry(OPENKEYCHAIN_PACKAGE, simpleName,
                            icon, marketIntent));
                }
            }
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            selectedPackage = K9.getOpenPgpProvider();
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setTitle(R.string.account_settings_crypto_app);

            // do again, maybe an app has now been installed
            populateAppList();

            // Init ArrayAdapter with OpenPGP Providers
            ListAdapter adapter = new ArrayAdapter<OpenPgpProviderEntry>(getActivity(),
                    android.R.layout.select_dialog_singlechoice, android.R.id.text1, openPgpProviderList) {
                public View getView(int position, View convertView, ViewGroup parent) {
                    // User super class to create the View
                    View v = super.getView(position, convertView, parent);
                    TextView tv = (TextView) v.findViewById(android.R.id.text1);

                    // Put the image on the TextView
                    tv.setCompoundDrawablesWithIntrinsicBounds(openPgpProviderList.get(position).icon, null,
                            null, null);

                    // Add margin between image and text (support various screen densities)
                    int dp10 = (int) (10 * getContext().getResources().getDisplayMetrics().density + 0.5f);
                    tv.setCompoundDrawablePadding(dp10);

                    return v;
                }
            };

            builder.setSingleChoiceItems(adapter, getIndexOfProviderList(selectedPackage),
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            OpenPgpProviderEntry entry = openPgpProviderList.get(which);

                            if (entry.intent != null) {
                                /*
                                 * Intents are called as activity
                                 *
                                 * Current approach is to assume the user installed the app.
                                 * If he does not, the selected package is not valid.
                                 *
                                 * However  applications should always consider this could happen,
                                 * as the user might remove the currently used OpenPGP app.
                                 */
                                getActivity().startActivity(entry.intent);
                                return;
                            }

                            selectedPackage = entry.packageName;

                            dialog.dismiss();
                        }
                    });

            return builder.create();
        }

        private int getIndexOfProviderList(String packageName) {
            for (OpenPgpProviderEntry app : openPgpProviderList) {
                if (app.packageName.equals(packageName)) {
                    return openPgpProviderList.indexOf(app);
                }
            }

            // default is "none"
            return 0;
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            super.onDismiss(dialog);

            ((OpenPgpAppSelectDialog) getActivity()).onSelectProvider(selectedPackage);
        }
    }