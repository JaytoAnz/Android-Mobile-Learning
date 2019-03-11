package com.learning.cyberkom.cyberkomlearningfix.views;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.learning.cyberkom.cyberkomlearningfix.R;
import com.learning.cyberkom.cyberkomlearningfix.model.ApiURL;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

/**
 * Created by Jayto on 7/12/2018.
 */

public class RegisterActivity extends AppCompatActivity{

    EditText name, reg_email, password;
    Button register;
    TextView txtpengguna;
    SpotsDialog spotsDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Edit Text
        name = (EditText) findViewById(R.id.name);
        reg_email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        txtpengguna = (TextView) findViewById(R.id.txtpengguna);

        //Button
        register = (Button) findViewById(R.id.btnRegister);
        spotsDialog = new SpotsDialog(this, R.style.Custom);
        spotsDialog.setTitle("waiting..");
        txtpengguna.setText("mahasiswa");

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if (CheckNetwork()) {
                final String usern = name.getText().toString();
                final String email = reg_email.getText().toString();
                final String pass = password.getText().toString();
                if (usern.isEmpty() || email.isEmpty() || pass.isEmpty()) {

                    Toast.makeText(RegisterActivity.this, "Data Masih Kosong", Toast.LENGTH_SHORT).show();

                } else {
                    signup(usern, email, pass);
                }
            }else if (!CheckNetwork()) {
                Toast.makeText(RegisterActivity.this, "Network Disconnected", Toast.LENGTH_SHORT).show();
            }
            }
        });

    }

    public void signup (final String username, final String email, final String password){
        spotsDialog.show();
        ApiURL apiURL = new ApiURL();
        RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);
        requestQueue.getCache().clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, apiURL.getSignUp(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.i("Hitesh", "" + response);
                Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_LONG).show();
                spotsDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                spotsDialog.dismiss();
                Toast.makeText(RegisterActivity.this, "Gagal Mendaftar", Toast.LENGTH_SHORT).show();

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> stringMap = new HashMap<>();
                stringMap.put("username", username);
                stringMap.put("email", email);
                stringMap.put("password", password);
                stringMap.put("level", txtpengguna.getText().toString());
                return stringMap;
            }
        };
        requestQueue.add(stringRequest);
    }

    private boolean CheckNetwork(){
        boolean WIFI = false;
        boolean DATA_MOBILE = false;

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();

        for (NetworkInfo info : networkInfos){
            if(info.getTypeName().equalsIgnoreCase("WIFI"))
                if (info.isConnected())
                    WIFI = true;

            if(info.getTypeName().equalsIgnoreCase("MOBILE"))
                if (info.isConnected())
                    DATA_MOBILE = true;
        }

        return WIFI||DATA_MOBILE;
    }
}
