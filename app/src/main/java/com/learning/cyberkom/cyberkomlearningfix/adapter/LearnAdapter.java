package com.learning.cyberkom.cyberkomlearningfix.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.widget.VideoView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.learning.cyberkom.cyberkomlearningfix.R;
import com.learning.cyberkom.cyberkomlearningfix.helper.Utils;
import com.learning.cyberkom.cyberkomlearningfix.model.ApiURL;
import com.learning.cyberkom.cyberkomlearningfix.model.Mlearning;
import com.learning.cyberkom.cyberkomlearningfix.views.EditLearnActivity;
import com.learning.cyberkom.cyberkomlearningfix.views.NilaiActivity;
import com.learning.cyberkom.cyberkomlearningfix.views.PlayActivity;
import com.learning.cyberkom.cyberkomlearningfix.views.fragment.LearnFrag;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
/**
 * Created by Jayto on 8/7/2018.
 */

public class LearnAdapter extends RecyclerView.Adapter<LearnAdapter.LearnViewHolder> {
    private List<Mlearning> dataList;
    private Context ctx;

    public LearnAdapter(List<Mlearning> dataList, Context ctx) {
        this.dataList = dataList;
        this.ctx = ctx;
    }

    public class LearnViewHolder extends RecyclerView.ViewHolder{
        private final List<Mlearning> dataList;
        private final Context ctx;
        public TextView txtJudul;
        public ImageView txtVideo;
        public TextView txtJudul2;
        public ImageView txtVideo2;
        public TextView txtlinkkuis, txtNilai, Nilai, create_date;
        public ImageView imageView, imageView1, imageView2;

