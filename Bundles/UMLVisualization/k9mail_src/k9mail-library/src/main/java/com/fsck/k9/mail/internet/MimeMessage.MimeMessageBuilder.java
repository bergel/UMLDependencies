package com.fsck.k9.mail.internet;


import com.fsck.k9.mail.Body;
import com.fsck.k9.mail.BodyFactory;
import com.fsck.k9.mail.BodyPart;
import com.fsck.k9.mail.MessagingException;
import com.fsck.k9.mail.Multipart;
import com.fsck.k9.mail.Part;
import org.apache.commons.io.IOUtils;
import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.parser.ContentHandler;
import org.apache.james.mime4j.stream.BodyDescriptor;
import org.apache.james.mime4j.stream.Field;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;


/**
 * An implementation of Message that stores all of it's metadata in RFC 822 and
 * RFC 2045 style headers.
 */
private class MimeMessageBuilder implements ContentHandler {
        private final LinkedList<Object> stack = new LinkedList<>();
        private final BodyFactory bodyFactory;

        public MimeMessageBuilder(BodyFactory bodyFactory) {
            this.bodyFactory = bodyFactory;
        }

        private void expect(Class<?> c) {
            if (!c.isInstance(stack.peek())) {
                throw new IllegalStateException("Internal stack error: " + "Expected '"
                                                + c.getName() + "' found '" + stack.peek().getClass().getName() + "'");
            }
        }

        @Override
        public void startMessage() {
            if (stack.isEmpty()) {
                stack.addFirst(MimeMessage.this);
            } else {
                expect(Part.class);
                Part part = (Part) stack.peek();

                MimeMessage m = new MimeMessage();
                part.setBody(m);
                stack.addFirst(m);
            }
        }

        @Override
        public void endMessage() {
            expect(MimeMessage.class);
            stack.removeFirst();
        }

        @Override
        public void startHeader() {
            expect(Part.class);
        }

        @Override
        public void endHeader() {
            expect(Part.class);
        }

        @Override
        public void startMultipart(BodyDescriptor bd) throws MimeException {
            expect(Part.class);

            Part e = (Part)stack.peek();
            String mimeType = bd.getMimeType();
            String boundary = bd.getBoundary();
            MimeMultipart multiPart = new MimeMultipart(mimeType, boundary);
            e.setBody(multiPart);
            stack.addFirst(multiPart);
        }

        @Override
        public void body(BodyDescriptor bd, InputStream in) throws IOException, MimeException {
            expect(Part.class);
            Body body = bodyFactory.createBody(bd.getTransferEncoding(), bd.getMimeType(), in);
            ((Part)stack.peek()).setBody(body);
        }

        @Override
        public void endMultipart() {
            expect(Multipart.class);
            Multipart multipart = (Multipart) stack.removeFirst();

            boolean hasNoBodyParts = multipart.getCount() == 0;
            boolean hasNoEpilogue = multipart.getEpilogue() == null;
            if (hasNoBodyParts && hasNoEpilogue) {
                /*
                 * The parser is calling startMultipart(), preamble(), and endMultipart() when all we have is
                 * headers of a "multipart/*" part. But there's really no point in keeping a Multipart body if all
                 * of the content is missing.
                 */
                expect(Part.class);
                Part part = (Part) stack.peek();
                part.setBody(null);
            }
        }

        @Override
        public void startBodyPart() throws MimeException {
            expect(MimeMultipart.class);

            try {
                MimeBodyPart bodyPart = new MimeBodyPart();
                ((MimeMultipart)stack.peek()).addBodyPart(bodyPart);
                stack.addFirst(bodyPart);
            } catch (MessagingException me) {
                throw new MimeException(me);
            }
        }

        @Override
        public void endBodyPart() {
            expect(BodyPart.class);
            stack.removeFirst();
        }

        @Override
        public void preamble(InputStream is) throws IOException {
            expect(MimeMultipart.class);
            ByteArrayOutputStream preamble = new ByteArrayOutputStream();
            IOUtils.copy(is, preamble);
            ((MimeMultipart)stack.peek()).setPreamble(preamble.toByteArray());
        }

        @Override
        public void epilogue(InputStream is) throws IOException {
            expect(MimeMultipart.class);
            ByteArrayOutputStream epilogue = new ByteArrayOutputStream();
            IOUtils.copy(is, epilogue);
            ((MimeMultipart) stack.peek()).setEpilogue(epilogue.toByteArray());
        }

        @Override
        public void raw(InputStream is) throws IOException {
            throw new UnsupportedOperationException("Not supported");
        }

        @Override
        public void field(Field parsedField) throws MimeException {
            expect(Part.class);
            String name = parsedField.getName();
            String raw = parsedField.getRaw().toString();
            ((Part) stack.peek()).addRawHeader(name, raw);
        }
    }