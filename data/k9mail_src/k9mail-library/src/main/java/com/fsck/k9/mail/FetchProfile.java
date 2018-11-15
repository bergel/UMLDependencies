package com.fsck.k9.mail;

import java.util.ArrayList;

/**
 * <pre>
 * A FetchProfile is a list of items that should be downloaded in bulk for a set of messages.
 * FetchProfile can contain the following objects:
 *      FetchProfile.Item:      Described below.
 *      Message:                Indicates that the body of the entire message should be fetched.
 *                              Synonymous with FetchProfile.Item.BODY.
 *      Part:                   Indicates that the given Part should be fetched. The provider
 *                              is expected have previously created the given BodyPart and stored
 *                              any information it needs to download the content.
 * </pre>
 */
public class FetchProfile extends ArrayList<FetchProfile.Item> {
    private static final long serialVersionUID = -5520076119120964166L;

    /**
     * Default items available for pre-fetching. It should be expected that any
     * item fetched by using these items could potentially include all of the
     * previous items.
     */
    
}