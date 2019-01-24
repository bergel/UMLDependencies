package com.fsck.k9.message;


import com.fsck.k9.crypto.MessageCryptoStructureDetector;
import com.fsck.k9.mail.Message;
import com.fsck.k9.mail.Part;

import java.util.List;
public class ComposePgpEnableByDefaultDecider {
    public boolean shouldEncryptByDefault(Message localMessage) {
        return messageIsEncrypted(localMessage);
    }

    private boolean messageIsEncrypted(Message localMessage) {
        List<Part> encryptedParts = MessageCryptoStructureDetector.findMultipartEncryptedParts(localMessage);
        return !encryptedParts.isEmpty();
    }
}