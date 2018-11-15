package com.fsck.k9.provider;


import com.fsck.k9.activity.MessageInfoHolder;
public static class SenderExtractor implements FieldExtractor<MessageInfoHolder, CharSequence> {
        @Override
        public CharSequence getField(MessageInfoHolder source) {
            return source.sender;
        }
    }