package com.fsck.k9.preferences;


import com.fsck.k9.mail.ConnectionSecurity;
import com.fsck.k9.mail.ServerSettings;

import java.util.Collections;
import java.util.Map;
private static class ImportedServerSettings extends ServerSettings {
        private final ImportedServer importedServer;

        public ImportedServerSettings(ImportedServer server) {
            super(ServerSettings.Type.valueOf(server.type), server.host, convertPort(server.port),
                    convertConnectionSecurity(server.connectionSecurity),
                    server.authenticationType, server.username, server.password,
                    server.clientCertificateAlias);
            importedServer = server;
        }

        @Override
        public Map<String, String> getExtra() {
            return (importedServer.extras != null) ?
                    Collections.unmodifiableMap(importedServer.extras.settings) : null;
        }

        private static int convertPort(String port) {
            try {
                return Integer.parseInt(port);
            } catch (NumberFormatException e) {
                return -1;
            }
        }

        private static ConnectionSecurity convertConnectionSecurity(String connectionSecurity) {
            try {
                /*
                 * TODO:
                 * Add proper settings validation and upgrade capability for server settings.
                 * Once that exists, move this code into a SettingsUpgrader.
                 */
                if ("SSL_TLS_OPTIONAL".equals(connectionSecurity)) {
                    return ConnectionSecurity.SSL_TLS_REQUIRED;
                } else if ("STARTTLS_OPTIONAL".equals(connectionSecurity)) {
                    return ConnectionSecurity.STARTTLS_REQUIRED;
                }
                return ConnectionSecurity.valueOf(connectionSecurity);
            } catch (Exception e) {
                return ConnectionSecurity.SSL_TLS_REQUIRED;
            }
        }
    }