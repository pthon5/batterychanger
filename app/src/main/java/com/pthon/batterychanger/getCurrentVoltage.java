package com.pthon.batterychanger;

import android.content.Context;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class getCurrentVoltage {
    public static String get(Context context) {
        Process root = null;
        String voltage = "";
        try {

            try {
                root = Runtime.getRuntime().exec("su -c cat /sys/class/power_supply/Battery/voltage_now");
            } catch (NullPointerException e) {
                try {
                    root = Runtime.getRuntime().exec("su -c cat /sys/class/power_supply/battery/voltage_now");
                } catch (NullPointerException l) {
                    Toast.makeText(context, "ERROR: i can't read info. Maybe, file don't exist.",Toast.LENGTH_LONG).show();
                }
            }
        } catch (IOException e) {
            Toast.makeText(context, "ERROR: u grant root access?.",Toast.LENGTH_LONG).show();
        }

        InputStream outputStream = root.getInputStream();
        InputStreamReader isr = new InputStreamReader(outputStream);
        BufferedReader bufferedReader = new BufferedReader(isr);
        try{
            voltage = bufferedReader.readLine().toString();
        } catch (IOException e) {
            Toast.makeText(context, "ERROR: can't read info.",Toast.LENGTH_LONG).show();
        }
        int voltageReal = Integer.valueOf(voltage) / 1000;
        return String.valueOf(voltageReal);
    }
}
