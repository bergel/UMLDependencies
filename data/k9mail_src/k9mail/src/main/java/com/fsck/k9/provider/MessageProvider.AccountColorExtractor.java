package com.fsck.k9.provider;


import com.fsck.k9.activity.MessageInfoHolder;
public static class AccountColorExtractor implements FieldExtractor<MessageInfoHolder, Integer> {
        @Override
        public Integer getField(MessageInfoHolder source) {
            return source.message.getAccount().getChipColor();
        }
    }