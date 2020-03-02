package com.coverteam.junkcraft.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.coverteam.junkcraft.R;
import com.coverteam.junkcraft.model.DataIklan;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class AdapterIklan extends ArrayAdapter<DataIklan> {
    private Activity context;
    private List<DataIklan> dataIklanList;

    public AdapterIklan(Activity context, List<DataIklan> dataIklanList){

        super(context, R.layout.list_iklan, dataIklanList);
        this.context = context;
        this.dataIklanList = dataIklanList;
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.list_iklan,null,true);
        TextView textJudul = listViewItem.findViewById(R.id.judul);
        TextView textDesc = listViewItem.findViewById(R.id.desc);;
        TextView textHarga = listViewItem.findViewById(R.id.harga);
        ImageView imagebarang = listViewItem.findViewById(R.id.fotobarang);

        DataIklan dataIklanlist = dataIklanList.get(position);
        textJudul.setText(dataIklanlist.getIklanJudul());
        textDesc.setText(dataIklanlist.getIklanDeskripsi());

        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        textHarga.setText(formatRupiah.format(dataIklanlist.getIklanHarga()));

        Picasso.with(context)
                .load(dataIklanlist.getIklanFoto()).centerCrop().fit()
                .into(imagebarang, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                    }
                });

        return listViewItem;
    }

}
