package com.techninja01.callassistantsupport;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        Dexter.withContext(MainActivity.this).withPermissions(Manifest.permission.READ_SMS,Manifest.permission.RECEIVE_SMS).withListener(new MultiplePermissionsListener() {
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
    }
}