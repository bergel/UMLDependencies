package com.fsck.k9.mailstore;


import com.fsck.k9.mail.Body;
import com.fsck.k9.mail.MessagingException;
import com.fsck.k9.mail.internet.MimeBodyPart;
import com.fsck.k9.mailstore.util.FileFactory;
import org.apache.commons.io.IOUtils;
import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.io.EOLConvertingInputStream;
import org.apache.james.mime4j.parser.MimeStreamParser;
import org.apache.james.mime4j.stream.MimeConfig;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
public class MimePartStreamParser {

    public static MimeBodyPart parse(FileFactory fileFactory, InputStream inputStream)
            throws MessagingException, IOException {
        MimeBodyPart parsedRootPart = new MimeBodyPart();

        MimeConfig parserConfig  = new MimeConfig.Builder()
                .setMaxHeaderLen(-1)
                .setMaxLineLen(-1)
                .setMaxHeaderCount(-1)
                .build();

        MimeStreamParser parser = new MimeStreamParser(parserConfig);
        parser.setContentHandler(new PartBuilder(fileFactory, parsedRootPart));
        parser.setRecurse();

        try {
            parser.parse(new EOLConvertingInputStream(inputStream));
        } catch (MimeException e) {
            throw new MessagingException("Failed to parse decrypted content", e);
        }

        return parsedRootPart;
    }

    private static Body createBody(InputStream inputStream, String transferEncoding,
            FileFactory fileFactory) throws IOException {
        DeferredFileBody body = new DeferredFileBody(fileFactory, transferEncoding);
        OutputStream outputStream = body.getOutputStream();
        try {
            IOUtils.copy(inputStream, outputStream);
        } finally {
            outputStream.close();
        }

        return body;
    }


    
}