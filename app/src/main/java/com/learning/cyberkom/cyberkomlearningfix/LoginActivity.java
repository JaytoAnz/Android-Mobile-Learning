package com.learning.cyberkom.cyberkomlearningfix;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.learning.cyberkom.cyberkomlearningfix.model.ApiURL;
import com.learning.cyberkom.cyberkomlearningfix.views.MenuActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Jayto on 8/14/2018.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    EditText namenew, passwordnew;
    TextView textView;
    Button loginnew;
    Spinner spLevel;
    Context context;
    String[] users = {
            "Pilih",
            "dosen",
            "mahasiswa"
    };

    private static final String KEYNAME = "username";
    private static final String KEYPASS = "password";
    private static final String KEYLEVEL = "level";

    private ProgressBar progressBar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Editteks deks
        namenew = (EditText) findViewById(R.id.name);
        passwordnew = (EditText) findViewById(R.id.password);
        textView = (TextView) findViewById(R.id.textViewSign);
        progressBar = (ProgressBar) findViewById(R.id.loadingProgress);

        //Button deks
        loginnew = (Button) findViewById(R.id.btnLogin);

        spLevel = (Spinner) findViewById(R.id.spinLevel);
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this,android.
                R.layout.simple_spinner_dropdown_item ,users);

        spLevel.setAdapter(adapter);

        loginnew.setOnClickListener(this);
        spLevel.setOnItemSelectedListener(this);
    }

    @Override
    public void onClick(View v) {
        if (CheckNetwork()) {
            if (v.getId() == R.id.btnLogin) {
                final String username = namenew.getText().toString().trim();
                final String pass = passwordnew.getText().toString().trim();
                final String level = spLevel.getSelectedItem().toString().trim();

                if (username.equals("") || pass.equals("") || level.equals("Pilih")) {
                    Toast.makeText(LoginActivity.this, "Data Masih Kosong", Toast.LENGTH_SHORT).show();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.VISIBLE);
                    ApiURL apiURL = new ApiURL();
                    RequestQueue requestQueue = Volley.newRequestQueue(this);
                    requestQueue.getCache().clear();
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, apiURL.getSignIn(), new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {

                                JSONObject jsonObject = new JSONObject(response);
                                boolean responsestatus = jsonObject.getBoolean("success");
                                Log.d("COba", " respon : " + responsestatus);
                                if (responsestatus) {
                                    String message = jsonObject.getString("message");
                                    Toast.makeText(LoginActivity.this, "" + message, Toast.LENGTH_SHORT).show();

                                    String id = jsonObject.getString("id");
                                    String name = jsonObject.getString("name");
                                    String level = jsonObject.getString("level");

                                    SharedPreferences preferences = getSharedPreferences("Mypref_Login", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.clear();
                                    editor.putString("idKey", id);
                                    editor.putString("usernameKey", name);
                                    editor.putString("levelKey", level);
                                    editor.commit();

                                    Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                                    finish();
                                    startActivity(intent);

                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    textView.setVisibility(View.GONE);
                                    String message = jsonObject.getString("message");
                                    Toast.makeText(LoginActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("Eror ", String.valueOf(error));
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put(KEYNAME, username);
                            params.put(KEYPASS, pass);
                            params.put(KEYLEVEL, level);

                            return params;
                        }
                    };
                    requestQueue.add(stringRequest);
                }
            }
        }else if (!CheckNetwork()) {
            Toast.makeText(this, "Network Disconnected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int sid=spLevel.getSelectedItemPosition();
        Toast.makeText(getBaseContext(), "" + users[sid],
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
