package com.fsck.k9.mail.store.pop3;

import com.fsck.k9.mail.*;
import com.fsck.k9.mail.ServerSettings.Type;
import com.fsck.k9.mail.ssl.TrustedSocketFactory;
import com.fsck.k9.mail.store.RemoteStore;
import com.fsck.k9.mail.store.StoreConfig;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.fsck.k9.mail.helper.UrlEncodingHelper.decodeUtf8;
import static com.fsck.k9.mail.helper.UrlEncodingHelper.encodeUtf8;
public class Pop3Store extends RemoteStore {

    private static final String STLS_COMMAND = "STLS";
    private static final String USER_COMMAND = "USER";
    private static final String PASS_COMMAND = "PASS";
    private static final String CAPA_COMMAND = "CAPA";
    private static final String AUTH_COMMAND = "AUTH";
    private static final String STAT_COMMAND = "STAT";
    private static final String LIST_COMMAND = "LIST";
    private static final String UIDL_COMMAND = "UIDL";
    private static final String TOP_COMMAND = "TOP";
    private static final String RETR_COMMAND = "RETR";
    private static final String DELE_COMMAND = "DELE";
    private static final String QUIT_COMMAND = "QUIT";

    private static final String STLS_CAPABILITY = "STLS";
    private static final String UIDL_CAPABILITY = "UIDL";
    private static final String TOP_CAPABILITY = "TOP";
    private static final String SASL_CAPABILITY = "SASL";
    private static final String AUTH_PLAIN_CAPABILITY = "PLAIN";
    private static final String AUTH_CRAM_MD5_CAPABILITY = "CRAM-MD5";
    private static final String AUTH_EXTERNAL_CAPABILITY = "EXTERNAL";

    /**
     * Decodes a Pop3Store URI.
     *
     * <p>Possible forms:</p>
     * <pre>
     * pop3://authType:user:password@server:port
     *      ConnectionSecurity.NONE
     * pop3+tls+://authType:user:password@server:port
     *      ConnectionSecurity.STARTTLS_REQUIRED
     * pop3+ssl+://authType:user:password@server:port
     *      ConnectionSecurity.SSL_TLS_REQUIRED
     * </pre>
     * 
     * e.g.
     * <pre>pop3://PLAIN:admin:pass123@example.org:12345</pre>
     */
    public static ServerSettings decodeUri(String uri) {
        String host;
        int port;
        ConnectionSecurity connectionSecurity;
        String username = null;
        String password = null;
        String clientCertificateAlias = null;

        URI pop3Uri;
        try {
            pop3Uri = new URI(uri);
        } catch (URISyntaxException use) {
            throw new IllegalArgumentException("Invalid Pop3Store URI", use);
        }

        String scheme = pop3Uri.getScheme();
        /*
         * Currently available schemes are:
         * pop3
         * pop3+tls+
         * pop3+ssl+
         *
         * The following are obsolete schemes that may be found in pre-existing
         * settings from earlier versions or that may be found when imported. We
         * continue to recognize them and re-map them appropriately:
         * pop3+tls
         * pop3+ssl
         */
        if (scheme.equals("pop3")) {
            connectionSecurity = ConnectionSecurity.NONE;
            port = Type.POP3.defaultPort;
        } else if (scheme.startsWith("pop3+tls")) {
            connectionSecurity = ConnectionSecurity.STARTTLS_REQUIRED;
            port = Type.POP3.defaultPort;
        } else if (scheme.startsWith("pop3+ssl")) {
            connectionSecurity = ConnectionSecurity.SSL_TLS_REQUIRED;
            port = Type.POP3.defaultTlsPort;
        } else {
            throw new IllegalArgumentException("Unsupported protocol (" + scheme + ")");
        }

        host = pop3Uri.getHost();

        if (pop3Uri.getPort() != -1) {
            port = pop3Uri.getPort();
        }

        AuthType authType = AuthType.PLAIN;
        if (pop3Uri.getUserInfo() != null) {
            int userIndex = 0, passwordIndex = 1;
            String userinfo = pop3Uri.getUserInfo();
            String[] userInfoParts = userinfo.split(":");
            if (userInfoParts.length > 2 || userinfo.endsWith(":") ) {
                // If 'userinfo' ends with ":" the password is empty. This can only happen
                // after an account was imported (so authType and username are present).
                userIndex++;
                passwordIndex++;
                authType = AuthType.valueOf(userInfoParts[0]);
            }
            username = decodeUtf8(userInfoParts[userIndex]);
            if (userInfoParts.length > passwordIndex) {
                if (authType == AuthType.EXTERNAL) {
                    clientCertificateAlias = decodeUtf8(userInfoParts[passwordIndex]);
                } else {
                    password = decodeUtf8(userInfoParts[passwordIndex]);
                }
            }
        }

        return new ServerSettings(ServerSettings.Type.POP3, host, port, connectionSecurity, authType, username,
                password, clientCertificateAlias);
    }

