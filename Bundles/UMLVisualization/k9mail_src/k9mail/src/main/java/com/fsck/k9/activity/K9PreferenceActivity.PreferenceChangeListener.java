package com.fsck.k9.activity;

import android.preference.ListPreference;
import android.preference.Preference;
private static class PreferenceChangeListener implements Preference.OnPreferenceChangeListener {
        private ListPreference mPrefView;

        private PreferenceChangeListener(final ListPreference prefView) {
            this.mPrefView = prefView;
        }

        /**
         * Show the preference value in the preference summary field.
         */
        @Override
        public boolean onPreferenceChange(final Preference preference, final Object newValue) {
            final String summary = newValue.toString();
            final int index = mPrefView.findIndexOfValue(summary);
            mPrefView.setSummary(mPrefView.getEntries()[index]);
            mPrefView.setValue(summary);
            return false;
        }
    }