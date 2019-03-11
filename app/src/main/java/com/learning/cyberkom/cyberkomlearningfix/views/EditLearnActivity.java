package com.learning.cyberkom.cyberkomlearningfix.views;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class EditLearnActivity extends Fragment {

    TextView textView1, textView2, textView3, textView4, textView5, textView6, textView7;
    Button button1, button2, button3;
    EditText editText1;

    private static final int SELECT_VIDEO = 3;

    private String selectedPath;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_edit_learn, container, false);

        textView1 = (TextView) v.findViewById(R.id.textView);
        textView2 = (TextView) v.findViewById(R.id.textViewResponse);
        textView3 = (TextView) v.findViewById(R.id.textViewSlide);
        textView4 = (TextView) v.findViewById(R.id.textVideoSlide);
        textView5 = (TextView) v.findViewById(R.id.textLinkKuis);
        textView6 = (TextView) v.findViewById(R.id.txtLinkNilai);
        textView7 = (TextView) v.findViewById(R.id.txtID);

        button1 = (Button) v.findViewById(R.id.btn_next1);
        button2 = (Button) v.findViewById(R.id.buttonChoose);
        button3 = (Button) v.findViewById(R.id.buttonUpload);

        editText1 = (EditText) v.findViewById(R.id.txtJudul1);

        Bundle b = getArguments();
        String getJudul = b.getString("judulUrl");
        String getName = b.getString("nameUrl");
        String getJudul2 = b.getString("judulUrl2");
        String getName2 = b.getString("nameUrl2");
        String getLinkKuis = b.getString("linkKuis");
        String getLinkNilai = b.getString("linkNilai");

        editText1.setText(getJudul);
        textView2.setText(getName);
        textView3.setText(getJudul2);
        textView4.setText(getName2);
        textView5.setText(getLinkKuis);
        textView6.setText(getLinkNilai);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameJudul = editText1.getText().toString();
                String aftvideoUrl = textView2.getText().toString();
                if (nameJudul.isEmpty() || aftvideoUrl.isEmpty()) {

                    Toast.makeText(getContext(), "Data Tidak ada", Toast.LENGTH_SHORT).show();

                } else {
                    ValidasiSave();
                    String value1 = editText1.getText().toString();
                    String value2 = textView2.getText().toString();
                    String value3 = textView3.getText().toString();
                    String value4 = textView4.getText().toString();
                    String value5 = textView5.getText().toString();
                    String value6 = textView6.getText().toString();
                    String value7 = textView7.getText().toString();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("judulUrl", value1);
                    bundle.putSerializable("nameUrl", value2);
                    bundle.putSerializable("judulUrl2", value3);
                    bundle.putSerializable("nameUrl2", value4);
                    bundle.putSerializable("linkNilai", value5);
                    bundle.putSerializable("linkKuis", value6);
                    bundle.putSerializable("ID", value7);
                    EditLearn2Activity editLearn2Activity = new EditLearn2Activity();
                    editLearn2Activity.setArguments(bundle);
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, editLearn2Activity).commit();
                }
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseVideo();
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String befvideoUrl = textView1.getText().toString();
                if (befvideoUrl.isEmpty()) {

                    Toast.makeText(getContext(), "Pilih Dulu Video", Toast.LENGTH_SHORT).show();

                } else {
                    uploadVideo();
                }
            }
        });

        load();

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
                textView2.setText(Html.fromHtml("<b> <a href='" + s + "'>" + s + "</a></b>"));
                textView2.setMovementMethod(LinkMovementMethod.getInstance());
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
        String nameJudul = editText1.getText().toString();
        String aftvideoUrl = textView2.getText().toString();
        if (nameJudul.isEmpty() || aftvideoUrl.isEmpty()) {

            Toast.makeText(getContext(), "Data Tidak ada", Toast.LENGTH_SHORT).show();

        } else {
            updateData(nameJudul, aftvideoUrl);
        }
    }

    private void updateData(final String nameJudul, final String aftvideoUrl){
        ApiURL apiURL = new ApiURL();
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, apiURL.getEditMateri(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i("Hitesh", "" + error);
                Toast.makeText(getContext(), "" + error, Toast.LENGTH_SHORT).show();

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> stringMap = new HashMap<>();
                stringMap.put("nameUrl", nameJudul);
                stringMap.put("video", aftvideoUrl);
                stringMap.put("nameUrl2", textView3.getText().toString());
                stringMap.put("video2", textView4.getText().toString());
                stringMap.put("linkKuis", textView5.getText().toString());
                stringMap.put("linkNilai", textView6.getText().toString());
                stringMap.put("no_id", textView7.getText().toString());
                return stringMap;
            }
        };

        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();
    }

    private void load(){
        ApiURL apiURL = new ApiURL();
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, apiURL.getViewid(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String id = jsonObject.getString("no_id");
                    textView7.setText(id);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("nameUrl", editText1.getText().toString());

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}
