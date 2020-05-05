package com.pthon.batterychanger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class fixCharger {
    public static void check() throws IOException {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("su -c cat /sys/devices/platform/huawei_charger/enable_charger");
            InputStream outputStream = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(outputStream);
            BufferedReader bufferedReader = new BufferedReader(isr);
            int status = Integer.parseInt(bufferedReader.readLine());

            if (status == 1) {
                Runtime.getRuntime().exec("su -c dumpsys battery set ac 1");
                Runtime.getRuntime().exec("su -c dumpsys battery set status 2");
            }
            if (status == 0) {
                Runtime.getRuntime().exec("su -c dumpsys battery set ac 0");
                Runtime.getRuntime().exec("su -c dumpsys battery set status 1");
            }
        } catch (Exception e) {
            try {
                process = Runtime.getRuntime().exec("su -c cat /sys/class/hw_power/charger/charge_data/enable_charger");
                InputStream outputStream = process.getInputStream();
                InputStreamReader isr = new InputStreamReader(outputStream);
                BufferedReader bufferedReader = new BufferedReader(isr);
                int status = Integer.parseInt(bufferedReader.readLine());

                if (status == 1) {
                    Runtime.getRuntime().exec("su -c dumpsys battery set ac 1");
                    Runtime.getRuntime().exec("su -c dumpsys battery set status 2");
                }
                if (status == 0) {
                    Runtime.getRuntime().exec("su -c dumpsys battery set ac 0");
                    Runtime.getRuntime().exec("su -c dumpsys battery set status 1");
                }
            } catch (Exception g) {

            }
        }
    }
}
