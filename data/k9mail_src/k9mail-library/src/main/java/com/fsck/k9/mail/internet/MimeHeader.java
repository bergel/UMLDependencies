package com.fsck.k9.mail.internet;

import android.support.annotation.NonNull;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.*;
public class MimeHeader implements Cloneable {
    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    public static final String HEADER_CONTENT_TRANSFER_ENCODING = "Content-Transfer-Encoding";
    public static final String HEADER_CONTENT_DISPOSITION = "Content-Disposition";
    public static final String HEADER_CONTENT_ID = "Content-ID";

    private List<Field> mFields = new ArrayList<Field>();
    private String mCharset = null;

    public void clear() {
        mFields.clear();
    }

    public String getFirstHeader(String name) {
        String[] header = getHeader(name);
        if (header.length == 0) {
            return null;
        }
        return header[0];
    }

    public void addHeader(String name, String value) {
        Field field = Field.newNameValueField(name, MimeUtility.foldAndEncode(value));
        mFields.add(field);
    }

    void addRawHeader(String name, String raw) {
        Field field = Field.newRawField(name, raw);
        mFields.add(field);
    }

    public void setHeader(String name, String value) {
        if (name == null || value == null) {
            return;
        }
        removeHeader(name);
        addHeader(name, value);
    }

    @NonNull
    public Set<String> getHeaderNames() {
        Set<String> names = new LinkedHashSet<String>();
        for (Field field : mFields) {
            names.add(field.getName());
        }
        return names;
    }

    @NonNull
    public String[] getHeader(String name) {
        List<String> values = new ArrayList<String>();
        for (Field field : mFields) {
            if (field.getName().equalsIgnoreCase(name)) {
                values.add(field.getValue());
            }
        }
        return values.toArray(new String[values.size()]);
    }

    public void removeHeader(String name) {
        List<Field> removeFields = new ArrayList<Field>();
        for (Field field : mFields) {
            if (field.getName().equalsIgnoreCase(name)) {
                removeFields.add(field);
            }
        }
        mFields.removeAll(removeFields);
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Field field : mFields) {
            if (field.hasRawData()) {
                builder.append(field.getRaw());
            } else {
                writeNameValueField(builder, field);
            }
            builder.append('\r').append('\n');
        }
        return builder.toString();
    }

    public void writeTo(OutputStream out) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out), 1024);
        for (Field field : mFields) {
            if (field.hasRawData()) {
                writer.write(field.getRaw());
            } else {
                writeNameValueField(writer, field);
            }
            writer.write("\r\n");
        }
        writer.flush();
    }

    private void writeNameValueField(BufferedWriter writer, Field field) throws IOException {
        String value = field.getValue();

        if (hasToBeEncoded(value)) {
            Charset charset = null;

            if (mCharset != null) {
                charset = Charset.forName(mCharset);
            }
            value = EncoderUtil.encodeEncodedWord(field.getValue(), charset);
        }

        writer.write(field.getName());
        writer.write(": ");
        writer.write(value);
    }

    private void writeNameValueField(StringBuilder builder, Field field) {
        String value = field.getValue();

        if (hasToBeEncoded(value)) {
            Charset charset = null;

            if (mCharset != null) {
                charset = Charset.forName(mCharset);
            }
            value = EncoderUtil.encodeEncodedWord(field.getValue(), charset);
        }

        builder.append(field.getName());
        builder.append(": ");
        builder.append(value);
    }

    // encode non printable characters except LF/CR/TAB codes.
    private boolean hasToBeEncoded(String text) {
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if ((c < 0x20 || 0x7e < c) && // non printable
                    (c != 0x0a && c != 0x0d && c != 0x09)) { // non LF/CR/TAB
                return true;
            }
        }

        return false;
    }

    

    public void setCharset(String charset) {
        mCharset = charset;
    }

    @Override
    public MimeHeader clone() {
        try {
            MimeHeader header = (MimeHeader) super.clone();
            header.mFields = new ArrayList<Field>(mFields);
            return header;
        } catch(CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }
}