package com.learning.cyberkom.cyberkomlearningfix.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.learning.cyberkom.cyberkomlearningfix.R;

import bg.devlabs.fullscreenvideoview.FullscreenVideoView;

public class PlayActivity extends AppCompatActivity{

    TextView txtLink, textKuis, textJudul;
    LinearLayout linearLayout;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        Bundle b = getIntent().getExtras();
        String getJudul = b.getString("judulUrl");
        String getName = b.getString("nameUrl");
        String getJudul2 = b.getString("judulUrl2");
        String getName2 = b.getString("nameUrl2");
        String getLinkKuis = b.getString("linkKuis");

        sharedPreferences = getSharedPreferences("Mypref_Learn", Context.MODE_PRIVATE);
        final FullscreenVideoView videoView = findViewById(R.id.video_view);
        textJudul = findViewById(R.id.textJudul);
        linearLayout = findViewById(R.id.linearjudul);
        txtLink = findViewById(R.id.txtKuis);
        textKuis = findViewById(R.id.textKuis);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            textJudul.setVisibility(View.GONE);
        }

        if(getName2 !=null){
            textJudul.setText(getJudul2);
            if (getLinkKuis.equals("null")) {
                txtLink.setText("Tidak ada Kuis");
            }else {
                txtLink.setText(getLinkKuis);
            }
            videoView.videoUrl(getName2);
            videoView.enableAutoStart();
        }else{
            textJudul.setText(getJudul);
            if (getLinkKuis.equals("null")) {
                txtLink.setText("Tidak ada Kuis");
            }else {
                txtLink.setText(getLinkKuis);
            }
            videoView.videoUrl(getName);
            videoView.enableAutoStart();
        }

        txtLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = txtLink.getText().toString();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                v.getContext().startActivity(i);
            }
        });

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            linearLayout.setVisibility(View.GONE);
            textKuis.setVisibility(View.GONE);
            txtLink.setVisibility(View.GONE);
        }else{
            linearLayout.setVisibility(View.VISIBLE);
            textKuis.setVisibility(View.VISIBLE);
            txtLink.setVisibility(View.VISIBLE);
        }
        super.onConfigurationChanged(newConfig);
    }
}
