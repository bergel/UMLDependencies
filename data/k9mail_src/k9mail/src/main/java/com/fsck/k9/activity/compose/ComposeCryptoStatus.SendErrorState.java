package com.fsck.k9.activity.compose;


/** This is an immutable object which contains all relevant metadata entered
 * during e-mail composition to apply cryptographic operations before sending
 * or saving as draft.
 */
public enum SendErrorState {
        PROVIDER_ERROR,
        KEY_CONFIG_ERROR,
        ENABLED_ERROR
    }