package com.fsck.k9.mail.ssl;

import timber.log.Timber;

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
public final class TrustManagerFactory {
    private static X509TrustManager defaultTrustManager;

    private static LocalKeyStore keyStore;


    

    static {
        try {
            keyStore = LocalKeyStore.getInstance();

            javax.net.ssl.TrustManagerFactory tmf = javax.net.ssl.TrustManagerFactory.getInstance("X509");
            tmf.init((KeyStore) null);

            TrustManager[] tms = tmf.getTrustManagers();
            if (tms != null) {
                for (TrustManager tm : tms) {
                    if (tm instanceof X509TrustManager) {
                        defaultTrustManager = (X509TrustManager) tm;
                        break;
                    }
                }
            }
        } catch (NoSuchAlgorithmException e) {
            Timber.e(e, "Unable to get X509 Trust Manager ");
        } catch (KeyStoreException e) {
            Timber.e(e, "Key Store exception while initializing TrustManagerFactory");
        }
    }

    private TrustManagerFactory() {
    }

    public static X509TrustManager get(String host, int port) {
        return SecureX509TrustManager.getInstance(host, port);
    }
}