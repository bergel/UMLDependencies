package com.fsck.k9.message.html;


import android.text.Editable;
import android.text.Html.TagHandler;
import org.xml.sax.XMLReader;

/**
 * Contains common routines to convert html to text and vice versa.
 */
public static class ListTagHandler implements TagHandler {
        @Override
        public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
            if (tag.equals("ul")) {
                if (opening) {
                    char lastChar = 0;
                    if (output.length() > 0) {
                        lastChar = output.charAt(output.length() - 1);
                    }
                    if (lastChar != '\n') {
                        output.append("\r\n");
                    }
                } else {
                    output.append("\r\n");
                }
            }

            if (tag.equals("li")) {
                if (opening) {
                    output.append("\tâ¢  ");
                } else {
                    output.append("\r\n");
                }
            }
        }
    }