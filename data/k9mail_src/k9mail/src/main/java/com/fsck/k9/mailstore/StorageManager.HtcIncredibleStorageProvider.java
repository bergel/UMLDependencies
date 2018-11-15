package com.fsck.k9.mailstore;

import android.content.Context;
import android.os.Build;
import com.fsck.k9.R;

import java.io.File;

/**
 * Manager for different {@link StorageProvider} -classes that abstract access
 * to sd-cards, additional internal memory and other storage-locations.
 */
public static class HtcIncredibleStorageProvider extends FixedStorageProviderBase {

        public static final String ID = "HtcIncredibleStorage";

        @Override
        public String getId() {
            return ID;
        }

        @Override
        public String getName(Context context) {
            return context.getString(R.string.local_storage_provider_samsunggalaxy_label,
                                     Build.MODEL);
        }

        @Override
        protected boolean supportsVendor() {
            return "inc".equals(Build.DEVICE);
        }

        @Override
        protected File computeRoot(Context context) {
            return new File("/emmc");
        }
    }