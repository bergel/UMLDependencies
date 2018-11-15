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
public interface SpecialColumns {
        String ACCOUNT_UUID = "account_uuid";

        String THREAD_COUNT = "thread_count";

        String FOLDER_NAME = "name";
        String INTEGRATE = "integrate";
    }