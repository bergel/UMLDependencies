package com.fsck.k9.widget.list;


import android.net.Uri;

import java.util.Calendar;
import java.util.Locale;
private class MailItem {
        final long date;
        final String sender;
        final String preview;
        final String subject;
        final boolean unread;
        final boolean hasAttachment;
        final Uri uri;


        MailItem(String sender, long date, String subject, String preview, boolean unread, boolean hasAttachment,
                Uri viewUri) {
            this.sender = sender;
            this.date = date;
            this.preview = preview;
            this.subject = subject;
            this.unread = unread;
            this.uri = viewUri;
            this.hasAttachment = hasAttachment;
        }

        int getTextColor() {
            return unread ? unreadTextColor : readTextColor;
        }

        String getDateFormatted(String format) {
            calendar.setTimeInMillis(date);

            return String.format(format,
                    calendar.get(Calendar.DAY_OF_MONTH),
                    calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()));
        }
    }