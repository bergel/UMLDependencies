package com.fsck.k9.activity.compose;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.fsck.k9.R;
private static class RecipientTokenHolder {
        public final TextView name;
        public final TextView email;
        final ImageView photo;
        final View cryptoStatus;
        final ImageView cryptoStatusIcon;
        final ImageView cryptoStatusSimple;


        RecipientTokenHolder(View view) {
            name = (TextView) view.findViewById(R.id.text1);
            email = (TextView) view.findViewById(R.id.text2);
            photo = (ImageView) view.findViewById(R.id.contact_photo);
            cryptoStatus = view.findViewById(R.id.contact_crypto_status);
            cryptoStatusIcon = (ImageView) view.findViewById(R.id.contact_crypto_status_icon);
            cryptoStatusSimple = (ImageView) view.findViewById(R.id.contact_crypto_status_icon_simple);
        }
    }