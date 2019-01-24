package com.fsck.k9.activity.misc;


import com.bumptech.glide.Priority;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.ModelLoader;
private class FallbackGlideModelLoader implements ModelLoader<FallbackGlideParams, FallbackGlideParams> {
        @Override
        public DataFetcher<FallbackGlideParams> getResourceFetcher(final FallbackGlideParams model, int width,
                int height) {

            return new DataFetcher<FallbackGlideParams>() {

                @Override
                public FallbackGlideParams loadData(Priority priority) throws Exception {
                    return model;
                }

                @Override
                public void cleanup() {

                }

                @Override
                public String getId() {
                    return model.getId();
                }

                @Override
                public void cancel() {

                }
            };
        }
    }