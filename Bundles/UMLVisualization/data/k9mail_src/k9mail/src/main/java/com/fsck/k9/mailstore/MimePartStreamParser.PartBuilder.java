package com.fsck.k9.mailstore;


import com.fsck.k9.mail.Body;
import com.fsck.k9.mail.BodyPart;
import com.fsck.k9.mail.Message;
import com.fsck.k9.mail.MessagingException;
import com.fsck.k9.mail.Multipart;
import com.fsck.k9.mail.Part;
import com.fsck.k9.mail.internet.MimeBodyPart;
import com.fsck.k9.mail.internet.MimeMessage;
import com.fsck.k9.mail.internet.MimeMultipart;
import com.fsck.k9.mailstore.util.FileFactory;
import org.apache.commons.io.IOUtils;
import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.parser.ContentHandler;
import org.apache.james.mime4j.stream.BodyDescriptor;
import org.apache.james.mime4j.stream.Field;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;
private static class PartBuilder implements ContentHandler {
        private final FileFactory fileFactory;
        private final MimeBodyPart decryptedRootPart;
        private final Stack<Object> stack = new Stack<>();

        public PartBuilder(FileFactory fileFactory, MimeBodyPart decryptedRootPart)
                throws MessagingException {
            this.fileFactory = fileFactory;
            this.decryptedRootPart = decryptedRootPart;
        }

        @Override
        public void startMessage() throws MimeException {
            if (stack.isEmpty()) {
                stack.push(decryptedRootPart);
            } else {
                Part part = (Part) stack.peek();

                Message innerMessage = new MimeMessage();
                part.setBody(innerMessage);

                stack.push(innerMessage);
            }
        }

        @Override
        public void endMessage() throws MimeException {
            stack.pop();
        }

        @Override
        public void startBodyPart() throws MimeException {
            try {
                Multipart multipart = (Multipart) stack.peek();

                BodyPart bodyPart = new MimeBodyPart();
                multipart.addBodyPart(bodyPart);

                stack.push(bodyPart);
            } catch (MessagingException e) {
                throw new MimeException(e);
            }
        }

        @Override
        public void endBodyPart() throws MimeException {
            stack.pop();
        }

        @Override
        public void startHeader() throws MimeException {
            // Do nothing
        }

        @Override
        public void field(Field parsedField) throws MimeException {
            String name = parsedField.getName();
            String raw = parsedField.getRaw().toString();

            Part part = (Part) stack.peek();
            part.addRawHeader(name, raw);
        }

        @Override
        public void endHeader() throws MimeException {
            // Do nothing
        }

        @Override
        public void preamble(InputStream is) throws MimeException, IOException {
            expect(MimeMultipart.class);
            ByteArrayOutputStream preamble = new ByteArrayOutputStream();
            IOUtils.copy(is, preamble);
            ((MimeMultipart)stack.peek()).setPreamble(preamble.toByteArray());
        }

        @Override
        public void epilogue(InputStream is) throws MimeException, IOException {
            expect(MimeMultipart.class);
            ByteArrayOutputStream epilogue = new ByteArrayOutputStream();
            IOUtils.copy(is, epilogue);
            ((MimeMultipart) stack.peek()).setEpilogue(epilogue.toByteArray());
        }

        @Override
        public void startMultipart(BodyDescriptor bd) throws MimeException {
            Part part = (Part) stack.peek();

            String mimeType = bd.getMimeType();
            String boundary = bd.getBoundary();

            MimeMultipart multipart = new MimeMultipart(mimeType, boundary);
            part.setBody(multipart);

            stack.push(multipart);
        }

        @Override
        public void endMultipart() throws MimeException {
            stack.pop();
        }

        @Override
        public void body(BodyDescriptor bd, InputStream inputStream) throws MimeException, IOException {
            Part part = (Part) stack.peek();

            String transferEncoding = bd.getTransferEncoding();
            Body body = createBody(inputStream, transferEncoding, fileFactory);

            part.setBody(body);
        }

        @Override
        public void raw(InputStream is) throws MimeException, IOException {
            throw new IllegalStateException("Not implemented");
        }

        private void expect(Class<?> c) {
            if (!c.isInstance(stack.peek())) {
                throw new IllegalStateException("Internal stack error: " + "Expected '"
                        + c.getName() + "' found '" + stack.peek().getClass().getName() + "'");
            }
        }
    }