package com.pthon.batterychanger.ui.dashboard;

import android.content.Context;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.tabs.TabLayout;
import com.pthon.batterychanger.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;

    TableLayout tableLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        final Context context = root.getContext();
        final Context applicationContext = context.getApplicationContext();
        Button btnget = root.findViewById(R.id.btnget);
        tableLayout = root.findViewById(R.id.tablelayoutinfo);
        btnget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Integer i = tableLayout.getChildCount();
                    if (i != 1 ) {
                        tableLayout.removeViews(1, i - 1);
                    }
                } catch (NullPointerException e) {}



                try {
                    Process root = null;
                    try {
                        root = Runtime.getRuntime().exec("su -c cat /sys/class/power_supply/Battery/uevent");
                    } catch (NullPointerException e) {
                        try {
                            root = Runtime.getRuntime().exec("su -c cat /sys/class/power_supply/battery/uevent");
                        } catch (NullPointerException l) {
                            Toast.makeText(applicationContext, "ERROR: i can't read info. Maybe, file don't exist.",Toast.LENGTH_LONG).show();
                        }

                    }
                    InputStream outputStream = root.getInputStream();
                    InputStreamReader isr = new InputStreamReader(outputStream);
                    BufferedReader bufferedReader = new BufferedReader(isr);
                    Log.e("DEBUG",bufferedReader.readLine());
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        String[] data;
                        data = line.split("=");
                        switch (data[0]){
                            case "POWER_SUPPLY_TECHNOLOGY":
                                fill(applicationContext, "Technology", data[1]);
                                break;
                            case "POWER_SUPPLY_HEALTH":
                                fill(applicationContext, "Health", data[1]);
                                break;
                            case "POWER_SUPPLY_CYCLE_COUNT":
                                fill(applicationContext, "Cycles", data[1]);
                                break;
                            case "POWER_SUPPLY_CAPACITY":
                                fill(applicationContext, "Level", data[1] + "%");
                                break;
                            case "POWER_SUPPLY_CHARGE_FULL_DESIGN":
                                fill(applicationContext, "Design capacity", data[1]);
                                break;
                            case "POWER_SUPPLY_CHARGE_FULL":
                                fill(applicationContext, "Real capacity", data[1]);
                                break;
                            case "POWER_SUPPLY_BRAND":
                                fill(applicationContext, "Brand", data[1]);
                                break;
                            case "POWER_SUPPLY_VOLTAGE_NOW":
                                fill(applicationContext, "Current voltage", Integer.parseInt(data[1]) / 1000 + "mV");
                                break;
                            case "POWER_SUPPLY_VOLTAGE_MAX":
                                fill(applicationContext, "Max voltage", data[1] + "mV");
                                break;
                            case "POWER_SUPPLY_TEMP":
                                fill(applicationContext, "Current temp", Integer.parseInt(data[1]) / 10 + "°C");
                                break;

                        }
                    }

                } catch (IOException | NullPointerException e) {
                    Toast.makeText(applicationContext, "ERROR: u grant root access?",Toast.LENGTH_LONG).show();

                }
            }
        });

        return root;



    }

    private void fill(Context context, String data, String data1) {

        TableRow tableRow = new TableRow(context);
        TableRow tableRow1 = new TableRow(context);
        TextView tv = new TextView(context);
        TextView tv1 = new TextView(context);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP ,20);
        tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        tv.setText(data);
        tv1.setText(data1);
        tableRow.addView(tv);
        tableRow1.addView(tv1);
        tableRow.addView(tableRow1);
        //tableRow.setMinimumWidth();
        tableRow.setPadding(0,15,0,0);
        tableLayout.addView(tableRow);

    }



}
