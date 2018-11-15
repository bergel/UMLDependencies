package com.fsck.k9.mail.internet;

import com.fsck.k9.mail.Part;

import java.util.List;

/**
 * Empty marker class interface the class hierarchy used by
 * {@link MessageExtractor#findViewablesAndAttachments(com.fsck.k9.mail.Part, java.util.List)}
 *
 * @see Viewable.Text
 * @see Viewable.Html
 * @see Viewable.MessageHeader
 * @see Viewable.Alternative
 */
class Text extends Textual {
        public Text(Part part) {
            super(part);
        }
    }