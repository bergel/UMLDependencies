package com.fsck.k9.provider;


import com.fsck.k9.activity.MessageInfoHolder;
@Deprecated
    public static class IncrementExtractor implements FieldExtractor<MessageInfoHolder, Integer> {
        private int count = 0;


        @Override
        public Integer getField(MessageInfoHolder source) {
            return count++;
        }
    }