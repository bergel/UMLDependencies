package com.fsck.k9.message;


import android.app.PendingIntent;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import org.openintents.openpgp.OpenPgpError;
import org.openintents.openpgp.util.OpenPgpApi;
import timber.log.Timber;

import java.io.InputStream;
public class AutocryptStatusInteractor {
    private static final AutocryptStatusInteractor INSTANCE = new AutocryptStatusInteractor();

    public static AutocryptStatusInteractor getInstance() {
        return INSTANCE;
    }


    @WorkerThread
    public RecipientAutocryptStatus retrieveCryptoProviderRecipientStatus(
            OpenPgpApi openPgpApi, String[] recipientAddresses) {
        Intent intent = new Intent(OpenPgpApi.ACTION_QUERY_AUTOCRYPT_STATUS);
        intent.putExtra(OpenPgpApi.EXTRA_USER_IDS, recipientAddresses);

        Intent result = openPgpApi.executeApi(intent, (InputStream) null, null);

        switch (result.getIntExtra(OpenPgpApi.RESULT_CODE, OpenPgpApi.RESULT_CODE_ERROR)) {
            case OpenPgpApi.RESULT_CODE_SUCCESS:
                RecipientAutocryptStatusType type = getRecipientAutocryptStatusFromIntent(result);
                PendingIntent pendingIntent = result.getParcelableExtra(OpenPgpApi.RESULT_INTENT);
                return new RecipientAutocryptStatus(type, pendingIntent);

            case OpenPgpApi.RESULT_CODE_ERROR:
                OpenPgpError error = result.getParcelableExtra(OpenPgpApi.RESULT_ERROR);
                if (error != null) {
                    Timber.w("OpenPGP API Error #%s: %s", error.getErrorId(), error.getMessage());
                } else {
                    Timber.w("OpenPGP API Unknown Error");
                }
                return new RecipientAutocryptStatus(RecipientAutocryptStatusType.ERROR, null);
            case OpenPgpApi.RESULT_CODE_USER_INTERACTION_REQUIRED:
                // should never happen, so treat as error!
            default:
                return new RecipientAutocryptStatus(RecipientAutocryptStatusType.ERROR, null);
        }
    }

    @Nullable
    private RecipientAutocryptStatusType getRecipientAutocryptStatusFromIntent(Intent result) {
        boolean allKeysConfirmed = result.getBooleanExtra(OpenPgpApi.RESULT_KEYS_CONFIRMED, false);
        int autocryptStatus =
                result.getIntExtra(OpenPgpApi.RESULT_AUTOCRYPT_STATUS, OpenPgpApi.AUTOCRYPT_STATUS_UNAVAILABLE);

        switch (autocryptStatus) {
            case OpenPgpApi.AUTOCRYPT_STATUS_UNAVAILABLE:
                return RecipientAutocryptStatusType.UNAVAILABLE;
            case OpenPgpApi.AUTOCRYPT_STATUS_DISCOURAGE:
                if (allKeysConfirmed) {
                    return RecipientAutocryptStatusType.DISCOURAGE_CONFIRMED;
                } else {
                    return RecipientAutocryptStatusType.DISCOURAGE_UNCONFIRMED;
                }
            case OpenPgpApi.AUTOCRYPT_STATUS_AVAILABLE:
                if (allKeysConfirmed) {
                    return RecipientAutocryptStatusType.AVAILABLE_CONFIRMED;
                } else {
                    return RecipientAutocryptStatusType.AVAILABLE_UNCONFIRMED;
                }
            case OpenPgpApi.AUTOCRYPT_STATUS_MUTUAL:
                if (allKeysConfirmed) {
                    return RecipientAutocryptStatusType.RECOMMENDED_CONFIRMED;
                } else {
                    return RecipientAutocryptStatusType.RECOMMENDED_UNCONFIRMED;
                }
        }

        throw new IllegalStateException("encountered bad autocrypt status number!");
    }

    

    
}