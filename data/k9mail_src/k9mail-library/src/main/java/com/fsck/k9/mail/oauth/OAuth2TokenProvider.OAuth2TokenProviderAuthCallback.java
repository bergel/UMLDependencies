package com.fsck.k9.mail.oauth;


interface OAuth2TokenProviderAuthCallback {
        void success();
        void failure(AuthorizationException e);
    }