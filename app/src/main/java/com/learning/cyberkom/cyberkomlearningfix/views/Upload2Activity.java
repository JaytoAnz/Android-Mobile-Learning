package com.learning.cyberkom.cyberkomlearningfix.views;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
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
import com.learning.cyberkom.cyberkomlearningfix.upload.Upload;

import java.util.HashMap;
import java.util.Map;

public class Upload2Activity extends AppCompatActivity {

    private Button buttonChoose2;
    private Button buttonUpload2;
    private TextView textView2;
    private TextView textViewResponse2;
    private TextView textnameJudul;
    private EditText txtJudul2, edit_kuis;
    private static final int SELECT_VIDEO2 = 3;
    private String selectedPath2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_upload2);

        buttonChoose2 = findViewById(R.id.buttonChoose);
        buttonUpload2 = findViewById(R.id.buttonUpload);
        textView2 = findViewById(R.id.textView);
        textViewResponse2 = findViewById(R.id.textViewResponse);
        txtJudul2 = findViewById(R.id.txtJudul1);
        textnameJudul = findViewById(R.id.txtnameJudul);
        edit_kuis = findViewById(R.id.edit_Kuiss);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ActivityCompat.checkSelfPermission(Upload2Activity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);
            }
        }

        Bundle b = getIntent().getExtras();
        if (b != null) {
            String getName = b.getString("nameJudul");
            textnameJudul.setText(getName);
        }

        enableButton();
    }

    private void enableButton(){
        buttonChoose2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseVideo();
            }
        });

        buttonUpload2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = textView2.getText().toString();
                if(value.isEmpty()){
                    Toast.makeText(Upload2Activity.this, "Pilih Dulu Video", Toast.LENGTH_SHORT).show();
                } else {
                    uploadVideo();
                }
            }
        });
    }

    public void chooseVideo() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select a Video "), SELECT_VIDEO2);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_VIDEO2) {
                System.out.println("SELECT_VIDEO");
                Uri selectedImageUri = data.getData();
                selectedPath2 = getPath(selectedImageUri);
                textView2.setText(selectedPath2);
            }
        }
    }

    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
        cursor.close();

        return path;
    }

    private void uploadVideo() {
        class UploadVideo extends AsyncTask<Void, Void, String> {

            ProgressDialog uploading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                uploading = ProgressDialog.show(Upload2Activity.this, "Uploading File", "Please wait...", false, false);
                uploading.setIndeterminate(false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                uploading.dismiss();
                textViewResponse2.setText(Html.fromHtml("<b> <a href='" + s + "'>" + s + "</a></b>"));
                textViewResponse2.setMovementMethod(LinkMovementMethod.getInstance());
                ValidasiSave();
            }

            @Override
            protected String doInBackground(Void... params) {
                Upload u = new Upload();
                String msg = u.uploadVideo(selectedPath2);
                return msg;
            }
        }
        UploadVideo uv = new UploadVideo();
        uv.execute();
    }

    public void ValidasiSave() {
        String nameUrl2 = txtJudul2.getText().toString();
        String videoUrl = textViewResponse2.getText().toString();
        String urlVideo = textView2.getText().toString();
        String nameUrl = textnameJudul.getText().toString();
        String linkKuis = edit_kuis.getText().toString();
        if (nameUrl2.isEmpty()|| urlVideo.isEmpty() || videoUrl.isEmpty() || linkKuis.isEmpty() ) {
            Toast.makeText(Upload2Activity.this, "Data Tidak ada", Toast.LENGTH_SHORT).show();
        } else {
            save(nameUrl2, videoUrl, nameUrl, linkKuis);
        }
    }

    public void save(final String judul2, final String Url, final String judul, final String linkKuis){
        ApiURL apiURL = new ApiURL();
        RequestQueue requestQueue = Volley.newRequestQueue(Upload2Activity.this);
        requestQueue.getCache().clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, apiURL.getSaveVideo2(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(Upload2Activity.this, "Succes, Data Telah Tersimpan", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(Upload2Activity.this, MenuActivity.class);
                intent.putExtra(MenuActivity.FRAG_LEARN, "learfrag");
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Hitesh", "" + error);
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> stringMap = new HashMap<>();
                stringMap.put("nameUrl2", judul2);
                stringMap.put("video2", Url);
                stringMap.put("nameUrl", judul);
                stringMap.put("linkKuis", linkKuis);
                return stringMap;
            }
        };

        requestQueue.add(stringRequest);
    }
}
