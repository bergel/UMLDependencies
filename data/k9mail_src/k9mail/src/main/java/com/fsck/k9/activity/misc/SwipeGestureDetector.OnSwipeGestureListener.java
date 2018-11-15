package com.fsck.k9.activity.misc;

import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
public interface OnSwipeGestureListener {
        /**
         * Called when a swipe from right to left is handled by {@link MyGestureDetector}.
         *
         * <p>See {@link OnGestureListener#onFling(MotionEvent, MotionEvent, float, float)}
         * for more information on the {@link MotionEvent}s being passed.</p>
         *
         * @param e1
         *         First down motion event that started the fling.
         * @param e2
         *         The move motion event that triggered the current onFling.
         */
        void onSwipeRightToLeft(final MotionEvent e1, final MotionEvent e2);

        /**
         * Called when a swipe from left to right is handled by {@link MyGestureDetector}.
         *
         * <p>See {@link OnGestureListener#onFling(MotionEvent, MotionEvent, float, float)}
         * for more information on the {@link MotionEvent}s being passed.</p>
         *
         * @param e1
         *         First down motion event that started the fling.
         * @param e2
         *         The move motion event that triggered the current onFling.
         */
        void onSwipeLeftToRight(final MotionEvent e1, final MotionEvent e2);
    }