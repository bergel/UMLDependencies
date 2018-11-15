package com.fsck.k9.provider;


import com.fsck.k9.activity.MessageInfoHolder;
public static class SendDateExtractor implements FieldExtractor<MessageInfoHolder, Long> {
        @Override
        public Long getField(MessageInfoHolder source) {
            return source.message.getSentDate().getTime();
        }
    }