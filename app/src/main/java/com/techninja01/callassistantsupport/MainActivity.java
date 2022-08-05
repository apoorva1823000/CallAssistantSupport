package com.techninja01.callassistantsupport;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.DialogOnAnyDeniedMultiplePermissionsListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static Context context;
    public static MediaPlayer mediaPlayer;
    SwitchCompat service;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        service = findViewById(R.id.serviceSwitch);

        Dexter.withContext(MainActivity.this).withPermissions(Manifest.permission.READ_SMS,Manifest.permission.RECEIVE_SMS,Manifest.permission.FOREGROUND_SERVICE).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                if (multiplePermissionsReport.areAllPermissionsGranted()) {
                    Toast.makeText(MainActivity.this, "All permissions are granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Not all permissions are granted", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
        MultiplePermissionsListener dialogMultiplePermissionsListener =
                DialogOnAnyDeniedMultiplePermissionsListener.Builder
                        .withContext(getApplicationContext())
                        .withTitle("Phone and bluetooth permissions required")
                        .withMessage("This application requires both phone and bluetooth permissions")
                        .withButtonText(android.R.string.ok)
                        .build();

        if(!foregroudServiceRunning()){
            Intent serviceIntent = new Intent(this,ReceiveService.class);
            startForegroundService(serviceIntent);
        }
        Intent serviceIntent = new Intent(this,ReceiveService.class);
        service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(service.isChecked()){
                    startForegroundService(serviceIntent);
                    service.setChecked(true);
                }else{
                    stopService(serviceIntent);
                    service.setChecked(false);
                 }
            }
        });
    }
    public boolean foregroudServiceRunning(){
        ActivityManager activityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo service: activityManager.getRunningServices(Integer.MAX_VALUE)){
            if(ReceiveService.class.getName().equals(service.service.getClassName())){
                return true;
            }
        }
        return false;
    }
}