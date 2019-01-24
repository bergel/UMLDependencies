package com.fsck.k9.view;


import android.content.Intent;
import android.net.Uri;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.fsck.k9.mailstore.AttachmentResolver;


/**
 * {@link WebViewClient} that intercepts requests for {@code cid:} URIs to load the respective body part.
 */
@SuppressWarnings("deprecation")
    private static class PreLollipopWebViewClient extends K9WebViewClient {
        protected PreLollipopWebViewClient(AttachmentResolver attachmentResolver) {
            super(attachmentResolver);
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView webView, String url) {
            return shouldInterceptRequest(webView, Uri.parse(url));
        }

        @Override
        protected void addActivityFlags(Intent intent) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        }

        @Override
        protected void addCacheControlHeader(WebResourceResponse response) {
            // Sadly, adding headers is not supported prior to Lollipop
        }
    }