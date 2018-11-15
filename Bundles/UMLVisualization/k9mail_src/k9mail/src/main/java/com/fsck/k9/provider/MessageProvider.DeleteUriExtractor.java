package com.fsck.k9.provider;


import com.fsck.k9.activity.MessageInfoHolder;
import com.fsck.k9.mailstore.LocalMessage;
public static class DeleteUriExtractor implements FieldExtractor<MessageInfoHolder, String> {
        @Override
        public String getField(MessageInfoHolder source) {
            LocalMessage message = source.message;
            int accountNumber = message.getAccount().getAccountNumber();
            return CONTENT_URI.buildUpon()
                    .appendPath("delete_message")
                    .appendPath(Integer.toString(accountNumber))
                    .appendPath(message.getFolder().getName())
                    .appendPath(message.getUid())
                    .build()
                    .toString();
        }
    }