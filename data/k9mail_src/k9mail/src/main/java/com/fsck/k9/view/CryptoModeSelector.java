package com.fsck.k9.view;
public interface CryptoModeSelector {
    void setCryptoStatusListener(CryptoStatusSelectedListener cryptoStatusListener);
    void setCryptoStatus(CryptoModeSelectorState status);

    

    
}