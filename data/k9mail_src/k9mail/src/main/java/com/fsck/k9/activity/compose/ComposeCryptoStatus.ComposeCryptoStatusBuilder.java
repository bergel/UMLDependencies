package com.fsck.k9.activity.compose;


import com.fsck.k9.activity.compose.RecipientPresenter.CryptoMode;
import com.fsck.k9.activity.compose.RecipientPresenter.CryptoProviderState;
import com.fsck.k9.view.RecipientSelectView.Recipient;

import java.util.ArrayList;
import java.util.List;

/** This is an immutable object which contains all relevant metadata entered
 * during e-mail composition to apply cryptographic operations before sending
 * or saving as draft.
 */
public static class ComposeCryptoStatusBuilder {

        private CryptoProviderState cryptoProviderState;
        private CryptoMode cryptoMode;
        private Long openPgpKeyId;
        private List<Recipient> recipients;
        private Boolean enablePgpInline;
        private Boolean preferEncryptMutual;

        public ComposeCryptoStatusBuilder setCryptoProviderState(CryptoProviderState cryptoProviderState) {
            this.cryptoProviderState = cryptoProviderState;
            return this;
        }

        public ComposeCryptoStatusBuilder setCryptoMode(CryptoMode cryptoMode) {
            this.cryptoMode = cryptoMode;
            return this;
        }

        public ComposeCryptoStatusBuilder setOpenPgpKeyId(Long openPgpKeyId) {
            this.openPgpKeyId = openPgpKeyId;
            return this;
        }

        public ComposeCryptoStatusBuilder setRecipients(List<Recipient> recipients) {
            this.recipients = recipients;
            return this;
        }

        public ComposeCryptoStatusBuilder setEnablePgpInline(boolean cryptoEnableCompat) {
            this.enablePgpInline = cryptoEnableCompat;
            return this;
        }

        public ComposeCryptoStatusBuilder setPreferEncryptMutual(boolean preferEncryptMutual) {
            this.preferEncryptMutual = preferEncryptMutual;
            return this;
        }

        public ComposeCryptoStatus build() {
            if (cryptoProviderState == null) {
                throw new AssertionError("cryptoProviderState must be set!");
            }
            if (cryptoMode == null) {
                throw new AssertionError("crypto mode must be set!");
            }
            if (recipients == null) {
                throw new AssertionError("recipients must be set!");
            }
            if (enablePgpInline == null) {
                throw new AssertionError("enablePgpInline must be set!");
            }
            if (preferEncryptMutual == null) {
                throw new AssertionError("preferEncryptMutual must be set!");
            }

            ArrayList<String> recipientAddresses = new ArrayList<>();
            for (Recipient recipient : recipients) {
                recipientAddresses.add(recipient.address.getAddress());
            }

            ComposeCryptoStatus result = new ComposeCryptoStatus();
            result.cryptoProviderState = cryptoProviderState;
            result.cryptoMode = cryptoMode;
            result.recipientAddresses = recipientAddresses.toArray(new String[0]);
            result.openPgpKeyId = openPgpKeyId;
            result.enablePgpInline = enablePgpInline;
            result.preferEncryptMutual = preferEncryptMutual;
            return result;
        }
    }