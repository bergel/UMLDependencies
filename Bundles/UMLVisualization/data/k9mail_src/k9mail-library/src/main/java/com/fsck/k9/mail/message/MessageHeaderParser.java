package com.fsck.k9.mail.message;


import com.fsck.k9.mail.MessagingException;
import com.fsck.k9.mail.Part;
import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.parser.MimeStreamParser;
import org.apache.james.mime4j.stream.MimeConfig;

import java.io.IOException;
import java.io.InputStream;
public class MessageHeaderParser {

    public static void parse(final Part part, InputStream headerInputStream) throws MessagingException {
        MimeStreamParser parser = getMimeStreamParser();
        parser.setContentHandler(new MessageHeaderParserContentHandler(part));

        try {
            parser.parse(headerInputStream);
        } catch (MimeException me) {
            throw new MessagingException("Error parsing headers", me);
        } catch (IOException e) {
            throw new MessagingException("I/O error parsing headers", e);
        }
    }

    private static MimeStreamParser getMimeStreamParser() {
        MimeConfig parserConfig = new MimeConfig.Builder()
                .setMaxHeaderLen(-1)
                .setMaxLineLen(-1)
                .setMaxHeaderCount(-1)
                .build();

        return new MimeStreamParser(parserConfig);
    }

    
}