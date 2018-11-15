package com.fsck.k9.mailstore;


static class DataLocation {
        static final int MISSING = 0;
        static final int IN_DATABASE = 1;
        static final int ON_DISK = 2;
        static final int CHILD_PART_CONTAINS_DATA = 3;
    }