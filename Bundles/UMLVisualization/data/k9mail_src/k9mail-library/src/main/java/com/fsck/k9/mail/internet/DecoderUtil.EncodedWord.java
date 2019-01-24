package com.fsck.k9.mail.internet;

/**
 * Static methods for decoding strings, byte arrays and encoded words.
 *
 * This class is copied from the org.apache.james.mime4j.decoder.DecoderUtil class.  It's modified here in order to
 * decode emoji characters in the Subject headers.  The method to decode emoji depends on the MimeMessage class because
 * it has to be determined with the sender address, the mailer and so on.
 */
private static class EncodedWord {
        private String charset;
        private String encoding;
        private String encodedText;
    }