    /**
     * Creates a Pop3Store URI with the supplied settings.
     *
     * @param server
     *         The {@link ServerSettings} object that holds the server settings.
     *
     * @return A Pop3Store URI that holds the same information as the {@code server} parameter.
     *
     * @see StoreConfig#getStoreUri()
     * @see Pop3Store#decodeUri(String)
     */
    public static String createUri(ServerSettings server) {
        String userEnc = encodeUtf8(server.username);
        String passwordEnc = (server.password != null) ?
                    encodeUtf8(server.password) : "";
        String clientCertificateAliasEnc = (server.clientCertificateAlias != null) ?
                    encodeUtf8(server.clientCertificateAlias) : "";

        String scheme;
        switch (server.connectionSecurity) {
            case SSL_TLS_REQUIRED:
                scheme = "pop3+ssl+";
                break;
            case STARTTLS_REQUIRED:
                scheme = "pop3+tls+";
                break;
            default:
            case NONE:
                scheme = "pop3";
                break;
        }

        AuthType authType = server.authenticationType;
        String userInfo;
        if (AuthType.EXTERNAL == authType) {
            userInfo = authType.name() + ":" + userEnc + ":" + clientCertificateAliasEnc;
        } else {
            userInfo = authType.name() + ":" + userEnc + ":" + passwordEnc;
        }

        try {
            return new URI(scheme, userInfo, server.host, server.port, null, null,
                    null).toString();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Can't create Pop3Store URI", e);
        }
    }


    private String mHost;
    private int mPort;
    private String mUsername;
    private String mPassword;
    private String mClientCertificateAlias;
    private AuthType mAuthType;
    private ConnectionSecurity mConnectionSecurity;
    private Map<String, Folder> mFolders = new HashMap<String, Folder>();
    private Pop3Capabilities mCapabilities;

    /**
     * This value is {@code true} if the server supports the CAPA command but doesn't advertise
     * support for the TOP command OR if the server doesn't support the CAPA command and we
     * already unsuccessfully tried to use the TOP command.
     */
    private boolean mTopNotSupported;


    public Pop3Store(StoreConfig storeConfig, TrustedSocketFactory socketFactory) throws MessagingException {
        super(storeConfig, socketFactory);

        ServerSettings settings;
        try {
            settings = decodeUri(storeConfig.getStoreUri());
        } catch (IllegalArgumentException e) {
            throw new MessagingException("Error while decoding store URI", e);
        }

        mHost = settings.host;
        mPort = settings.port;

        mConnectionSecurity = settings.connectionSecurity;

        mUsername = settings.username;
        mPassword = settings.password;
        mClientCertificateAlias = settings.clientCertificateAlias;
        mAuthType = settings.authenticationType;
    }

    @Override
    public Folder getFolder(String name) {
        Folder folder = mFolders.get(name);
        if (folder == null) {
            folder = new Pop3Folder(name);
            mFolders.put(folder.getName(), folder);
        }
        return folder;
    }

    @Override
    public List <? extends Folder > getPersonalNamespaces(boolean forceListAll) throws MessagingException {
        List<Folder> folders = new LinkedList<Folder>();
        folders.add(getFolder(mStoreConfig.getInboxFolderName()));
        return folders;
    }

    @Override
    public void checkSettings() throws MessagingException {
        Pop3Folder folder = new Pop3Folder(mStoreConfig.getInboxFolderName());
        try {
            folder.open(Folder.OPEN_MODE_RW);
            if (!mCapabilities.uidl) {
            /*
             * Run an additional test to see if UIDL is supported on the server. If it's not we
             * can't service this account.
             */

            /*
             * If the server doesn't support UIDL it will return a - response, which causes
             * executeSimpleCommand to throw a MessagingException, exiting this method.
             */
                folder.executeSimpleCommand(UIDL_COMMAND);

            }
        }
        finally {
            folder.close();
        }
    }

    @Override
    public boolean isSeenFlagSupported() {
        return false;
    }

    //Pop3Folder

    

    

    

    /**
     * Exception that is thrown if the server returns an error response.
     */
    
}