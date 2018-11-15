package com.fsck.k9.message;


import com.fsck.k9.crypto.MessageCryptoStructureDetector;
import com.fsck.k9.mail.Message;
import com.fsck.k9.mail.Part;

import java.util.List;
public class ComposePgpInlineDecider {
    public boolean shouldReplyInline(Message localMessage) {
        // TODO more criteria for this? maybe check the User-Agent header?
        return messageHasPgpInlineParts(localMessage);
    }

    private boolean messageHasPgpInlineParts(Message localMessage) {
        List<Part> inlineParts = MessageCryptoStructureDetector.findPgpInlineParts(localMessage);
        return !inlineParts.isEmpty();
    }
}