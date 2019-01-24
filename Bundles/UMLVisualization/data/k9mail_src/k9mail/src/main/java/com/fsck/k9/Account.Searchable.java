package com.fsck.k9;

/**
 * Account stores all of the settings for a single account defined by the user. It is able to save
 * and delete itself given a Preferences to work with. Each account is defined by a UUID.
 */
public enum Searchable {
        ALL, DISPLAYABLE, NONE
    }