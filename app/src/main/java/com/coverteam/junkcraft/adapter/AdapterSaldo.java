package com.coverteam.junkcraft.adapter;

import android.app.LauncherActivity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.coverteam.junkcraft.CheckOut_Saldo0;
import com.coverteam.junkcraft.Checkout_pembelian;
import com.coverteam.junkcraft.Profile;
import com.coverteam.junkcraft.R;
import com.coverteam.junkcraft.iklan_pesanan;
import com.coverteam.junkcraft.model.DataSaldo;


import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class AdapterSaldo extends RecyclerView.Adapter<AdapterSaldo.ViewHolder> {

    private List<DataSaldo> listItems;
    private Context context;

    Locale localeID = new Locale("in", "ID");
    NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

    public AdapterSaldo(List<DataSaldo> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_saldo,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final String status, type, idtransaksi;
        holder.tgltopup.setText(listItems.get(position).getSaldoTanggal());
        status = listItems.get(position).getSaldoStatus();
        type = listItems.get(position).getSaldoType();
        idtransaksi = listItems.get(position).getSaldoIDTransaksi();
        if (type.equals("Top-up")) {
            if (status.equals("transfer")) {
                holder.stattopup.setImageResource(R.drawable.ic_upload_saldo);
                holder.desctopup.setText("[Top-up] Belum Transfer");
            } else if (status.equals("verifikasi")) {
                holder.stattopup.setImageResource(R.drawable.ic_wait_saldo);
                holder.desctopup.setText("[Top-up] Menunggu Verifikasi");
            } else if (status.equals("berhasil")) {
                holder.stattopup.setImageResource(R.drawable.ic_done_saldo);
                holder.desctopup.setText("[Top-up] Top-up Berhasil");
            } else {
                holder.stattopup.setImageResource(R.drawable.ic_batal_saldo);
                holder.desctopup.setText("[Top-up] Top-up Dibatalkan");
            }
            holder.jmltopup.setText(formatRupiah.format(listItems.get(position).getSaldoJumlah()));
            holder.cardViewsaldo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer pos = position+1;
                    Intent intent = new Intent(context, CheckOut_Saldo0.class);
                    //Toast.makeText(context, pos.toString(), Toast.LENGTH_SHORT).show();
                    intent.putExtra("posisi",pos);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }
        else if (type.equals("Beli")){
            holder.stattopup.setImageResource(R.drawable.ic_beli_saldo);
            holder.desctopup.setText("[Pembelian] ID:"+status);
            holder.jmltopup.setText("-"+formatRupiah.format(listItems.get(position).getSaldoJumlah()));
            holder.cardViewsaldo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //idtransaksi
                    Intent intent = new Intent(context, Checkout_pembelian.class);
                    //Toast.makeText(context, pos.toString(), Toast.LENGTH_SHORT).show();
                    intent.putExtra("checkoutid",idtransaksi);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }
        else {
            holder.stattopup.setImageResource(R.drawable.ic_jual_saldo);
            holder.desctopup.setText("[Penjualan] ID:"+status);
            holder.jmltopup.setText(formatRupiah.format(listItems.get(position).getSaldoJumlah()));
            holder.cardViewsaldo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //idtransaksi
                    //Toast.makeText(context, "Jual", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, iklan_pesanan.class);
                    intent.putExtra("checkoutid",idtransaksi);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView jmltopup;
        public TextView tgltopup;
        public TextView desctopup;
        public ImageView stattopup;
        public CardView cardViewsaldo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tgltopup = itemView.findViewById(R.id.tglsaldo);
            jmltopup = itemView.findViewById(R.id.jmlhsaldo);
            desctopup = itemView.findViewById(R.id.descsaldo);
            stattopup = itemView.findViewById(R.id.statsaldo);
            cardViewsaldo = itemView.findViewById(R.id.CardViewSaldo);
        }
    }
}
