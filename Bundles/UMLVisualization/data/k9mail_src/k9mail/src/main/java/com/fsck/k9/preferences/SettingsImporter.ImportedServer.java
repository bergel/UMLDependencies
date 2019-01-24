package com.fsck.k9.preferences;


import android.support.annotation.VisibleForTesting;
import com.fsck.k9.mail.AuthType;
@VisibleForTesting
    static class ImportedServer {
        public String type;
        public String host;
        public String port;
        public String connectionSecurity;
        public AuthType authenticationType;
        public String username;
        public String password;
        public String clientCertificateAlias;
        public ImportedSettings extras;
    }