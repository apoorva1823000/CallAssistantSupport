package com.techninja01.callassistantsupport;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class ReceiveService extends Service {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new ReceiveSms();

        final String CHANNEL_ID = "1001";
        NotificationChannel channel =  new NotificationChannel(
                CHANNEL_ID,
                CHANNEL_ID,
                NotificationManager.IMPORTANCE_HIGH
        );
        getSystemService(NotificationManager.class).createNotificationChannel(channel);
        Notification.Builder notification = new Notification.Builder(MainActivity.context,CHANNEL_ID)
                .setContentText("Call Assistant Support has this service running to handle incoming message state")
                .setContentTitle("Call Assistant Support Running")
                .setSmallIcon(R.drawable.ic_launcher_foreground);
        startForeground(1001,notification.build());

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}