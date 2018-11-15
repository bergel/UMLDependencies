package com.fsck.k9.provider;


/**
 * Content Provider used to display the message list etc.
 *
 * <p>
 * For now this content provider is for internal use only. In the future we may allow third-party
 * apps to access K-9 Mail content using this content provider.
 * </p>
 */
/*
 * TODO:
 * - add support for account list and folder list
 */
public interface FolderColumns {
        String ID = "id";
        String NAME = "name";
        String LAST_UPDATED = "last_updated";
        String UNREAD_COUNT = "unread_count";
        String VISIBLE_LIMIT = "visible_limit";
        String STATUS = "status";
        String PUSH_STATE = "push_state";
        String LAST_PUSHED = "last_pushed";
        String FLAGGED_COUNT = "flagged_count";
        String INTEGRATE = "integrate";
        String TOP_GROUP = "top_group";
        String POLL_CLASS = "poll_class";
        String PUSH_CLASS = "push_class";
        String DISPLAY_CLASS = "display_class";
    }