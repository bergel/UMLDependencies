package com.fsck.k9.activity;

import com.fsck.k9.activity.misc.SwipeGestureDetector.OnSwipeGestureListener;


/**
 * This class implements functionality common to most activities used in K-9 Mail.
 *
 * @see K9Activity
 * @see K9ListActivity
 */
public interface K9ActivityMagic {
        void setupGestureDetector(OnSwipeGestureListener listener);
    }