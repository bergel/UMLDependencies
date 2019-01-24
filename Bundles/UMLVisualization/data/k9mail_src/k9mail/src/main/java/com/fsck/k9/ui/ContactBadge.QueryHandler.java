package com.fsck.k9.ui;


import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Intents;
import android.provider.ContactsContract.QuickContact;


/**
 * ContactBadge replaces the android ContactBadge for custom drawing.
 * <p>
 * Based on QuickContactBadge:
 * https://android.googlesource.com/platform/frameworks/base/+/master/core/java/android/widget/QuickContactBadge.java
 */
private class QueryHandler extends AsyncQueryHandler {

        QueryHandler(ContentResolver cr) {
            super(cr);
        }

        @Override
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
            Uri lookupUri = null;
            Uri createUri = null;
            boolean trigger = false;
            Bundle extras = (cookie != null) ? (Bundle) cookie : new Bundle();
            try {
                switch (token) {
                    case TOKEN_EMAIL_LOOKUP_AND_TRIGGER:
                        trigger = true;
                        createUri = Uri.fromParts("mailto",
                                extras.getString(EXTRA_URI_CONTENT), null);

                        //$FALL-THROUGH$
                    case TOKEN_EMAIL_LOOKUP: {
                        if (cursor != null && cursor.moveToFirst()) {
                            long contactId = cursor.getLong(EMAIL_ID_COLUMN_INDEX);
                            String lookupKey = cursor.getString(EMAIL_LOOKUP_STRING_COLUMN_INDEX);
                            lookupUri = Contacts.getLookupUri(contactId, lookupKey);
                        }
                        break;
                    }
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }

            contactUri = lookupUri;
            onContactUriChanged();

            if (trigger && lookupUri != null) {
                // Found contact, so trigger QuickContact
                QuickContact.showQuickContact(
                        getContext(), ContactBadge.this, lookupUri, QuickContact.MODE_LARGE, null);
            } else if (createUri != null) {
                // Prompt user to add this person to contacts
                final Intent intent = new Intent(Intents.SHOW_OR_CREATE_CONTACT, createUri);
                extras.remove(EXTRA_URI_CONTENT);
                intent.putExtras(extras);
                getContext().startActivity(intent);
            }
        }
    }