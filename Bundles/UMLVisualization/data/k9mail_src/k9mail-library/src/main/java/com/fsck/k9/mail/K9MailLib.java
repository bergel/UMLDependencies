package com.fsck.k9.mail;
public class K9MailLib {
    private static DebugStatus debugStatus = new DefaultDebugStatus();

    private K9MailLib() {
    }

    public static final int PUSH_WAKE_LOCK_TIMEOUT = 60000;
    public static final String IDENTITY_HEADER = "X-K9mail-Identity";

    /**
     * Should K-9 log the conversation it has over the wire with
     * SMTP servers?
     */
    public static boolean DEBUG_PROTOCOL_SMTP = true;

    /**
     * Should K-9 log the conversation it has over the wire with
     * IMAP servers?
     */
    public static boolean DEBUG_PROTOCOL_IMAP = true;

    /**
     * Should K-9 log the conversation it has over the wire with
     * POP3 servers?
     */
    public static boolean DEBUG_PROTOCOL_POP3 = true;

    /**
     * Should K-9 log the conversation it has over the wire with
     * WebDAV servers?
     */
    public static boolean DEBUG_PROTOCOL_WEBDAV = true;

    public static boolean isDebug() {
        return debugStatus.enabled();
    }

    public static boolean isDebugSensitive() {
        return debugStatus.debugSensitive();
    }

    public static void setDebugSensitive(boolean b) {
        if (debugStatus instanceof WritableDebugStatus) {
            ((WritableDebugStatus) debugStatus).setSensitive(b);
        }
    }

    public static void setDebug(boolean b) {
        if (debugStatus instanceof WritableDebugStatus) {
            ((WritableDebugStatus) debugStatus).setEnabled(b);
        }
    }

    

    public static void setDebugStatus(DebugStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("status cannot be null");
        }
        debugStatus = status;
    }

    

    
}