package com.fsck.k9.view;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.fsck.k9.R;
private static class RecipientTokenViewHolder {
        final TextView vName;
        final ImageView vContactPhoto;
        final View cryptoStatusRed;
        final View cryptoStatusOrange;
        final View cryptoStatusGreen;
        final View cryptoStatusSimple;
        final View cryptoStatusSimpleEnabled;
        final View cryptoStatusSimpleError;


        RecipientTokenViewHolder(View view) {
            vName = (TextView) view.findViewById(android.R.id.text1);
            vContactPhoto = (ImageView) view.findViewById(R.id.contact_photo);
            cryptoStatusRed = view.findViewById(R.id.contact_crypto_status_red);
            cryptoStatusOrange = view.findViewById(R.id.contact_crypto_status_orange);
            cryptoStatusGreen = view.findViewById(R.id.contact_crypto_status_green);

            cryptoStatusSimple = view.findViewById(R.id.contact_crypto_status_icon_simple);
            cryptoStatusSimpleEnabled = view.findViewById(R.id.contact_crypto_status_icon_simple_enabled);
            cryptoStatusSimpleError = view.findViewById(R.id.contact_crypto_status_icon_simple_error);
        }

        void showSimpleCryptoState(boolean isAvailable, boolean isShowEnabled) {
            cryptoStatusRed.setVisibility(View.GONE);
            cryptoStatusOrange.setVisibility(View.GONE);
            cryptoStatusGreen.setVisibility(View.GONE);

            cryptoStatusSimple.setVisibility(!isShowEnabled && isAvailable ? View.VISIBLE : View.GONE);
            cryptoStatusSimpleEnabled.setVisibility(isShowEnabled && isAvailable ? View.VISIBLE : View.GONE);
            cryptoStatusSimpleError.setVisibility(isShowEnabled && !isAvailable ? View.VISIBLE : View.GONE);
        }

        void showAdvancedCryptoState(boolean isAvailable, boolean isVerified) {
            cryptoStatusRed.setVisibility(!isAvailable ? View.VISIBLE : View.GONE);
            cryptoStatusOrange.setVisibility(isAvailable && !isVerified ? View.VISIBLE : View.GONE);
            cryptoStatusGreen.setVisibility(isAvailable && isVerified ? View.VISIBLE : View.GONE);

            cryptoStatusSimple.setVisibility(View.GONE);
            cryptoStatusSimpleEnabled.setVisibility(View.GONE);
            cryptoStatusSimpleError.setVisibility(View.GONE);
        }

        void hideCryptoState() {
            cryptoStatusRed.setVisibility(View.GONE);
            cryptoStatusOrange.setVisibility(View.GONE);
            cryptoStatusGreen.setVisibility(View.GONE);

            cryptoStatusSimple.setVisibility(View.GONE);
            cryptoStatusSimpleEnabled.setVisibility(View.GONE);
            cryptoStatusSimpleError.setVisibility(View.GONE);
        }
    }