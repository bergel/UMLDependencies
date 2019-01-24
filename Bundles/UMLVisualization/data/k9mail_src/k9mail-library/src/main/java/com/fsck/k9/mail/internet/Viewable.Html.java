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
class Html extends Textual {
        public Html(Part part) {
            super(part);
        }
    }