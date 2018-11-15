package com.fsck.k9.message.quote;

/**
 * <p>Represents an HTML document with an insertion point for placing a reply. The quoted
 * document may have been modified to make it suitable for insertion. The modified quoted
 * document should be used in place of the original document.</p>
 *
 * <p>Changes to the user-generated inserted content should be done with {@link
 * #setUserContent(String)}.</p>
 *
 * TODO: This container should also have a text part, along with its insertion point.  Or maybe a generic InsertableContent and maintain one each for Html and Text?
 */
public enum InsertionLocation {
        BEFORE_QUOTE, AFTER_QUOTE
    }