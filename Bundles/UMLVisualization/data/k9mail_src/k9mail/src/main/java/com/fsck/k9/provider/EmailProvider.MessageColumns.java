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
public interface MessageColumns {
        String ID = "id";
        String UID = "uid";
        String INTERNAL_DATE = "internal_date";
        String SUBJECT = "subject";
        String DATE = "date";
        String MESSAGE_ID = "message_id";
        String SENDER_LIST = "sender_list";
        String TO_LIST = "to_list";
        String CC_LIST = "cc_list";
        String BCC_LIST = "bcc_list";
        String REPLY_TO_LIST = "reply_to_list";
        String FLAGS = "flags";
        String ATTACHMENT_COUNT = "attachment_count";
        String FOLDER_ID = "folder_id";
        String PREVIEW_TYPE = "preview_type";
        String PREVIEW = "preview";
        String READ = "read";
        String FLAGGED = "flagged";
        String ANSWERED = "answered";
        String FORWARDED = "forwarded";
    }