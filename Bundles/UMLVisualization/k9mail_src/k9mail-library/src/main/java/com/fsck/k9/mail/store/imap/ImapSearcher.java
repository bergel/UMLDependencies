package com.fsck.k9.mail.store.imap;


import com.fsck.k9.mail.MessagingException;

import java.io.IOException;
import java.util.List;
interface ImapSearcher {
    List<ImapResponse> search() throws IOException, MessagingException;
}