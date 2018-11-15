package com.fsck.k9.mail.internet;

/**
 * Static methods for encoding header field values. This includes encoded-words
 * as defined in <a href='http://www.faqs.org/rfcs/rfc2047.html'>RFC 2047</a>
 * or display-names of an e-mail address, for example.
 *
 * This class is copied from the org.apache.james.mime4j.decoder.EncoderUtil class.  It's modified here in order to
 * encode emoji characters in the Subject headers.  The method to decode emoji depends on the MimeMessage class because
 * it has to be determined with the sender address.
 */
public enum Encoding {
        /** The B encoding (identical to base64 defined in RFC 2045). */
        B,
        /** The Q encoding (similar to quoted-printable defined in RFC 2045). */
        Q
    }