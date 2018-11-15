package com.fsck.k9.mail.store.imap;


import java.util.List;
import java.util.Set;
static class GroupedIds {
        public final Set<Long> ids;
        public final List<ContiguousIdGroup> idGroups;


        GroupedIds(Set<Long> ids, List<ContiguousIdGroup> idGroups) {
            if (ids.isEmpty() && idGroups.isEmpty()) {
                throw new IllegalArgumentException("Must have at least one id");
            }

            this.ids = ids;
            this.idGroups = idGroups;
        }
    }