        public LearnViewHolder(View itemView, final Context ctx, final List<Mlearning> dataList) {
            super(itemView);
            this.dataList = dataList;
            this.ctx = ctx;
            txtJudul = itemView.findViewById(R.id.txt_judul);
            txtVideo = itemView.findViewById(R.id.txt_video);
            txtJudul2 = itemView.findViewById(R.id.txt_judul2);
            txtVideo2 = itemView.findViewById(R.id.txt_video2);
            txtNilai = itemView.findViewById(R.id.txtNilai);
            txtlinkkuis = itemView.findViewById(R.id.txtlinkkuis);
            Nilai = itemView.findViewById(R.id.Nilai);
            imageView = itemView.findViewById(R.id.btn_edit);
            imageView1 = itemView.findViewById(R.id.btn_hapus);
            imageView2 = itemView.findViewById(R.id.btn_tmbahNilai);
            create_date = itemView.findViewById(R.id.txtdate);

            SharedPreferences shared = itemView.getContext().getSharedPreferences("Mypref_Login", Context.MODE_PRIVATE);
            final String val = shared.getString("levelKey", "");
            if(val.equals("dosen")){
                imageView.setVisibility(VISIBLE);
                imageView1.setVisibility(VISIBLE);
                imageView2.setVisibility(VISIBLE);
            }else{
                imageView.setVisibility(GONE);
                imageView1.setVisibility(GONE);
                imageView2.setVisibility(GONE);
            }

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String value1 = dataList.get(getAdapterPosition()).nameurl;
                    String value2 = dataList.get(getAdapterPosition()).video;
                    String value3 = dataList.get(getAdapterPosition()).nameurl2;
                    String value4 = dataList.get(getAdapterPosition()).video2;
                    String value5 = dataList.get(getAdapterPosition()).linkkuis;
                    String value6 = dataList.get(getAdapterPosition()).linknilai;
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("judulUrl", value1);
                    bundle.putSerializable("nameUrl", value2);
                    bundle.putSerializable("judulUrl2", value3);
                    bundle.putSerializable("nameUrl2", value4);
                    bundle.putSerializable("linkNilai", value5);
                    bundle.putSerializable("linkKuis", value6);
                    EditLearnActivity editLearnActivity = new EditLearnActivity();
                    editLearnActivity.setArguments(bundle);
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, editLearnActivity).commit();
                }
            });

            imageView1.setOnClickListener(new View.OnClickListener() {
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
                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, apiURL.getDeleteMateri(), new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            Log.i("Hitesh", "" + response);
                                            LearnFrag learnFrag = new LearnFrag();
                                            AppCompatActivity activity = (AppCompatActivity) v.getContext();
                                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, learnFrag).commit();

                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(v.getContext(), "Not Network", Toast.LENGTH_LONG).show();
                                        }
                                    }) {
                                        @Override
                                        protected Map<String, String> getParams() throws AuthFailureError {
                                            Map<String, String> params = new HashMap<String, String>();
                                            params.put("nameUrl", txtJudul.getText().toString());

                                            return params;
                                        }
                                    };
                                    requestQueue.add(stringRequest);
                                    requestQueue.getCache().clear();

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

            imageView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String value = txtJudul.getText().toString();
                    Intent intent = new Intent(v.getContext().getApplicationContext(), NilaiActivity.class);
                    intent.putExtra("nameJudul", value);
                    v.getContext().startActivity(intent);
                }
            });

            txtNilai.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String valueWeb = Nilai.getText().toString();
                    if(valueWeb.equals("null")){
                        Toast.makeText(v.getContext(), "Link Nilai Belum Di Upload", Toast.LENGTH_LONG).show();
                    }else{
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(valueWeb));
                        v.getContext().startActivity(i);
                    }
                }
            });

            txtVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String value1 = txtJudul.getText().toString();
                    String value2 = dataList.get(getAdapterPosition()).video;
                    String value3 = txtlinkkuis.getText().toString();
                    Intent intent = new Intent(v.getContext().getApplicationContext(), PlayActivity.class);
                    intent.putExtra("judulUrl", value1);
                    intent.putExtra("nameUrl", value2);
                    intent.putExtra("linkKuis", value3);
                    v.getContext().startActivity(intent);
                }
            });
            txtVideo2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String value1 = txtJudul2.getText().toString();
                    String value2 = dataList.get(getAdapterPosition()).video2;
                    String value3 = txtlinkkuis.getText().toString();
                    Intent intent = new Intent(v.getContext().getApplicationContext(), PlayActivity.class);
                    intent.putExtra("judulUrl2", value1);
                    intent.putExtra("nameUrl2", value2);
                    intent.putExtra("linkKuis", value3);
                    v.getContext().startActivity(intent);
                }
            });
        }
    }

    @Override
    public LearnViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_item, parent, false);
        return new LearnViewHolder(view, ctx, dataList);
    }

    @Override
    public void onBindViewHolder(LearnViewHolder holder, int position) {
        String title = dataList.get(position).getNameurl();
        holder.txtJudul.setText(title);
        String defaultt = "null";
        holder.txtJudul.setText(title);
        String link = dataList.get(position).getVideo();
        try {
            Bitmap thumbnail = Utils.retriveVideoFrameFromVideo(link);
            holder.txtVideo.setImageBitmap(thumbnail);
        } catch (Throwable throwable) {
            Log.d("LEARNN ", throwable.toString());
        }
        String title2 = dataList.get(position).getNameurl2();
        if (title2.equals(defaultt)) {
            holder.txtJudul2.setText("");
            holder.txtVideo2.setClickable(false);
            holder.txtVideo2.setFocusable(false);
        } else {
            String link2 = dataList.get(position).getVideo2();
            holder.txtVideo2.setFocusable(true);
            holder.txtVideo2.setClickable(true);
            try {
                Bitmap thumbnail = Utils.retriveVideoFrameFromVideo(link2);
                holder.txtVideo2.setImageBitmap(thumbnail);
            } catch (Throwable throwable) {
                Log.d("LEARNN ", throwable.toString());
            }
            holder.txtJudul2.setText(title2);
        }

        holder.txtlinkkuis.setText(dataList.get(position).getLinkkuis());
        holder.Nilai.setText(dataList.get(position).getLinknilai());

        String createDate = dataList.get(position).getDate();
        SimpleDateFormat df1 = new SimpleDateFormat("yyy-MM-dd");
        SimpleDateFormat df2 = new SimpleDateFormat("EEE, d MMM yyyy");
        Date date = null;

        try {
            date = df1.parse(createDate);
            holder.create_date.setText("" + df2.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
