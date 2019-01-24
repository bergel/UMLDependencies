package com.fsck.k9.helper;


import android.support.annotation.VisibleForTesting;
import com.fsck.k9.mail.Address;

import java.util.List;
public static class ReplyToAddresses {
        public final Address[] to;
        public final Address[] cc;

        @VisibleForTesting
        public ReplyToAddresses(List<Address> toAddresses, List<Address> ccAddresses) {
            to = toAddresses.toArray(new Address[toAddresses.size()]);
            cc = ccAddresses.toArray(new Address[ccAddresses.size()]);
        }

        @VisibleForTesting
        public ReplyToAddresses(Address[] toAddresses) {
            to = toAddresses;
            cc = new Address[0];
        }
    }