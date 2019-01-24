package com.fsck.k9.service;


import com.fsck.k9.mail.power.TracingPowerManager.TracingWakeLock;

import java.util.concurrent.CountDownLatch;
private static class SleepDatum {
        CountDownLatch latch;
        TracingWakeLock wakeLock;
        long timeout;
        CountDownLatch reacquireLatch;
    }