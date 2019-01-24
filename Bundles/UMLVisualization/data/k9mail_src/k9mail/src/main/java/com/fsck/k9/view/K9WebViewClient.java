package com.fsck.k9.view;


import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Browser;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.fsck.k9.mailstore.AttachmentResolver;
import com.fsck.k9.view.MessageWebView.OnPageFinishedListener;
import timber.log.Timber;

import java.io.InputStream;


/**
 * {@link WebViewClient} that intercepts requests for {@code cid:} URIs to load the respective body part.
 */
abstract class K9WebViewClient extends WebViewClient {
    private static final String CID_SCHEME = "cid";
    private static final WebResourceResponse RESULT_DO_NOT_INTERCEPT = null;
    private static final WebResourceResponse RESULT_DUMMY_RESPONSE = new WebResourceResponse(null, null, null);
    private OnPageFinishedListener onPageFinishedListener;


    @Nullable
    private final AttachmentResolver attachmentResolver;


    public static K9WebViewClient newInstance(@Nullable AttachmentResolver attachmentResolver) {
        if (Build.VERSION.SDK_INT < 21) {
            return new PreLollipopWebViewClient(attachmentResolver);
        }

        return new LollipopWebViewClient(attachmentResolver);
    }


    private K9WebViewClient(@Nullable AttachmentResolver attachmentResolver) {
        this.attachmentResolver = attachmentResolver;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView webView, String url) {
        Uri uri = Uri.parse(url);
        if (CID_SCHEME.equals(uri.getScheme())) {
            return false;
        }

        Context context = webView.getContext();
        Intent intent = createBrowserViewIntent(uri, context);
        addActivityFlags(intent);

        boolean overridingUrlLoading = false;
        try {
            context.startActivity(intent);
            overridingUrlLoading = true;
        } catch (ActivityNotFoundException ex) {
            // If no application can handle the URL, assume that the WebView can handle it.
        }

        return overridingUrlLoading;
    }

    private Intent createBrowserViewIntent(Uri uri, Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.putExtra(Browser.EXTRA_APPLICATION_ID, context.getPackageName());
        intent.putExtra(Browser.EXTRA_CREATE_NEW_TAB, true);
        return intent;
    }

    protected abstract void addActivityFlags(Intent intent);

    protected abstract void addCacheControlHeader(WebResourceResponse response);

    protected WebResourceResponse shouldInterceptRequest(WebView webView, Uri uri) {
        if (!CID_SCHEME.equals(uri.getScheme())) {
            return RESULT_DO_NOT_INTERCEPT;
        }

        if (attachmentResolver == null) {
            return RESULT_DUMMY_RESPONSE;
        }

        String cid = uri.getSchemeSpecificPart();
        if (TextUtils.isEmpty(cid)) {
            return RESULT_DUMMY_RESPONSE;
        }

        Uri attachmentUri = attachmentResolver.getAttachmentUriForContentId(cid);
        if (attachmentUri == null) {
            return RESULT_DUMMY_RESPONSE;
        }

        Context context = webView.getContext();
        ContentResolver contentResolver = context.getContentResolver();
        try {
            String mimeType = contentResolver.getType(attachmentUri);
            InputStream inputStream = contentResolver.openInputStream(attachmentUri);

            WebResourceResponse webResourceResponse = new WebResourceResponse(mimeType, null, inputStream);
            addCacheControlHeader(webResourceResponse);
            return webResourceResponse;
        } catch (Exception e) {
            Timber.e(e, "Error while intercepting URI: %s", uri);
            return RESULT_DUMMY_RESPONSE;
        }
    }

    public void setOnPageFinishedListener(OnPageFinishedListener onPageFinishedListener) {
        this.onPageFinishedListener = onPageFinishedListener;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        if (onPageFinishedListener != null) {
            onPageFinishedListener.onPageFinished();
        }
    }

    

    
}