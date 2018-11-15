package com.fsck.k9.mailstore;


import com.fsck.k9.mail.Part;
private static class PartContainer {
        public final long parent;
        public final Part part;

        PartContainer(long parent, Part part) {
            this.parent = parent;
            this.part = part;
        }
    }