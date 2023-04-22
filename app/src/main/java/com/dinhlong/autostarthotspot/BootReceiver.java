package com.dinhlong.autostarthotspot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class BootReceiver extends BroadcastReceiver {
    private static final String TAG = BootReceiver.class.getName();

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_BOOT_COMPLETED.equals(action) || Intent.ACTION_LOCKED_BOOT_COMPLETED.equals(action)) {
            Log.d(TAG, "[ACTION_BOOT_COMPLETED] ");
            if (!HotSpotManager.isHotspotOn(context.getApplicationContext())) {
                HotSpotManager.startTethering(context.getApplicationContext());
                Log.w(TAG, "Enable hotSpot");
            } else {
                Log.w(TAG, "Hotspot already started");
            }
        }
    }
}
