package com.fsck.k9.mail.transport.smtp;


import java.util.List;
private static class CommandResponse {

        private final int replyCode;
        private final List<String> results;

        CommandResponse(int replyCode, List<String> results) {
            this.replyCode = replyCode;
            this.results = results;
        }
    }