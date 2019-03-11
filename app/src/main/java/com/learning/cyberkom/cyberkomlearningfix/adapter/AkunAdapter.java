package com.learning.cyberkom.cyberkomlearningfix.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.learning.cyberkom.cyberkomlearningfix.R;
import com.learning.cyberkom.cyberkomlearningfix.model.AkunUser;
import com.learning.cyberkom.cyberkomlearningfix.model.ApiURL;
import com.learning.cyberkom.cyberkomlearningfix.views.fragment.ViewAccountMhs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class AkunAdapter extends RecyclerView.Adapter<AkunAdapter.AkunViewHolder> {

    private Context mCtx;
    private List<AkunUser> dataList;

    public AkunAdapter(List<AkunUser> dataList, Context mCtx) {
        this.dataList = dataList;
        this.mCtx = mCtx;
    }

    public class AkunViewHolder extends RecyclerView.ViewHolder{
        private Context mCtx;
        private List<AkunUser> dataList;
        private TextView username, password, email, level;
        private ImageView ImageAcc, btn_hapus;

        public AkunViewHolder(View itemView, Context mCtx, List<AkunUser> dataList) {
            super(itemView);
            this.mCtx = mCtx;
            this.dataList = dataList;

            username = (TextView) itemView.findViewById(R.id.username);
            email = (TextView) itemView.findViewById(R.id.email);
            password = (TextView) itemView.findViewById(R.id.password);
            level = (TextView) itemView.findViewById(R.id.levelnya);
            ImageAcc = (ImageView) itemView.findViewById(R.id.imageAcc);
            btn_hapus = (ImageView) itemView.findViewById(R.id.btn_hapus);

            btn_hapus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getRootView().getContext());
                    alertDialogBuilder.setMessage("Are you sure you want to delete?");
                    alertDialogBuilder.setPositiveButton("ok",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    ApiURL apiURL = new ApiURL();
                                    RequestQueue requestQueue = Volley.newRequestQueue(v.getContext());
                                    requestQueue.getCache().clear();
                                    String usernamee = username.getText().toString();
                                    StringRequest stringRequest = new StringRequest(Request.Method.GET, apiURL.getDeleteUsers() + "?username=" + usernamee, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            Log.i("Hitesh", "" + response);
                                            Toast.makeText(v.getContext(), "" + response, Toast.LENGTH_SHORT).show();

                                            ViewAccountMhs viewAccountMhs = new ViewAccountMhs();
                                            AppCompatActivity activity = (AppCompatActivity) v.getContext();
                                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, viewAccountMhs).commit();

                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(v.getContext(), "Network Disconnected", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                    requestQueue.add(stringRequest);

                                }
                            });

                    alertDialogBuilder.setNegativeButton("cancel",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {

                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                }
            });
        }
    }

    @Override
    public AkunViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_item_users, parent, false);
        return new AkunViewHolder(view, mCtx, dataList);
    }

    @Override
    public void onBindViewHolder(AkunViewHolder holder, int position) {
        AkunUser akunUser = dataList.get(position);
        RequestOptions requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                .skipMemoryCache(true);
        Glide.with(mCtx)
                .load(akunUser.getFoto())
                .apply(requestOptions.fitCenter())
                .into(holder.ImageAcc);

        holder.username.setText(akunUser.getUsername());
        holder.email.setText(akunUser.getEmail());
        holder.password.setText(akunUser.getPassword());
        holder.level.setText(akunUser.getLevel());

        SharedPreferences shared = mCtx.getSharedPreferences("Mypref_Login", Context.MODE_PRIVATE);
        String valLevel = shared.getString("levelKey", "");
        if(valLevel.equals("dosen")){
            holder.btn_hapus.setVisibility(VISIBLE);
        }else {
            holder.password.setVisibility(GONE);
            holder.level.setVisibility(GONE);
            holder.btn_hapus.setVisibility(GONE);
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
