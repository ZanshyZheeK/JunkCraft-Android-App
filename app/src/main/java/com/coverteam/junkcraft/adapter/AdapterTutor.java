package com.coverteam.junkcraft.adapter;

import android.app.Activity;
import android.app.LauncherActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.coverteam.junkcraft.Menu_utana;
import com.coverteam.junkcraft.Profile;
import com.coverteam.junkcraft.R;
import com.coverteam.junkcraft.model.DataTutorial;
import com.coverteam.junkcraft.tdetail;
import com.coverteam.junkcraft.tpaper;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

public class AdapterTutor extends RecyclerView.Adapter<AdapterTutor.MyViewHolder2> {

    private Activity activity;
    private Context context;
    private ArrayList<DataTutorial> datatutorials;

    String idtutor = null;
    String kategoridetail = null;

    public AdapterTutor(Context cont, ArrayList<DataTutorial>data){
        this.context=cont;
        datatutorials = data;
    }

    @NonNull
    @Override
    public MyViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tutorial, parent, false);

        return new MyViewHolder2(view);
    }

    @Override
    public void onBindViewHolder(final @NonNull MyViewHolder2 holder, final int position) {

        Picasso.with(context)
                .load(datatutorials.get(position).getFoto())
                .into(holder.vimage, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {

                    }
                });
        holder.vjudul.setText(datatutorials.get(position).getJudul());
        holder.vketerangan.setText(datatutorials.get(position).getKeterangan());

        holder.layouttutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,tdetail.class);
                idtutor = datatutorials.get(position).getId();
                if (idtutor.contains("paper")){
                    kategoridetail = "PAPER";
                }
                else if (idtutor.contains("metal")){
                    kategoridetail = "METAL";
                }
                else if (idtutor.contains("plastic")){
                    kategoridetail = "PLASTIC";
                }
                else if (idtutor.contains("glass")){
                    kategoridetail = "GLASS";
                }
                else{
                    kategoridetail = "WASTE";
                }
                intent.putExtra("kategoridetail",kategoridetail);
                intent.putExtra("tutorial", idtutor);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return datatutorials.size();
    }

    public class MyViewHolder2 extends RecyclerView.ViewHolder {
        TextView vjudul, vketerangan;
        ImageView vimage;
        public ConstraintLayout layouttutor;
        public ProgressBar progressBar;

        public MyViewHolder2(@NonNull View itemView) {
            super(itemView);

            vimage = itemView.findViewById(R.id.fototutor);
            vjudul = itemView.findViewById(R.id.judultutor);
            vketerangan = itemView.findViewById(R.id.kettutor);
            layouttutor = itemView.findViewById(R.id.layouttutor);
            progressBar = itemView.findViewById(R.id.progressbar);
        }
    }
}
