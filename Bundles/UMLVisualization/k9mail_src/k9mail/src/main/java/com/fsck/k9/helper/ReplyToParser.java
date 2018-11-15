package com.fsck.k9.helper;


import com.fsck.k9.Account;
import com.fsck.k9.mail.Address;
import com.fsck.k9.mail.Message;
import com.fsck.k9.mail.Message.RecipientType;
import com.fsck.k9.mail.internet.ListHeaders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
public class ReplyToParser {

    public ReplyToAddresses getRecipientsToReplyTo(Message message, Account account) {
        Address[] candidateAddress;

        Address[] replyToAddresses = message.getReplyTo();
        Address[] listPostAddresses = ListHeaders.getListPostAddresses(message);
        Address[] fromAddresses = message.getFrom();

        if (replyToAddresses.length > 0) {
            candidateAddress = replyToAddresses;
        } else if (listPostAddresses.length > 0) {
            candidateAddress = listPostAddresses;
        } else {
            candidateAddress = fromAddresses;
        }

        boolean replyToAddressIsUserIdentity = account.isAnIdentity(candidateAddress);
        if (replyToAddressIsUserIdentity) {
            candidateAddress = message.getRecipients(RecipientType.TO);
        }

        return new ReplyToAddresses(candidateAddress);
    }

    public ReplyToAddresses getRecipientsToReplyAllTo(Message message, Account account) {
        List<Address> replyToAddresses = Arrays.asList(getRecipientsToReplyTo(message, account).to);

        HashSet<Address> alreadyAddedAddresses = new HashSet<>(replyToAddresses);
        ArrayList<Address> toAddresses = new ArrayList<>(replyToAddresses);
        ArrayList<Address> ccAddresses = new ArrayList<>();

        for (Address address : message.getFrom()) {
            if (!alreadyAddedAddresses.contains(address) && !account.isAnIdentity(address)) {
                toAddresses.add(address);
                alreadyAddedAddresses.add(address);
            }
        }

        for (Address address : message.getRecipients(RecipientType.TO)) {
            if (!alreadyAddedAddresses.contains(address) && !account.isAnIdentity(address)) {
                toAddresses.add(address);
                alreadyAddedAddresses.add(address);
            }
        }

        for (Address address : message.getRecipients(RecipientType.CC)) {
            if (!alreadyAddedAddresses.contains(address) && !account.isAnIdentity(address)) {
                ccAddresses.add(address);
                alreadyAddedAddresses.add(address);
            }
        }

        return new ReplyToAddresses(toAddresses, ccAddresses);
    }

    

}