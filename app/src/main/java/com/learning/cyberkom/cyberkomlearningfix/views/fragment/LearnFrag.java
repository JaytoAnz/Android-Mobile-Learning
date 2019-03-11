package com.learning.cyberkom.cyberkomlearningfix.views.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.learning.cyberkom.cyberkomlearningfix.R;
import com.learning.cyberkom.cyberkomlearningfix.adapter.LearnAdapter;
import com.learning.cyberkom.cyberkomlearningfix.model.ApiURL;
import com.learning.cyberkom.cyberkomlearningfix.model.Mlearning;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class LearnFrag extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<Mlearning> learnList;
    SwipeRefreshLayout swipeRefreshLayout;
    FloatingActionButton fab;

    @SuppressLint("ResourceAsColor")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.frag_learn, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        fab = v.findViewById(R.id.fabtambah);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        learnList = new ArrayList<>();
        adapter = new LearnAdapter(learnList, getContext().getApplicationContext());
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                checkInternet();
            }
        });

        //eksekusi

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CheckNetwork()) {
                    tampilUpload();
                }else if (!CheckNetwork()) {
                    Toast.makeText(getContext(), "Network Disconnected", Toast.LENGTH_SHORT).show();
                }
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && fab.getVisibility() == View.VISIBLE) {
                    fab.hide();
                } else if (dy < 0 && fab.getVisibility() !=View.VISIBLE) {
                    fab.show();
                }
            }
        });

        Validate();
        onBackPressed();

        return v;
    }

    public void Validate(){
        SharedPreferences shared = getContext().getSharedPreferences("Mypref_Login", Context.MODE_PRIVATE);
        String val = shared.getString("levelKey", "");
        if(val.equals("dosen")){
            fab.setVisibility(VISIBLE);
        }else{
            fab.setVisibility(GONE);
        }
    }

    public void onBackPressed()
    {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.popBackStack();
    }

    public void tampilUpload() {
        UploadFrag uploadFrag = new UploadFrag();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, uploadFrag);
        fragmentTransaction.commit();
    }

    public void checkInternet(){
        if(CheckNetwork()) {
            loadData();
        }
        else if (!CheckNetwork()){
            Toast.makeText(getContext(), "Network Disconnected ", Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);
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

    public void loadData(){
        swipeRefreshLayout.setRefreshing(true);
        learnList.clear();
        ApiURL apiURL = new ApiURL();
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.getCache().clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, apiURL.getViewMateri(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //converting the string to json array object
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("materi");
                    Log.d("COBA LEARN", " RESPON : " + array);

                    for (int i = 0; i < array.length(); i++) {

                        //getting product object from json array
                        JSONObject items = array.getJSONObject(i);

                        //adding the product to product list
                        learnList.add(new Mlearning(
                                items.getString("getid"),
                                items.getString("nameurl"),
                                items.getString("video"),
                                items.getString("nameurl2"),
                                items.getString("video2"),
                                items.getString("linkkuis"),
                                items.getString("linknilai"),
                                items.getString("date")
                        ));
                    }

                    swipeRefreshLayout.setRefreshing(false);

                } catch (JSONException e) {
                    e.printStackTrace();
                    swipeRefreshLayout.setRefreshing(false);
                }

                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), " Network Disconnected ", Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(stringRequest);
    }

    @Override
    public void onRefresh() {
        checkInternet();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
