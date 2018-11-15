package com.fsck.k9.activity.misc;


import com.fsck.k9.mail.Address;

import java.util.Locale;
private class FallbackGlideParams {
        final Address address;

        FallbackGlideParams(Address address) {
            this.address = address;
        }

        public String getId() {
            return String.format(Locale.ROOT, "%s-%s", address.getAddress(), address.getPersonal());
        }
    }