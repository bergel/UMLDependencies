package com.fsck.k9.mail.ssl;

import com.fsck.k9.mail.CertificateChainException;
import org.apache.http.conn.ssl.StrictHostnameVerifier;

import javax.net.ssl.SSLException;
import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
private static class SecureX509TrustManager implements X509TrustManager {
        private static final Map<String, SecureX509TrustManager> mTrustManager =
            new HashMap<String, SecureX509TrustManager>();

        private final String mHost;
        private final int mPort;

        private SecureX509TrustManager(String host, int port) {
            mHost = host;
            mPort = port;
        }

        public synchronized static X509TrustManager getInstance(String host, int port) {
            String key = host + ":" + port;
            SecureX509TrustManager trustManager;
            if (mTrustManager.containsKey(key)) {
                trustManager = mTrustManager.get(key);
            } else {
                trustManager = new SecureX509TrustManager(host, port);
                mTrustManager.put(key, trustManager);
            }

            return trustManager;
        }

        public void checkClientTrusted(X509Certificate[] chain, String authType)
        throws CertificateException {
            defaultTrustManager.checkClientTrusted(chain, authType);
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
            String message = null;
            X509Certificate certificate = chain[0];

            Throwable cause = null;

            try {
                defaultTrustManager.checkServerTrusted(chain, authType);
                new StrictHostnameVerifier().verify(mHost, certificate);
                return;
            } catch (CertificateException e) {
                // cert. chain can't be validated
                message = e.getMessage();
                cause = e;
            } catch (SSLException e) {
                // host name doesn't match certificate
                message = e.getMessage();
                cause = e;
            }

            // Check the local key store if we couldn't verify the certificate using the global
            // key store or if the host name doesn't match the certificate name
            if (!keyStore.isValidCertificate(certificate, mHost, mPort)) {
                throw new CertificateChainException(message, chain, cause);
            }
        }

        public X509Certificate[] getAcceptedIssuers() {
            return defaultTrustManager.getAcceptedIssuers();
        }

    }