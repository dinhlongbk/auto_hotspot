package com.dinhlong.autostarthotspot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import com.dinhlong.autostarthotspot.databinding.ActivityMainBinding;
import com.google.dexmaker.stock.ProxyBuilder;

import java.io.File;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 888;

    private ConnectivityManager mConnectivityManager;
    private ActivityMainBinding mMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = mMainBinding.getRoot();
        setContentView(view);
        showWritePermissionSettings(false);
        //requestMultiPermissions();
        init();

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS:
                for (int i = 0; i < permissions.length; i++) {
                    Log.v(TAG, "----onRequestPermissionsResult: " + permissions[i] + "=" + grantResults[i]);
                }
                break;
        }
    }

    private void init() {
        mMainBinding.startHotspotButton.setOnClickListener(view -> {
            HotSpotManager.startTethering(getApplicationContext());
        });

        mMainBinding.stopHotspotButton.setOnClickListener(view -> {
            HotSpotManager.stopTethering(getApplicationContext());
        });
    }

    private void showWritePermissionSettings(boolean force) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (force || !Settings.System.canWrite(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
    }

    private void requestMultiPermissions() {
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_SETTINGS) != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_SETTINGS);
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_WIFI_STATE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
        }
    }




}