/*
 * Copyright (C) 2010 The Android Open Source Project
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

package com.fsck.k9;


import timber.log.Timber;

import java.util.TimerTask;


/**
 * This class used to "throttle" a flow of events.
 *
 * When {@link #onEvent()} is called, it calls the callback in a certain timeout later.
 * Initially {@link #minTimeout} is used as the timeout, but if it gets multiple {@link #onEvent}
 * calls in a certain amount of time, it extends the timeout, until it reaches {@link #maxTimeout}.
 *
 * This class is primarily used to throttle content changed events.
 */
private class MyTimerTask extends TimerTask {
        private boolean mCanceled;

        @Override
        public void run() {
            handler.post(new HandlerRunnable());
        }

        @Override
        public boolean cancel() {
            mCanceled = true;
            return super.cancel();
        }

        private class HandlerRunnable implements Runnable {
            @Override
            public void run() {
                runningTimerTask = null;
                if (!mCanceled) { // This check has to be done on the UI thread.
                    Timber.d("Throttle: [%s] Kicking callback", name);
                    callback.run();
                }
            }
        }
    }