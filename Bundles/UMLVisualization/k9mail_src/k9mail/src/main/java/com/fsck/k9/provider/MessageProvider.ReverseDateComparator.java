package com.fsck.k9.provider;


import com.fsck.k9.activity.MessageInfoHolder;

import java.util.Comparator;
public static class ReverseDateComparator implements Comparator<MessageInfoHolder> {
        @Override
        public int compare(MessageInfoHolder object2, MessageInfoHolder object1) {
            if (object1.compareDate == null) {
                return (object2.compareDate == null ? 0 : 1);
            } else if (object2.compareDate == null) {
                return -1;
            } else {
                return object1.compareDate.compareTo(object2.compareDate);
            }
        }
    }