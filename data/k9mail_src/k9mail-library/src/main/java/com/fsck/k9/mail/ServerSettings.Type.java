package com.fsck.k9.mail;

/**
 * This is an abstraction to get rid of the store- and transport-specific URIs.
 *
 * <p>
 * Right now it's only used for settings import/export. But the goal is to get rid of
 * store/transport URIs altogether.
 * </p>
 *
 * @see com.fsck.k9.mail.store.StoreConfig#getStoreUri()
 * @see com.fsck.k9.mail.store.StoreConfig#getTransportUri()
 */
public enum Type {

        IMAP(143, 993),
        SMTP(587, 465),
        WebDAV(80, 443),
        POP3(110, 995);

        public final int defaultPort;

        /**
         * Note: port for connections using TLS (=SSL) immediately
         * from the initial TCP connection.
         *
         * STARTTLS uses the defaultPort, then upgrades.
         *
         * See https://www.fastmail.com/help/technical/ssltlsstarttls.html.
         */
        public final int defaultTlsPort;

        private Type(int defaultPort, int defaultTlsPort) {
            this.defaultPort = defaultPort;
            this.defaultTlsPort = defaultTlsPort;
        }
    }