package com.fsck.k9.view;


import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build.VERSION_CODES;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.fsck.k9.mailstore.AttachmentResolver;

import java.util.Collections;
import java.util.Map;


/**
 * {@link WebViewClient} that intercepts requests for {@code cid:} URIs to load the respective body part.
 */
@TargetApi(VERSION_CODES.LOLLIPOP)
    private static class LollipopWebViewClient extends K9WebViewClient {
        protected LollipopWebViewClient(AttachmentResolver attachmentResolver) {
            super(attachmentResolver);
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView webView, WebResourceRequest request) {
            return shouldInterceptRequest(webView, request.getUrl());
        }

        @Override
        protected void addActivityFlags(Intent intent) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        }

        @Override
        protected void addCacheControlHeader(WebResourceResponse response) {
            Map<String, String> headers = Collections.singletonMap("Cache-Control", "no-store");
            response.setResponseHeaders(headers);
        }
    }