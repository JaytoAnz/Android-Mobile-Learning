package com.learning.cyberkom.cyberkomlearningfix.views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

public class NilaiActivity extends AppCompatActivity {

    TextView textView, textView1;
    EditText textLink;
    Button btn_simpan;
    ProgressDialog uploading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nilai);

        textView = (TextView) findViewById(R.id.textJudulM);
        textView1 = (TextView) findViewById(R.id.textVieww);

        textLink = (EditText) findViewById(R.id.txtLinkNilai);

        btn_simpan = (Button) findViewById(R.id.btn_simpan);

        Bundle b = getIntent().getExtras();
        String getJudul = b.getString("nameJudul");
        textView.setText(getJudul);

        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urlNilai = textLink.getText().toString();
                if(urlNilai.equals("")){
                    Toast.makeText(NilaiActivity.this, "Data Belum Di Inputkan", Toast.LENGTH_SHORT).show();
                }else{
                    SimpanNilai(urlNilai);
                }
            }
        });
    }

    public void SimpanNilai(final String urlNilai){
        ApiURL apiURL = new ApiURL();
        uploading = ProgressDialog.show(this, "", "Please wait...", false, false);
        uploading.setIndeterminate(false);
        RequestQueue requestQueue = Volley.newRequestQueue(NilaiActivity.this);
        requestQueue.getCache().clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, apiURL.getSaveNilai(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.i("Hitesh", "" + response);
                Toast.makeText(NilaiActivity.this, response, Toast.LENGTH_LONG).show();
                uploading.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(NilaiActivity.this, "Gagal Menyimpan", Toast.LENGTH_SHORT).show();
                uploading.dismiss();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> stringMap = new HashMap<>();
                stringMap.put("linkNilai", urlNilai);
                stringMap.put("nameUrl", textView.getText().toString());
                return stringMap;
            }
        };

        requestQueue.add(stringRequest);
    }
}