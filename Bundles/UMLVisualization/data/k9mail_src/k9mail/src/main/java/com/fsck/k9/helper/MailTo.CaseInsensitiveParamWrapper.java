package com.fsck.k9.helper;


import android.net.Uri;

import java.util.ArrayList;
import java.util.List;
static class CaseInsensitiveParamWrapper {
        private final Uri uri;


        public CaseInsensitiveParamWrapper(Uri uri) {
            this.uri = uri;
        }

        public List<String> getQueryParameters(String key) {
            List<String> params = new ArrayList<String>();
            for (String paramName : uri.getQueryParameterNames()) {
                if (paramName.equalsIgnoreCase(key)) {
                    params.addAll(uri.getQueryParameters(paramName));
                }
            }

            return params;
        }
    }