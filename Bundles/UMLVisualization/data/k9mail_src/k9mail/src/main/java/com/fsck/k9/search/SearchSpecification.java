package com.fsck.k9.search;

import android.os.Parcelable;
public interface SearchSpecification extends Parcelable {

    /**
     * Get all the uuids of accounts this search acts on.
     * @return Array of uuids.
     */
    public String[] getAccountUuids();

    /**
     * Returns the search's name if it was named.
     * @return Name of the search.
     */
    public String getName();

    /**
     * Returns the root node of the condition tree accompanying
     * the search.
     *
     * @return Root node of conditions tree.
     */
    public ConditionsTreeNode getConditions();

    /*
     * Some meta names for certain conditions.
     */
    public static final String ALL_ACCOUNTS = "allAccounts";

    ///////////////////////////////////////////////////////////////
    // ATTRIBUTE enum
    ///////////////////////////////////////////////////////////////
    

    ///////////////////////////////////////////////////////////////
    // SEARCHFIELD enum
    ///////////////////////////////////////////////////////////////
    /*
     * Using an enum in order to have more robust code. Users ( & coders )
     * are prevented from passing illegal fields. No database overhead
     * when invalid fields passed.
     *
     * By result, only the fields in here are searchable.
     *
     * Fields not in here at this moment ( and by effect not searchable ):
     *      id, html_content, internal_date, message_id,
     *      preview, mime_type
     *
     */
    


    ///////////////////////////////////////////////////////////////
    // SearchCondition class
    ///////////////////////////////////////////////////////////////
    /**
     * This class represents 1 value for a certain search field. One
     * value consists of three things:
     *      an attribute: equals, starts with, contains,...
     *      a searchfield: date, flags, sender, subject,...
     *      a value: "apple", "jesse",..
     *
     * @author dzan
     */
    
}