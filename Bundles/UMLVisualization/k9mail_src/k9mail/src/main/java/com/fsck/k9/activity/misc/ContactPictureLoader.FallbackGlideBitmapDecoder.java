package com.fsck.k9.activity.misc;


import android.content.Context;
import android.graphics.Bitmap;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;

import java.io.IOException;
private class FallbackGlideBitmapDecoder implements ResourceDecoder<FallbackGlideParams, Bitmap> {
        private final Context context;

        FallbackGlideBitmapDecoder(Context context) {
            this.context = context;
        }

        @Override
        public Resource<Bitmap> decode(FallbackGlideParams source, int width, int height) throws IOException {
            BitmapPool pool = Glide.get(context).getBitmapPool();
            Bitmap bitmap = pool.getDirty(mPictureSizeInPx, mPictureSizeInPx, Bitmap.Config.ARGB_8888);
            if (bitmap == null) {
                bitmap = Bitmap.createBitmap(mPictureSizeInPx, mPictureSizeInPx, Bitmap.Config.ARGB_8888);
            }
            drawTextAndBgColorOnBitmap(bitmap, source);
            return BitmapResource.obtain(bitmap, pool);
        }

        @Override
        public String getId() {
            return "fallback-photo";
        }
    }