package com.pthon.batterychanger.ui.home;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import com.pthon.batterychanger.DBworker;
import com.pthon.batterychanger.R;
import com.pthon.batterychanger.checkservice;
import com.pthon.batterychanger.getCurrentVoltage;

import java.io.IOException;

public class HomeFragment extends Fragment {

    //private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_home, container, false);
        final EditText minmv = root.findViewById(R.id.minmv);
        final EditText maxmv = root.findViewById(R.id.maxmv);
        Button btnapply = root.findViewById(R.id.btnapply);
        Button btnset = root.findViewById(R.id.btnSet);
        Button btnstop = root.findViewById(R.id.btnstop);
        Button btnreset = root.findViewById(R.id.btnreset);

        try {
            getActivity().stopService(new Intent(getActivity(), checkservice.class));
        } catch (NullPointerException e) {

        }

        getActivity().startService(new Intent(getActivity(), checkservice.class));
        try {
            String res = DBworker.get(root.getContext());
            String[] resArray = res.split("/");
            minmv.setText(resArray[1]);
            maxmv.setText(resArray[2]);
        } catch (ArrayIndexOutOfBoundsException e) {}


        btnapply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Integer currentVoltage = Integer.parseInt(getCurrentVoltage.get(root.getContext()));
                try {
                    Runtime.getRuntime().exec("su");
                } catch (NullPointerException | IOException e) {

                }
                Integer min = Integer.parseInt(minmv.getText().toString());
                Integer max = Integer.parseInt(maxmv.getText().toString());
                DBworker.put(root.getContext(), "start",String.valueOf(min), String.valueOf(max));
                Toast.makeText(getContext(), "Success! Wait 30 sec.", Toast.LENGTH_SHORT).show();

                try {
                    getActivity().stopService(new Intent(getActivity(), checkservice.class));
                } catch (NullPointerException e) {

                }

                getActivity().startService(new Intent(getActivity(), checkservice.class));

                //Count percent
                //Integer perc = (100 * (currentVoltage - min) / (max - min));


            }
        });

        btnset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    EditText manualPercent = root.findViewById(R.id.manualset);
                    Runtime.getRuntime().exec("su -c dumpsys battery set level " + manualPercent.getText().toString());
                    Toast.makeText(getContext(), "Success: Executed without error", Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Error. Why? Maybe, unsupported OS.", Toast.LENGTH_LONG).show();
                }
            }
        });

        //stopbtn
        btnstop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Stopped.", Toast.LENGTH_SHORT).show();
                DBworker.put(getContext(), "stop","3200", "4200");
            }
        });

        return root;
    }
}
