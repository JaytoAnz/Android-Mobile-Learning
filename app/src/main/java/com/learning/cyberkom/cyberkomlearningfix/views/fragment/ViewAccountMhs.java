package com.learning.cyberkom.cyberkomlearningfix.views.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.learning.cyberkom.cyberkomlearningfix.R;
import com.learning.cyberkom.cyberkomlearningfix.adapter.AkunAdapter;
import com.learning.cyberkom.cyberkomlearningfix.model.AkunUser;
import com.learning.cyberkom.cyberkomlearningfix.model.ApiURL;
import com.learning.cyberkom.cyberkomlearningfix.views.RegisterActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class ViewAccountMhs extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private RecyclerView recyclerView;
    private RecyclerView.Adapter akunAdapter;
    private List<AkunUser> akunUserList;
    private FloatingActionButton fab;
    TextView titleakun;
    SwipeRefreshLayout swipeRefreshLayout;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.frag_view_account_mhs, container, false);

        titleakun = (TextView) v.findViewById(R.id.titleakun);
        recyclerView = (RecyclerView) v.findViewById(R.id.recylcerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        fab = v.findViewById(R.id.fabtambah_user);

        akunUserList = new ArrayList<>();
        akunAdapter = new AkunAdapter(akunUserList, getContext().getApplicationContext());
        recyclerView.setAdapter(akunAdapter);

        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(this);

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                checkInternet();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext().getApplicationContext(), RegisterActivity.class);
                getContext().startActivity(intent);
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

        validasi();

        return v;
    }

    private void validasi(){
        SharedPreferences shared = getContext().getSharedPreferences("Mypref_Login", Context.MODE_PRIVATE);
        String valLevel = shared.getString("levelKey", "");
        if(valLevel.equals("dosen")){
            fab.setVisibility(View.VISIBLE);
        }else{
            fab.setVisibility(View.GONE);
            titleakun.setText("Profil Dosen");
        }
    }

    public void checkInternet(){
        if(CheckNetwork()) {
            loadData();
        } else if (!CheckNetwork()){
            Toast.makeText(getContext(), "Network Disconnected ", Toast.LENGTH_LONG).show();
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
        akunUserList.clear();
        final ApiURL apiURL = new ApiURL();
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        String level = null;
        SharedPreferences shared = getContext().getSharedPreferences("Mypref_Login", Context.MODE_PRIVATE);
        String valLevel = shared.getString("levelKey", "");
        if(valLevel.equals("dosen")){
            level = "mahasiswa";
        }else{
            level = "dosen";
        }
        StringRequest stringRequest = new StringRequest(Request.Method.GET, apiURL.getViewAccount() + "?level=" + level, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //converting the string to json array object
                    JSONArray array = new JSONArray(response);

                    //traversing through all the object
                    for (int i = 0; i < array.length(); i++) {

                        //getting product object from json array
                        JSONObject items = array.getJSONObject(i);

                        //adding the product to product list
                        akunUserList.add(new AkunUser(
                                items.getInt("id"),
                                items.getString("username"),
                                items.getString("email"),
                                items.getString("password"),
                                items.getString("level"),
                                items.getString("foto")
                        ));

                    }

                    swipeRefreshLayout.setRefreshing(false);

                } catch (JSONException e) {
                    e.printStackTrace();
                    swipeRefreshLayout.setRefreshing(false);
                }

                akunAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Network Disconnected", Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();

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
