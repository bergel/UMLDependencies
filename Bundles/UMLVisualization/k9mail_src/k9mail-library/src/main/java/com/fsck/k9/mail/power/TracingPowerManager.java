package com.fsck.k9.mail.power;


import android.content.Context;
import android.os.PowerManager;
import com.fsck.k9.mail.K9MailLib;
import timber.log.Timber;

import java.util.Timer;
import java.util.concurrent.atomic.AtomicInteger;
public class TracingPowerManager {
    private final static boolean TRACE = false;
    public static AtomicInteger wakeLockId = new AtomicInteger(0);
    PowerManager pm = null;
    private static TracingPowerManager tracingPowerManager;
    private Timer timer = null;

    public static synchronized TracingPowerManager getPowerManager(Context context) {
        Context appContext = context.getApplicationContext();
        if (tracingPowerManager == null) {
            if (K9MailLib.isDebug()) {
                Timber.v("Creating TracingPowerManager");
            }
            tracingPowerManager = new TracingPowerManager(appContext);
        }
        return tracingPowerManager;
    }


    private TracingPowerManager(Context context) {
        pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (TRACE) {
            timer = new Timer();
        }
    }

    public TracingWakeLock newWakeLock(int flags, String tag) {
        return new TracingWakeLock(flags, tag);
    }
    
}