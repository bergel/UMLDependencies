package com.fsck.k9.message;


public enum RecipientAutocryptStatusType {
        NO_RECIPIENTS (false, false, false),
        UNAVAILABLE (false, false, false),
        DISCOURAGE_UNCONFIRMED (true, false, false),
        DISCOURAGE_CONFIRMED (true, true, false),
        AVAILABLE_UNCONFIRMED (true, false, false),
        AVAILABLE_CONFIRMED (true, true, false),
        RECOMMENDED_UNCONFIRMED (true, false, true),
        RECOMMENDED_CONFIRMED (true, true, true),
        ERROR (false, false, false);

        private final boolean canEncrypt;
        private final boolean isConfirmed;
        private final boolean isMutual;

        RecipientAutocryptStatusType(boolean canEncrypt, boolean isConfirmed, boolean isMutual) {
            this.canEncrypt = canEncrypt;
            this.isConfirmed = isConfirmed;
            this.isMutual = isMutual;
        }

        public boolean canEncrypt() {
            return canEncrypt;
        }

        public boolean isConfirmed() {
            return isConfirmed;
        }

        public boolean isMutual() {
            return isMutual;
        }
    }