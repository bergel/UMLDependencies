package com.fsck.k9.mail.store.pop3;

import com.fsck.k9.mail.*;
import com.fsck.k9.mail.internet.MimeMessage;

import java.io.*;
import java.net.*;
import java.util.Collections;
static class Pop3Message extends MimeMessage {
        Pop3Message(String uid, Pop3Folder folder) {
            mUid = uid;
            mFolder = folder;
            mSize = -1;
        }

        public void setSize(int size) {
            mSize = size;
        }

        @Override
        public void setFlag(Flag flag, boolean set) throws MessagingException {
            super.setFlag(flag, set);
            mFolder.setFlags(Collections.singletonList(this), Collections.singleton(flag), set);
        }

        @Override
        public void delete(String trashFolderName) throws MessagingException {
            //  try
            //  {
            //  Poor POP3 users, we can't copy the message to the Trash folder, but they still want a delete
            setFlag(Flag.DELETED, true);
            //   }
//         catch (MessagingException me)
//         {
//          Log.w(LOG_TAG, "Could not delete non-existent message", me);
//         }
        }
    }