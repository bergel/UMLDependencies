package com.fsck.k9.mailstore;


private static class MessagePartType {
        static final int UNKNOWN = 0;
        static final int ALTERNATIVE_PLAIN = 1;
        static final int ALTERNATIVE_HTML = 2;
        static final int TEXT = 3;
        static final int RELATED = 4;
        static final int ATTACHMENT = 5;
        static final int HIDDEN_ATTACHMENT = 6;
    }