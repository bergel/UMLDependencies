package com.fsck.k9.provider;


import com.fsck.k9.activity.MessageInfoHolder;
import com.fsck.k9.mail.Flag;
public static class HasStarExtractor implements FieldExtractor<MessageInfoHolder, Boolean> {
        @Override
        public Boolean getField(MessageInfoHolder source) {
            return source.message.isSet(Flag.FLAGGED);
        }
    }