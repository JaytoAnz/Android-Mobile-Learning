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
import android.view.WindowManager;
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

public class UploadActivity extends AppCompatActivity {

    private Button buttonChoose;
    private Button buttonUpload;
    private Button btn_next;
    private TextView textView;
    private TextView textViewResponse;
    private EditText txtJudul;

    private static final int SELECT_VIDEO = 3;

    private String selectedPath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_upload);

        buttonChoose = (Button) findViewById(R.id.buttonChoose);
        buttonUpload = (Button) findViewById(R.id.buttonUpload);
        btn_next = (Button) findViewById(R.id.btn_next);
        textView = (TextView) findViewById(R.id.textView);
        textViewResponse = (TextView) findViewById(R.id.textViewResponse);
        txtJudul = (EditText) findViewById(R.id.txtJudul1);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);
            }
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        enableButton();
    }

    private void enableButton(){
        buttonChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseVideo();
            }
        });

        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = textView.getText().toString();
                if(value.isEmpty()){
                    Toast.makeText(UploadActivity.this , "Pilih Dulu Video", Toast.LENGTH_SHORT).show();
                }
                else {
                    uploadVideo();

                }
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameUrl = txtJudul.getText().toString();
                String videoUrl = textViewResponse.getText().toString();
                String urlVideo = textView.getText().toString();
                if (nameUrl.isEmpty() || urlVideo.isEmpty() || videoUrl.isEmpty()) {
                    Toast.makeText(UploadActivity.this, "Isi data terlebih dahulu", Toast.LENGTH_SHORT).show();
                } else {
                    String nameJudul = txtJudul.getText().toString();
                    Intent intent = new Intent(UploadActivity.this, Upload2Activity.class);
                    intent.putExtra("nameJudul", nameJudul);
                    startActivity(intent);
                }
            }
        });
    }

    public void chooseVideo() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select a Video "), SELECT_VIDEO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_VIDEO) {
                System.out.println("SELECT_VIDEO");
                Uri selectedImageUri = data.getData();
                selectedPath = getPath(selectedImageUri);
                textView.setText(selectedPath);
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
                uploading = ProgressDialog.show(UploadActivity.this, "Uploading File", "Please wait...", false, false);
                uploading.setIndeterminate(false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                uploading.dismiss();
                textViewResponse.setText(Html.fromHtml("<b> <a href='" + s + "'>" + s + "</a></b>"));
                textViewResponse.setMovementMethod(LinkMovementMethod.getInstance());
//                String respone = textViewResponse.getText().toString();
                ValidasiSave();
            }

            @Override
            protected String doInBackground(Void... params) {
                Upload u = new Upload();
                String msg = u.uploadVideo(selectedPath);
                return msg;
            }
        }
        UploadVideo uv = new UploadVideo();
        uv.execute();
    }

    public void ValidasiSave() {
        String nameUrl = txtJudul.getText().toString();
        String videoUrl = textViewResponse.getText().toString();
        String urlVideo = textView.getText().toString();
        if (urlVideo.isEmpty() || videoUrl.isEmpty()) {
            Toast.makeText(this, "Data Tidak ada", Toast.LENGTH_SHORT).show();
        } else {
            save(nameUrl, videoUrl);
        }
    }

    public void save(final String judul, final String Url){
        ApiURL apiURL = new ApiURL();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.getCache().clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, apiURL.getSaveVideo(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(UploadActivity.this, "Success, Silahkan Next", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i("Hitesh", "" + error);
                Toast.makeText(UploadActivity.this, "" + error, Toast.LENGTH_SHORT).show();

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> stringMap = new HashMap<>();
                stringMap.put("nameUrl", judul);
                stringMap.put("video", Url);
                return stringMap;
            }
        };

        requestQueue.add(stringRequest);
    }
}
