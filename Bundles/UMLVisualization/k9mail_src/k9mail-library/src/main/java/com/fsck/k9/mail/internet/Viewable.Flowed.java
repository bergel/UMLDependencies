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
class Flowed extends Textual {
        private boolean delSp;

        public Flowed(Part part, boolean delSp) {
            super(part);
            this.delSp = delSp;
        }

        public boolean isDelSp() {
            return delSp;
        }
    }