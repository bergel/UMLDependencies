package com.fsck.k9.mailstore;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import com.fsck.k9.R;

import java.io.File;

/**
 * Manager for different {@link StorageProvider} -classes that abstract access
 * to sd-cards, additional internal memory and other storage-locations.
 */
public static class SamsungGalaxySStorageProvider extends FixedStorageProviderBase {

        public static final String ID = "SamsungGalaxySStorage";

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
            // FIXME
            return "GT-I5800".equals(Build.DEVICE) || "GT-I9000".equals(Build.DEVICE)
                   || "SGH-T959".equals(Build.DEVICE) || "SGH-I897".equals(Build.DEVICE);
        }

        @Override
        protected File computeRoot(Context context) {
            return Environment.getExternalStorageDirectory(); // was: new
            // File("/sdcard")
        }
    }