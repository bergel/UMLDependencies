package com.fsck.k9.mailstore;


import android.support.annotation.VisibleForTesting;
@VisibleForTesting
    static class ViewableExtractedText {
        public final String text;
        public final String html;

        ViewableExtractedText(String text, String html) {
            this.text = text;
            this.html = html;
        }
    }