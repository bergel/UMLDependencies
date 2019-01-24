package com.fsck.k9.ui.messageview;


import android.content.Intent;
import android.content.IntentSender;
import com.fsck.k9.view.MessageCryptoDisplayStatus;
public interface MessageCryptoMvpView {
        void redisplayMessage();
        void restartMessageCryptoProcessing();

        void startPendingIntentForCryptoPresenter(IntentSender si, Integer requestCode, Intent fillIntent,
                int flagsMask, int flagValues, int extraFlags) throws IntentSender.SendIntentException;

        void showCryptoInfoDialog(MessageCryptoDisplayStatus displayStatus, boolean hasSecurityWarning);
        void showCryptoConfigDialog();
    }