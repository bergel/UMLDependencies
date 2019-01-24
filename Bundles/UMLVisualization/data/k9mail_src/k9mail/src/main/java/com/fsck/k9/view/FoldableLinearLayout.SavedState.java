package com.fsck.k9.view;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Class representing a LinearLayout that can fold and hide it's content when
 * pressed To use just add the following to your xml layout
 * <pre>{@code
 *<com.fsck.k9.view.FoldableLinearLayout
 *    android:layout_width="wrap_content" android:layout_height="wrap_content"
 *    custom:foldedLabel="@string/TEXT_TO_DISPLAY_WHEN_FOLDED"
 *    custom:unFoldedLabel="@string/TEXT_TO_DISPLAY_WHEN_UNFOLDED">
 *    <include layout="@layout/ELEMENTS_TO_BE_FOLDED"/>
 *</com.fsck.k9.view.FoldableLinearLayout>}
 * </pre>
 */
static class SavedState extends BaseSavedState {

        static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<FoldableLinearLayout.SavedState>() {

            @Override
            public SavedState createFromParcel(Parcel source) {
                return new SavedState(source);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };

        private boolean mFolded;

        private SavedState(Parcel parcel) {
            super(parcel);
            mFolded = (parcel.readInt() == 1);
        }

        private SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(mFolded ? 1 : 0);
        }
    }