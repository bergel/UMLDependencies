package com.fsck.k9.activity.setup;


import android.os.AsyncTask;
import android.preference.ListPreference;
import android.preference.PreferenceScreen;
import com.fsck.k9.K9;
import com.fsck.k9.mail.Folder;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
private class PopulateFolderPrefsTask extends AsyncTask<Void, Void, Void> {
        List <? extends Folder > folders = new LinkedList<>();
        String[] allFolderValues;
        String[] allFolderLabels;

        @Override
        protected Void doInBackground(Void... params) {
            try {
                folders = account.getLocalStore().getPersonalNamespaces(false);
            } catch (Exception e) {
                /// this can't be checked in
            }

            // TODO: In the future the call above should be changed to only return remote folders.
            // For now we just remove the Outbox folder if present.
            Iterator <? extends Folder > iter = folders.iterator();
            while (iter.hasNext()) {
                Folder folder = iter.next();
                if (account.getOutboxFolderName().equals(folder.getName())) {
                    iter.remove();
                }
            }

            allFolderValues = new String[folders.size() + 1];
            allFolderLabels = new String[folders.size() + 1];

            allFolderValues[0] = K9.FOLDER_NONE;
            allFolderLabels[0] = K9.FOLDER_NONE;

            int i = 1;
            for (Folder folder : folders) {
                allFolderLabels[i] = folder.getName();
                allFolderValues[i] = folder.getName();
                i++;
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            autoExpandFolder = (ListPreference) findPreference(PREFERENCE_AUTO_EXPAND_FOLDER);
            autoExpandFolder.setEnabled(false);
            archiveFolder = (ListPreference) findPreference(PREFERENCE_ARCHIVE_FOLDER);
            archiveFolder.setEnabled(false);
            draftsFolder = (ListPreference) findPreference(PREFERENCE_DRAFTS_FOLDER);
            draftsFolder.setEnabled(false);
            sentFolder = (ListPreference) findPreference(PREFERENCE_SENT_FOLDER);
            sentFolder.setEnabled(false);
            spamFolder = (ListPreference) findPreference(PREFERENCE_SPAM_FOLDER);
            spamFolder.setEnabled(false);
            trashFolder = (ListPreference) findPreference(PREFERENCE_TRASH_FOLDER);
            trashFolder.setEnabled(false);

            if (!isMoveCapable) {
                PreferenceScreen foldersCategory =
                        (PreferenceScreen) findPreference(PREFERENCE_CATEGORY_FOLDERS);
                foldersCategory.removePreference(archiveFolder);
                foldersCategory.removePreference(spamFolder);
                foldersCategory.removePreference(draftsFolder);
                foldersCategory.removePreference(sentFolder);
                foldersCategory.removePreference(trashFolder);
            }
        }

        @Override
        protected void onPostExecute(Void res) {
            initListPreference(autoExpandFolder, account.getAutoExpandFolderName(), allFolderLabels, allFolderValues);
            autoExpandFolder.setEnabled(true);
            if (isMoveCapable) {
                initListPreference(archiveFolder, account.getArchiveFolderName(), allFolderLabels, allFolderValues);
                initListPreference(draftsFolder, account.getDraftsFolderName(), allFolderLabels, allFolderValues);
                initListPreference(sentFolder, account.getSentFolderName(), allFolderLabels, allFolderValues);
                initListPreference(spamFolder, account.getSpamFolderName(), allFolderLabels, allFolderValues);
                initListPreference(trashFolder, account.getTrashFolderName(), allFolderLabels, allFolderValues);
                archiveFolder.setEnabled(true);
                spamFolder.setEnabled(true);
                draftsFolder.setEnabled(true);
                sentFolder.setEnabled(true);
                trashFolder.setEnabled(true);
            }
        }
    }