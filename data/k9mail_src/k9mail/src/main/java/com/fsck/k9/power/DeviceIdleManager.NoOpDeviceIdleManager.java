package com.fsck.k9.power;


static class NoOpDeviceIdleManager extends DeviceIdleManager {
        @Override
        public void registerReceiver() {
            // Do nothing
        }

        @Override
        public void unregisterReceiver() {
            // Do nothing
        }
    }