package com.coverteam.junkcraft.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.coverteam.junkcraft.Menu_utana;
import com.coverteam.junkcraft.R;
import com.coverteam.junkcraft.model.DataMarket;
import com.coverteam.junkcraft.model.DataMarket;
import com.coverteam.junkcraft.mainmarket;
import com.google.firebase.database.DataSnapshot;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class AdapterMarket extends RecyclerView.Adapter<AdapterMarket.MyViewHolder3> {
    private Context context;
    private ArrayList<DataMarket> dataMarkets;

    String idmarket = null;
    String idpenjual = null;

    public AdapterMarket(Context cont, ArrayList<DataMarket> data) {
        context = cont;
        dataMarkets = data;
    }

    @NonNull
    @Override
    public MyViewHolder3 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_marketplace, parent, false);

        return new MyViewHolder3(view);
    }

    @Override
    public void onBindViewHolder(final @NonNull MyViewHolder3 holder, final int position) {

        Picasso.with(context)
                .load(dataMarkets.get(position).getIklanFoto())
                .into(holder.vfoto, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {

                    }
                });
        holder.vjudul.setText(dataMarkets.get(position).getIklanJudul());
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        holder.vharga.setText(formatRupiah.format(dataMarkets.get(position).getIklanHarga()));

        holder.layoutmarket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, mainmarket.class);
                idmarket = dataMarkets.get(position).getIklanID();
                idpenjual = dataMarkets.get(position).getIklanPenjualID();
                intent.putExtra("id", idmarket);
                intent.putExtra("idpenjual", idpenjual);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataMarkets.size();
    }

    public class MyViewHolder3 extends RecyclerView.ViewHolder {
        TextView vjudul, vharga;
        ImageView vfoto;
        public ConstraintLayout layoutmarket;
        ProgressBar progressBar;

        public MyViewHolder3(@NonNull View itemView) {
            super(itemView);

            vfoto = itemView.findViewById(R.id.fotomarket);
            vjudul = itemView.findViewById(R.id.judulmarket);
            vharga = itemView.findViewById(R.id.hargamarket);
            layoutmarket = itemView.findViewById(R.id.layoutmarket);
            progressBar = itemView.findViewById(R.id.progressbar);
        }
    }
}
