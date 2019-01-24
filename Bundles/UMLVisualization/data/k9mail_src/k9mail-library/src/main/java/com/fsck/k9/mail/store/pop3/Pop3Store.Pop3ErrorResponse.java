package com.fsck.k9.mail.store.pop3;

import com.fsck.k9.mail.*;

import java.io.*;
import java.net.*;
static class Pop3ErrorResponse extends MessagingException {
        private static final long serialVersionUID = 3672087845857867174L;

        public Pop3ErrorResponse(String message) {
            super(message, true);
        }
    }