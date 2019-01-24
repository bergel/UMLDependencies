package com.fsck.k9.mail.store.imap;


static class ContiguousIdGroup {
        public final long start;
        public final long end;


        ContiguousIdGroup(long start, long end) {
            if (start >= end) {
                throw new IllegalArgumentException("start >= end");
            }

            this.start = start;
            this.end = end;
        }

        @Override
        public String toString() {
            return start + ":" + end;
        }
    }