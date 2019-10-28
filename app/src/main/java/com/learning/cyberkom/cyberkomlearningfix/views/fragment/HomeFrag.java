package com.learning.cyberkom.cyberkomlearningfix.views.fragment;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.learning.cyberkom.cyberkomlearningfix.R;

public class HomeFrag extends Fragment {

    TextView greeting, slmt, pemb, footer, footer2;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.frag_home, container, false);

        greeting = v.findViewById(R.id.greeting);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);
                return v;
            }
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
                return v;
            }
        }

        SharedPreferences shared = getActivity().getSharedPreferences("Mypref_Login", Context.MODE_PRIVATE);
        String level = shared.getString("levelKey", "");
        String username = shared.getString("usernameKey", "");
        if (level.equals("mentor")) {
//            footer.setVisibility(View.GONE);
//            footer2.setVisibility(View.GONE);
            greeting.setText("Hi, Mr. " +username);
        }else {
            greeting.setText("Hi, " +username);
        }

        return v;
    }
}
