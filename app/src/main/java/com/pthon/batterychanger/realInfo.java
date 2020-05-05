package com.pthon.batterychanger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class realInfo {
    public static String check() {
        Process process = null;
        try {
            try {
                process = Runtime.getRuntime().exec("su -c cat /sys/class/power_supply/Battery/capacity");
            } catch (NullPointerException | IOException e) {
                try {
                    process = Runtime.getRuntime().exec("su -c cat /sys/class/power_supply/battery/capacity");
                } catch (NullPointerException | IOException l) {
                    //Toast.makeText(applicationContext, "ERROR: i can't read info. Maybe, file don't exist.",Toast.LENGTH_LONG).show();
                }

            }
            InputStream outputStream = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(outputStream);
            BufferedReader bufferedReader = new BufferedReader(isr);
            return bufferedReader.readLine();
        } catch (NullPointerException | IOException e) {
            return "0";
        }
        //return null;
    }
}
