package com.fsck.k9.view;

import android.widget.ViewAnimator;

/**
 * A {@link ViewAnimator} that animates between two child views using different animations
 * depending on which view is displayed.
 */
public interface OnSwitchCompleteListener {
        /**
         * This method will be called after the switch (including animation) has ended.
         *
         * @param displayedChild
         *         Contains the zero-based index of the child view that is now displayed.
         */
        void onSwitchComplete(int displayedChild);
    }