package com.fsck.k9.power;


import android.content.Context;
public abstract class DeviceIdleManager {
    private static DeviceIdleManager instance;


    public static synchronized DeviceIdleManager getInstance(Context context) {
        if (instance == null) {
            DozeChecker dozeChecker = new DozeChecker(context);
            if (dozeChecker.isDeviceIdleModeSupported() && !dozeChecker.isAppWhitelisted()) {
                instance = RealDeviceIdleManager.newInstance(context);
            } else {
                instance = new NoOpDeviceIdleManager();
            }
        }
        return instance;
    }

    private DeviceIdleManager() {
    }

    public abstract void registerReceiver();
    public abstract void unregisterReceiver();


    

    
}