package com.fsck.k9.mail.store.webdav;

/**
 * HTTP client for WebDAV communication
 */
public static class WebDavHttpClientFactory {

        public WebDavHttpClient create() {
            return new WebDavHttpClient();
        }
    }