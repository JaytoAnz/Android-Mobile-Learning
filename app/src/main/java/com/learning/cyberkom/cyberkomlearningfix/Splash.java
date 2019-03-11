package com.learning.cyberkom.cyberkomlearningfix;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.learning.cyberkom.cyberkomlearningfix.views.MenuActivity;

/**
 * Created by Jayto on 8/19/2018.
 */

public class Splash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        Thread thread = new Thread() {

            @Override
            public void run() {
                try {
                    sleep(3000);
                    SharedPreferences shared = getSharedPreferences("Mypref_Login", Context.MODE_PRIVATE);
                    String val = shared.getString("usernameKey", "");
                    if (val.length() == 0) {

                        Intent intent = new Intent(Splash.this, LoginActivity.class);
                        finish();
                        startActivity(intent);


                    } else {
                        Intent intent = new Intent(Splash.this, MenuActivity.class);
                        finish();
                        startActivity(intent);

                    }


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        };

        thread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
