package com.fsck.k9.mailstore;

public static class WrappedException extends RuntimeException {
        /**
         *
         */
        private static final long serialVersionUID = 8184421232587399369L;

        public WrappedException(final Exception cause) {
            super(cause);
        }
    }