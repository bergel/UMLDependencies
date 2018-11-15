package com.fsck.k9.activity;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.fsck.k9.R;
import com.fsck.k9.ui.ContactBadge;
private static class RecipientTokenHolder {
        public final View layoutHeader, layoutItem;
        public final TextView headerName;
        public final TextView headerAddressLabel;
        public final ContactBadge headerPhoto;
        public final View headerRemove;
        public final TextView itemAddress;
        public final TextView itemAddressLabel;
        public final View itemCryptoStatus;
        public final ImageView itemCryptoStatusIcon;
        public final ImageView itemCryptoStatusSimple;


        public RecipientTokenHolder(View view) {
            layoutHeader = view.findViewById(R.id.alternate_container_header);
            layoutItem = view.findViewById(R.id.alternate_container_item);

            headerName = (TextView) view.findViewById(R.id.alternate_header_name);
            headerAddressLabel = (TextView) view.findViewById(R.id.alternate_header_label);
            headerPhoto = (ContactBadge) view.findViewById(R.id.alternate_contact_photo);
            headerRemove = view.findViewById(R.id.alternate_remove);

            itemAddress = (TextView) view.findViewById(R.id.alternate_address);
            itemAddressLabel = (TextView) view.findViewById(R.id.alternate_address_label);
            itemCryptoStatus = view.findViewById(R.id.alternate_crypto_status);
            itemCryptoStatusIcon = (ImageView) view.findViewById(R.id.alternate_crypto_status_icon);

            itemCryptoStatusSimple = (ImageView) view.findViewById(R.id.alternate_crypto_status_simple);
        }

        public void setShowAsHeader(boolean isHeader) {
            layoutHeader.setVisibility(isHeader ? View.VISIBLE : View.GONE);
            layoutItem.setVisibility(isHeader ? View.GONE : View.VISIBLE);
        }
    }