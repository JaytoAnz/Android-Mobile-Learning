package com.learning.cyberkom.cyberkomlearningfix.views.fragment;

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
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import dmax.dialog.SpotsDialog;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Jayto on 8/15/2018.
 */

public class UploadFrag2 extends Fragment {

    private Button buttonChoose2;
    private Button buttonUpload2;
    private TextView textView2;
    private TextView textViewResponse2;
    private TextView textnameJudul;
    private EditText txtJudul2, edit_kuis;
    private static final int SELECT_VIDEO2 = 3;
    private String selectedPath2;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.frag_upload2, container, false);

        buttonChoose2 = (Button) v.findViewById(R.id.buttonChoose);
        buttonUpload2 = (Button) v.findViewById(R.id.buttonUpload);
        textView2 = (TextView) v.findViewById(R.id.textView);
        textViewResponse2 = (TextView) v.findViewById(R.id.textViewResponse);
        txtJudul2 = (EditText) v.findViewById(R.id.txtJudul1);
        textnameJudul = (TextView) v.findViewById(R.id.txtnameJudul);
        edit_kuis = (EditText) v.findViewById(R.id.edit_Kuiss);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);
                return v;
            }
        }

        Bundle b = getArguments();
        String getName = (String) b.get("nameJudul");
        textnameJudul.setText(getName);

        enableButton();

        return v;
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
                    Toast.makeText(getContext(), "Pilih Dulu Video", Toast.LENGTH_SHORT).show();
                }
                else {
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
        Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContext().getContentResolver().query(
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
                uploading = ProgressDialog.show(getContext(), "Uploading File", "Please wait...", false, false);
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

            Toast.makeText(getContext(), "Data Tidak ada", Toast.LENGTH_SHORT).show();

        } else {
            save(nameUrl2, videoUrl, nameUrl, linkKuis);
        }
    }

    public void save(final String judul2, final String Url, final String judul, final String linkKuis){
        ApiURL apiURL = new ApiURL();
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.getCache().clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, apiURL.getSaveVideo2(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getContext(), "Succes, Data Telah Tersimpan", Toast.LENGTH_LONG).show();

                LearnFrag learnFrag = new LearnFrag();
                FragmentTransaction ft  = getFragmentManager().beginTransaction();
                ft.replace(R.id.frame_container, learnFrag);
                ft.commit();

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
