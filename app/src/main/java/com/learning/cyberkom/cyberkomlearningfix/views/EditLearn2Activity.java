package com.learning.cyberkom.cyberkomlearningfix.views;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.learning.cyberkom.cyberkomlearningfix.views.fragment.LearnFrag;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

import static android.app.Activity.RESULT_OK;

public class EditLearn2Activity extends Fragment {

    TextView textView, textView1, textView2, textView3, textView4;
    EditText txt, txt1, txt2;
    Button btn, btn1, btn2;
    private static final int SELECT_VIDEO = 3;
    private SpotsDialog spotsDialog;
    private String selectedPath;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_edit_learn2, container, false);

        textView = (TextView) v.findViewById(R.id.textView2);
        textView1 = (TextView) v.findViewById(R.id.textViewResponse2);
        textView2 = (TextView) v.findViewById(R.id.txtnameJudul);
        textView3 = (TextView) v.findViewById(R.id.textViewResponse);
        textView4 = (TextView) v.findViewById(R.id.txtID);

        txt = (EditText) v.findViewById(R.id.edit_judul2);
        txt1 = (EditText) v.findViewById(R.id.edit_Kuis);
        txt2 = (EditText) v.findViewById(R.id.edit_Nilai);

        btn = (Button) v.findViewById(R.id.buttonChoose);
        btn1 = (Button) v.findViewById(R.id.buttonUpload);
        btn2 = (Button) v.findViewById(R.id.btn_simpan);

        spotsDialog = new SpotsDialog(getContext(), R.style.Custom);

        Bundle b = getArguments();
        String getJudul = b.getString("judulUrl");
        String getName = b.getString("nameUrl");
        String getJudul2 = b.getString("judulUrl2");
        String getName2 = b.getString("nameUrl2");
        String getLinkKuis = b.getString("linkKuis");
        String getLinkNilai = b.getString("linkNilai");
        String getID = b.getString("ID");

        textView2.setText(getJudul);
        textView3.setText(getName);
        txt.setText(getJudul2);
        textView1.setText(getName2);
        txt1.setText(getLinkKuis);
        txt2.setText(getLinkNilai);
        textView4.setText(getID);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseVideo();
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String aftvideoUrl = textView1.getText().toString();
                if (aftvideoUrl.isEmpty()) {

                    Toast.makeText(getContext(), "Pilih Dulu Video", Toast.LENGTH_SHORT).show();

                } else {
                    uploadVideo();
                }

            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameJudul2 = txt.getText().toString();
                String linkKuis = txt1.getText().toString();
                String linkNilai = txt2.getText().toString();
                String aftvideoUrl = textView1.getText().toString();
                if (nameJudul2.isEmpty() || aftvideoUrl.isEmpty() || linkKuis.isEmpty() || linkNilai.isEmpty()) {

                    Toast.makeText(getContext(), "Data Tidak ada", Toast.LENGTH_SHORT).show();

                } else {
                    ValidasiSave();
                }
            }
        });

        return v;
    }

    private void chooseVideo(){
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
                textView1.setText(selectedPath);
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

    private void uploadVideo(){
        class UploadVideo extends AsyncTask<Void, Void, String> {

            ProgressDialog uploading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                uploading = ProgressDialog.show(getContext(), "Uploading File", "Please wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                uploading.dismiss();
                textView1.setText(Html.fromHtml("<b> <a href='" + s + "'>" + s + "</a></b>"));
                textView1.setMovementMethod(LinkMovementMethod.getInstance());
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

    private void ValidasiSave(){
        String nameJudul2 = txt.getText().toString();
        String linkKuis = txt1.getText().toString();
        String linkNilai = txt2.getText().toString();
        String aftvideoUrl = textView1.getText().toString();
        if (nameJudul2.isEmpty() || aftvideoUrl.isEmpty() || linkKuis.isEmpty() || linkNilai.isEmpty()) {

            Toast.makeText(getContext(), "Data Masih Kosong", Toast.LENGTH_SHORT).show();

        } else {
            updateData(nameJudul2, aftvideoUrl, linkKuis, linkNilai);
        }
    }

    private void updateData(final String nameJudul2, final String aftvideoUrl, final String linkKuis, final String linkNilai){
        spotsDialog.show();
        ApiURL apiURL = new ApiURL();
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, apiURL.getEditMateri(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("Hitesh", "" + response);
                Toast.makeText(getContext(), "" + response, Toast.LENGTH_SHORT).show();
                spotsDialog.dismiss();
                LearnFrag learnFrag = new LearnFrag();
                AppCompatActivity activity = (AppCompatActivity) getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, learnFrag).commit();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                spotsDialog.dismiss();
                Log.i("Hitesh", "" + error);
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> stringMap = new HashMap<>();
                stringMap.put("nameUrl", textView2.getText().toString());
                stringMap.put("video", textView3.getText().toString());
                stringMap.put("nameUrl2", nameJudul2);
                stringMap.put("video2", aftvideoUrl);
                stringMap.put("linkKuis", linkKuis);
                stringMap.put("linkNilai", linkNilai);
                stringMap.put("no_id", textView4.getText().toString());
                return stringMap;
            }
        };

        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();
    }
}
