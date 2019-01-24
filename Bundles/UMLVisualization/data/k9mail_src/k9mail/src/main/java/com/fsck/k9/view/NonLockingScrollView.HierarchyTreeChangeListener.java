/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.fsck.k9.view;

import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ScrollView;

/**
 * A {@link ScrollView} that will never lock scrolling in a particular direction.
 *
 * Usually ScrollView will capture all touch events once a drag has begun. In some cases,
 * we want to delegate those touches to children as normal, even in the middle of a drag. This is
 * useful when there are childviews like a WebView that handles scrolling in the horizontal direction
 * even while the ScrollView drags vertically.
 *
 * This is only tested to work for ScrollViews where the content scrolls in one direction.
 */
class HierarchyTreeChangeListener implements OnHierarchyChangeListener {
        @Override
        public void onChildViewAdded(View parent, View child) {
            if (child instanceof WebView) {
                mChildrenNeedingAllTouches.add(child);                
            } else if (child instanceof ViewGroup) {
                ViewGroup childGroup = (ViewGroup) child;
                childGroup.setOnHierarchyChangeListener(this);
                for (int i = 0, childCount = childGroup.getChildCount(); i < childCount; i++) {
                    onChildViewAdded(childGroup, childGroup.getChildAt(i));
                }
            }
        }

        @Override
        public void onChildViewRemoved(View parent, View child) {
            if (child instanceof WebView) {
                mChildrenNeedingAllTouches.remove(child);
            } else if (child instanceof ViewGroup) {
                ViewGroup childGroup = (ViewGroup) child;
                for (int i = 0, childCount = childGroup.getChildCount(); i < childCount; i++) {
                    onChildViewRemoved(childGroup, childGroup.getChildAt(i));
                }
                childGroup.setOnHierarchyChangeListener(null);
            }
        }
    }