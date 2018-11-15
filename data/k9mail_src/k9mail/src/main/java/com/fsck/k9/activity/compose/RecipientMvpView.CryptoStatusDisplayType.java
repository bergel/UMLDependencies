package com.fsck.k9.activity.compose;


import com.fsck.k9.R;





        final int childIdToDisplay;

        CryptoStatusDisplayType(int childIdToDisplay) {
            this.childIdToDisplay = childIdToDisplay;
        }
    }

    public enum CryptoSpecialModeDisplayType {
        NONE(VIEW_INDEX_HIDDEN),
        PGP_INLINE(R.id.crypto_special_inline),
        SIGN_ONLY(R.id.crypto_special_sign_only),
        SIGN_ONLY_PGP_INLINE(R.id.crypto_special_sign_only_inline);


        final int childIdToDisplay;

        CryptoSpecialModeDisplayType(int childIdToDisplay) {
            this.childIdToDisplay = childIdToDisplay;
        }
    }
}
public enum CryptoStatusDisplayType {
        UNCONFIGURED(VIEW_INDEX_HIDDEN),
        UNINITIALIZED(VIEW_INDEX_HIDDEN),
        SIGN_ONLY(R.id.crypto_status_disabled),
        NO_CHOICE_EMPTY(VIEW_INDEX_HIDDEN