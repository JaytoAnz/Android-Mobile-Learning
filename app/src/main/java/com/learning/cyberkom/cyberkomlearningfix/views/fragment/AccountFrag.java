package com.learning.cyberkom.cyberkomlearningfix.views.fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.learning.cyberkom.cyberkomlearningfix.LoginActivity;
import com.learning.cyberkom.cyberkomlearningfix.R;
import com.learning.cyberkom.cyberkomlearningfix.model.ApiURL;
import com.learning.cyberkom.cyberkomlearningfix.views.ViewAccountActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.CONNECTIVITY_SERVICE;

public class AccountFrag extends Fragment implements View.OnClickListener{

    EditText txtUsername, txtEmail, txtPass;
    Button btnlogout;
    ImageView ViewAccount, ViewPass, edit_user, save_user;
    SharedPreferences shared;
    AlertDialog alertdialog;
    Bitmap bitmap;
    FloatingActionButton fab;
    ProgressBar progressBar;
    CircleImageView profil_image;
    private Activity mActivity;

    private final int IMG_REQUEST = 1;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_account, container, false);

        txtUsername = v.findViewById(R.id.txtusername);
        txtEmail = v.findViewById(R.id.txtemail);
        txtPass = v.findViewById(R.id.txtPassword);
        ViewAccount = v.findViewById(R.id.ViewAcc);
        btnlogout = v.findViewById(R.id.logout);
        ViewPass = v.findViewById(R.id.ViewPass);
        profil_image = v.findViewById(R.id.profile_image);
        edit_user = v.findViewById(R.id.Edit_user);
        save_user = v.findViewById(R.id.Save_user);
        fab = v.findViewById(R.id.changeImage);
        progressBar = v.findViewById(R.id.LoadImage);

        shared = getActivity().getSharedPreferences("Mypref_Login", Context.MODE_PRIVATE);

        txtUsername.setFocusableInTouchMode(false);
        txtEmail.setFocusableInTouchMode(false);
        txtPass.setFocusableInTouchMode(false);
        txtUsername.setFocusable(false);
        txtEmail.setFocusable(false);
        txtPass.setFocusable(false);

        edit_user.setOnClickListener(this);
        save_user.setOnClickListener(this);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseFile();
            }
        });

        ViewPass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        txtPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        return true;
                    case MotionEvent.ACTION_UP:
                        txtPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        return true;
                }
                return false;
            }
        });

        ViewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ViewAccountActivity.class);
                startActivity(intent);
            }
        });

        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertdialog = new AlertDialog.Builder(getContext()).create();
                alertdialog.setTitle("Logout");
                alertdialog.setMessage("Are you sure ! logout ?");
                alertdialog.setCancelable(false);
                alertdialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        alertdialog.dismiss();

                    }
                });

                alertdialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = shared.edit();
                        editor.clear();
                        editor.commit();
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        getActivity().finish();
                        startActivity(intent);

                    }
                });
                alertdialog.show();
            }
        });
        checkInternet();
        return v;
    }

    private void checkInternet(){
        if (CheckNetwork()){
            load();
        }else if (!CheckNetwork()){
            Toast.makeText(getContext(), "Network Disconnected", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean CheckNetwork(){
        boolean WIFI = false;
        boolean DATA_MOBILE = false;

        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(CONNECTIVITY_SERVICE);
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

    public void load(){
        String id = shared.getString("idKey", "");
        progressBar.setVisibility(View.VISIBLE);
        ApiURL apiURL = new ApiURL();
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.getCache().clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, apiURL.getDetailaccount() + "?id=" +id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
                Log.i("Hitesh", "" + response);
                try {
                    JSONArray array = new JSONArray(response);

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject items = array.getJSONObject(i);

                        String id = items.getString("id");
                        String name = items.getString("username");
                        String email = items.getString("email");
                        String password = items.getString("password");
                        String level = items.getString("level");
                        String foto = items.getString("foto");

                        txtUsername.setText("" + name);
                        txtEmail.setText("" + email);
                        txtPass.setText("" + password);

                        if (mActivity == null) {
                            return;
                        }

                        RequestOptions requestOptions = new RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                                .skipMemoryCache(true);
                        Glide.with(getActivity())
                                .load(foto)
                                .apply(requestOptions)
                                .into(profil_image);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
            }
        });

        requestQueue.add(stringRequest);
    }

    @Override
    public void onResume() {
        checkInternet();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Edit_user:
                txtUsername.setFocusableInTouchMode(true);
                txtEmail.setFocusableInTouchMode(true);
                txtPass.setFocusableInTouchMode(true);

                InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(txtUsername, InputMethodManager.SHOW_IMPLICIT);

                edit_user.setVisibility(View.GONE);
                save_user.setVisibility(View.VISIBLE);

                break;

            case R.id.Save_user:

                SaveEditDetaill();

                edit_user.setVisibility(View.VISIBLE);
                save_user.setVisibility(View.GONE);

                txtUsername.setFocusableInTouchMode(false);
                txtEmail.setFocusableInTouchMode(false);
                txtPass.setFocusableInTouchMode(false);

                txtUsername.setFocusable(false);
                txtEmail.setFocusable(false);
                txtPass.setFocusable(false);

                break;
        }
    }

    private void SaveEditDetaill() {

        final String name = this.txtUsername.getText().toString().trim();
        final String email = this.txtEmail.getText().toString().trim();
        final String pass = this.txtPass.getText().toString().trim();
        final String id = shared.getString("idKey", "");

        ApiURL apiURL = new ApiURL();
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.getCache().clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, apiURL.getEdit_account(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    JSONArray jsonArray = jsonObject.getJSONArray("users");
                    for (int i = 0; i < jsonArray.length(); i++) {

                        //getting product object from json array
                        JSONObject items = jsonArray.getJSONObject(i);
                        String id = items.getString("id");
                        String username = items.getString("username");
                        String email = items.getString("email");
                        String password = items.getString("password");
                        String level = items.getString("level");
                        SharedPreferences.Editor editor = shared.edit();
                        editor.clear();
                        editor.putString("idKey", id);
                        editor.putString("usernameKey", username);
                        editor.putString("emailKey", email);
                        editor.putString("passwordKey", password);
                        editor.putString("levelKey", level);
                        editor.commit();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> stringMap = new HashMap<>();
                stringMap.put("id", id);
                stringMap.put("username", name);
                stringMap.put("email", email);
                stringMap.put("password", pass);
                return stringMap;
            }
        };

        requestQueue.add(stringRequest);
    }

    private void ChooseFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Foto"), IMG_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String id = shared.getString("idKey", "");
            editPhoto(id, getStringImage(bitmap));
        }
    }

    private String getStringImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        byte[] imageByteArray = byteArrayOutputStream.toByteArray();
        String encodeImage = Base64.encodeToString(imageByteArray, Base64.NO_WRAP);

        return encodeImage;
    }

    private void editPhoto(final String id, final String photo){
        progressBar.setVisibility(View.VISIBLE);
        System.out.print(" " + photo);
        ApiURL apiURL = new ApiURL();
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.getCache().clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, apiURL.getEditphoto(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
                Log.i("Hitesh", "" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    load();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> stringMap = new HashMap<>();
                stringMap.put("id", id);
                stringMap.put("image", photo);
                return stringMap;
            }
        };

        requestQueue.add(stringRequest);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
    }
}