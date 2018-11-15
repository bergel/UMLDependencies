package com.fsck.k9.provider;


import com.fsck.k9.activity.MessageInfoHolder;
public static class IdExtractor implements FieldExtractor<MessageInfoHolder, Long> {
        @Override
        public Long getField(MessageInfoHolder source) {
            return source.message.getDatabaseId();
        }
    }