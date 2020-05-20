package com.pthon.batterychanger.ui.notifications;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.pthon.batterychanger.R;

import java.io.IOException;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        TextView mlink = root.findViewById(R.id.textView2);
        if (mlink != null) {
            mlink.setMovementMethod(LinkMovementMethod.getInstance());
        }

        Button startadb = root.findViewById(R.id.startadb);
        Button stopadb = root.findViewById(R.id.stopadb);

        startadb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Runtime.getRuntime().exec("su -c setprop service.adb.tcp.port 5555");
                    Runtime.getRuntime().exec("su -c start adbd");
                    Toast.makeText(getContext(), "Success started port 5555", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Toast.makeText(getContext(), "ERROR: have root?", Toast.LENGTH_SHORT).show();
                }
            }
        });

        stopadb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Runtime.getRuntime().exec("su -c stop adbd");
                    Toast.makeText(getContext(), "Success stopped ADB", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Toast.makeText(getContext(), "ERROR: have root?", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return root;
    }
}
