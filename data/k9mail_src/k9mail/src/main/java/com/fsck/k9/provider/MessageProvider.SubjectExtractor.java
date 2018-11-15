package com.fsck.k9.provider;


import com.fsck.k9.activity.MessageInfoHolder;
public static class SubjectExtractor implements FieldExtractor<MessageInfoHolder, String> {
        @Override
        public String getField(MessageInfoHolder source) {
            return source.message.getSubject();
        }
    }