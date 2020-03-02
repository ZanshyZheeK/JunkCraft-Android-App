package com.coverteam.junkcraft.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.coverteam.junkcraft.R;
import com.coverteam.junkcraft.model.DataCheckout;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterCheckout extends ArrayAdapter<DataCheckout> {
    private Activity context;
    private List<DataCheckout> dataCheckoutList;

    public AdapterCheckout(Activity context, List<DataCheckout> dataCheckoutList){

        super(context, R.layout.list_checkout, dataCheckoutList);
        this.context = context;
        this.dataCheckoutList = dataCheckoutList;
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.list_checkout,null,true);
        TextView textJudul = listViewItem.findViewById(R.id.judul);
        TextView textID = listViewItem.findViewById(R.id.id);
        TextView textTgl = listViewItem.findViewById(R.id.tanggal);
        TextView textStatus = listViewItem.findViewById(R.id.status);
        ImageView imagebarang = listViewItem.findViewById(R.id.fotobarang);

        DataCheckout dataCheckoutlist = dataCheckoutList.get(position);
        textJudul.setText(dataCheckoutlist.getCheckoutJudul());
        textID.setText("ID : "+dataCheckoutlist.getCheckoutIDPembelian());
        textTgl.setText(dataCheckoutlist.getCheckoutDateTime());
        textStatus.setText("Status : "+dataCheckoutlist.getCheckoutStatus());
        Picasso.with(context)
                .load(dataCheckoutlist.getCheckoutFotoBarang()).centerCrop().fit()
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
