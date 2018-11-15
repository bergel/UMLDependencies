package com.fsck.k9.provider;


import com.fsck.k9.activity.MessageInfoHolder;
public static class UnreadExtractor implements FieldExtractor<MessageInfoHolder, Boolean> {
        @Override
        public Boolean getField(MessageInfoHolder source) {
            return !source.read;
        }
    }