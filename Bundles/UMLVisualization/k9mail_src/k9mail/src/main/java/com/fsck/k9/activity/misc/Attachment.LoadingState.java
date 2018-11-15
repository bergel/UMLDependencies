package com.fsck.k9.activity.misc;

/**
 * Container class for information about an attachment.
 *
 * This is used by {@link com.fsck.k9.activity.MessageCompose} to fetch and manage attachments.
 */
public enum LoadingState {
        URI_ONLY,
        METADATA,
        COMPLETE,
        CANCELLED
    }