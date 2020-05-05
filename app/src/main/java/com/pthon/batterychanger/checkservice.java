package com.pthon.batterychanger;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.pthon.batterychanger.ui.home.HomeFragment;

import java.io.IOException;

@SuppressLint("Registered")
public class checkservice extends Service {

    public Context context = this;
    public Handler handler = null;
    public static Runnable runnable = null;


    @Override
    public void onCreate() {
        //Toast.makeText(this, "pthon.batteryChanger started!", Toast.LENGTH_LONG).show();

        handler = new Handler();
        runnable = new Runnable() {
            public void run() {

                try {
                    String minmax = DBworker.get(context);
                    String[] minmaxArr = minmax.split("/");
                    if (minmaxArr[0].equals("start")) {
                        int min = Integer.parseInt(minmaxArr[1]);
                        int max = Integer.parseInt(minmaxArr[2]);
                        String currentVoltageStr = getCurrentVoltage.get(context);
                        int currentVoltage = Integer.parseInt(currentVoltageStr);
                        int perc = (100 * (currentVoltage - min) / (max - min));
                        if (perc <= 1) {
                            Log.e("E", "<=1");
                        } else {
                            try {
                                Runtime.getRuntime().exec("su -c dumpsys battery set level " + String.valueOf(perc));

                                //charger check
                                fixCharger.check();

                                //Notification
                                //Notification Channel
                                NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                String channel_id = "batterychannel";
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                    NotificationChannel channel = new NotificationChannel(channel_id, "batterychanger",
                                            NotificationManager.IMPORTANCE_LOW);
                                    channel.setDescription("This is batterychanger notification channel");
                                    channel.enableLights(false);
                                    channel.setLightColor(Color.BLUE);
                                    channel.enableVibration(false);
                                    assert nManager != null;
                                    nManager.createNotificationChannel(channel);
                                }

                                NotificationCompat.Builder builder =
                                        new NotificationCompat.Builder(context)
                                                .setSmallIcon(R.drawable.baseline_battery_full_24)
                                                .setContentTitle("Status")
                                                .setStyle(new NotificationCompat.BigTextStyle()
                                                        .bigText("Voltage: "+currentVoltageStr + "\n"
                                                                + "Real level: " + realInfo.check()))
                                                .setPriority(-2)
                                                .setChannelId(channel_id);
                                int NOTIFICATION_ID = 12345;

                                Intent targetIntent = new Intent(context, HomeFragment.class);
                                PendingIntent contentIntent = PendingIntent.getActivity(context, 0, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                                builder.setContentIntent(contentIntent);

                                assert nManager != null;
                                nManager.notify(NOTIFICATION_ID, builder.build());

                                Log.e("e", "Ok");
                            } catch (IOException e) {
                                Log.e("e", "IOException");
                                e.printStackTrace();
                            }
                        }



                        handler.postDelayed(runnable, 2000);
                    }
                } catch (Exception e) {
                    Log.e("Crash", "Service Crash");
                }

            }
        };

        handler.postDelayed(runnable, 2000);
    }

    @Override
    public void onDestroy() {
        /* IF YOU WANT THIS SERVICE KILLED WITH THE APP THEN UNCOMMENT THE FOLLOWING LINE */
        //handler.removeCallbacks(runnable);
        //Toast.makeText(this, "Service stopped", Toast.LENGTH_LONG).show();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startid) {
        //Toast.makeText(this, "Service started by user.", Toast.LENGTH_LONG).show();
    }
}

