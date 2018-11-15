package com.fsck.k9.mail.internet;

import com.fsck.k9.mail.Message;
import com.fsck.k9.mail.MessagingException;
import org.apache.james.mime4j.codec.Base64InputStream;
import org.apache.james.mime4j.codec.QuotedPrintableInputStream;
import org.apache.james.mime4j.util.CharsetUtil;
import timber.log.Timber;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;


/**
 * Static methods for decoding strings, byte arrays and encoded words.
 *
 * This class is copied from the org.apache.james.mime4j.decoder.DecoderUtil class.  It's modified here in order to
 * decode emoji characters in the Subject headers.  The method to decode emoji depends on the MimeMessage class because
 * it has to be determined with the sender address, the mailer and so on.
 */
class DecoderUtil {

    

    /**
     * Decodes an encoded word encoded with the 'B' encoding (described in
     * RFC 2047) found in a header field body.
     *
     * @param encodedWord the encoded word to decode.
     * @param charset the Java charset to use.
     * @return the decoded string.
     */
    private static String decodeB(String encodedWord, String charset) {
        byte[] bytes = encodedWord.getBytes(Charset.forName("US-ASCII"));

        Base64InputStream is = new Base64InputStream(new ByteArrayInputStream(bytes));
        try {
            return CharsetSupport.readToString(is, charset);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Decodes an encoded word encoded with the 'Q' encoding (described in
     * RFC 2047) found in a header field body.
     *
     * @param encodedWord the encoded word to decode.
     * @param charset the Java charset to use.
     * @return the decoded string.
     */
    static String decodeQ(String encodedWord, String charset) {

        /*
         * Replace _ with =20
         */
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < encodedWord.length(); i++) {
            char c = encodedWord.charAt(i);
            if (c == '_') {
                sb.append("=20");
            } else {
                sb.append(c);
            }
        }

        byte[] bytes = sb.toString().getBytes(Charset.forName("US-ASCII"));

        QuotedPrintableInputStream is = new QuotedPrintableInputStream(new ByteArrayInputStream(bytes));
        try {
            return CharsetSupport.readToString(is, charset);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Decodes a string containing encoded words as defined by RFC 2047.
     * Encoded words in have the form
     * =?charset?enc?Encoded word?= where enc is either 'Q' or 'q' for
     * quoted-printable and 'B' or 'b' for Base64.
     *
     * ANDROID:  COPIED FROM A NEWER VERSION OF MIME4J
     *
     * @param body the string to decode.
     * @param message the message which has the string.
     * @return the decoded string.
     */
    public static String decodeEncodedWords(String body, Message message) {

        // ANDROID:  Most strings will not include "=?" so a quick test can prevent unneeded
        // object creation.  This could also be handled via lazy creation of the StringBuilder.
        if (!body.contains("=?")) {
            return body;
        }

        EncodedWord previousWord = null;
        int previousEnd = 0;

        StringBuilder sb = new StringBuilder();

        while (true) {
            int begin = body.indexOf("=?", previousEnd);
            if (begin == -1) {
                decodePreviousAndAppendSuffix(sb, previousWord, body, previousEnd);
                return sb.toString();
            }

            // ANDROID:  The mime4j original version has an error here.  It gets confused if
            // the encoded string begins with an '=' (just after "?Q?").  This patch seeks forward
            // to find the two '?' in the "header", before looking for the final "?=".
            int qm1 = body.indexOf('?', begin + 2);
            if (qm1 == -1) {
                decodePreviousAndAppendSuffix(sb, previousWord, body, previousEnd);
                return sb.toString();
            }

            int qm2 = body.indexOf('?', qm1 + 1);
            if (qm2 == -1) {
                decodePreviousAndAppendSuffix(sb, previousWord, body, previousEnd);
                return sb.toString();
            }

            int end = body.indexOf("?=", qm2 + 1);
            if (end == -1) {
                decodePreviousAndAppendSuffix(sb, previousWord, body, previousEnd);
                return sb.toString();
            }
            end += 2;

            String sep = body.substring(previousEnd, begin);

            EncodedWord word = extractEncodedWord(body, begin, end, message);

            if (previousWord == null) {
                sb.append(sep);
                if (word == null) {
                    sb.append(body.substring(begin, end));
                }
            } else {
                if (word == null) {
                    sb.append(decodeEncodedWord(previousWord));
                    sb.append(sep);
                    sb.append(body.substring(begin, end));
                } else {
                    if (!CharsetUtil.isWhitespace(sep)) {
                        sb.append(decodeEncodedWord(previousWord));
                        sb.append(sep);
                    } else if (previousWord.encoding.equals(word.encoding) &&
                            previousWord.charset.equals(word.charset)) {
                        word.encodedText = previousWord.encodedText + word.encodedText;
                    } else {
                        sb.append(decodeEncodedWord(previousWord));
                    }
                }
            }

            previousWord = word;
            previousEnd = end;
        }
    }

    private static void decodePreviousAndAppendSuffix(StringBuilder sb, EncodedWord previousWord, String body,
            int previousEnd) {

        if (previousWord != null) {
            sb.append(decodeEncodedWord(previousWord));
        }

        sb.append(body.substring(previousEnd));
    }

    private static String decodeEncodedWord(EncodedWord word) {
        if (word.encoding.equals("Q")) {
            return decodeQ(word.encodedText, word.charset);
        } else if (word.encoding.equals("B")) {
            return DecoderUtil.decodeB(word.encodedText, word.charset);
        } else {
            Timber.w("Warning: Unknown encoding '%s'", word.encoding);
            return null;
        }
    }

    private static EncodedWord extractEncodedWord(String body, int begin, int end, Message message) {
        int qm1 = body.indexOf('?', begin + 2);
        if (qm1 == end - 2)
            return null;

        int qm2 = body.indexOf('?', qm1 + 1);
        if (qm2 == end - 2)
            return null;

        String mimeCharset = body.substring(begin + 2, qm1);
        String encoding = body.substring(qm1 + 1, qm2);
        String encodedText = body.substring(qm2 + 1, end - 2);

        String charset;
        try {
            charset = CharsetSupport.fixupCharset(mimeCharset, message);
        } catch (MessagingException e) {
            return null;
        }

        if (encodedText.isEmpty()) {
            Timber.w("Missing encoded text in encoded word: '%s'", body.substring(begin, end));
            return null;
        }

        EncodedWord encodedWord = new EncodedWord();
        encodedWord.charset = charset;
        if (encoding.equalsIgnoreCase("Q")) {
            encodedWord.encoding = "Q";
        } else if (encoding.equalsIgnoreCase("B")) {
            encodedWord.encoding = "B";
        } else {
            Timber.w("Warning: Unknown encoding in encoded word '%s'", body.substring(begin, end));
            return null;
        }
        encodedWord.encodedText = encodedText;
        return encodedWord;
    }
}