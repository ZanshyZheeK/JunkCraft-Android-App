package com.coverteam.junkcraft;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.coverteam.junkcraft.adapter.AdapterMarket;
import com.coverteam.junkcraft.adapter.AdapterMarket;
import com.coverteam.junkcraft.model.DataMarket;
import com.coverteam.junkcraft.model.DataMarket;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class mpaper extends AppCompatActivity implements View.OnClickListener{

    ImageView btn_back;
    TextView tvpaper,tvmetal,tvplastic,tvwaste,tvglass,tvjudul;

    DatabaseReference databasemarket;

    ArrayList<DataMarket> list;
    AdapterMarket adapter;

    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;
    String kategorimarket = "paper";
    String kategoriiklan = "iklanKategori";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpaper);

        databasemarket = FirebaseDatabase.getInstance().getReference("Market");

        tvpaper = findViewById(R.id.txpaper);
        tvmetal = findViewById(R.id.txmetal);
        tvplastic = findViewById(R.id.txplastic);
        tvwaste = findViewById(R.id.txwaste);
        tvglass = findViewById(R.id.txglass);
        tvjudul = findViewById(R.id.mjudul);

        findViewById(R.id.mmetal).setOnClickListener(this);
        findViewById(R.id.mplastic).setOnClickListener(this);
        findViewById(R.id.mglass).setOnClickListener(this);
        findViewById(R.id.mpaper).setOnClickListener(this);
        findViewById(R.id.mwaste).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);

        mRecycler = findViewById(R.id.list_market);
        mRecycler.setHasFixedSize(true);

        mManager = new LinearLayoutManager(this);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        tvjudul.setText("PAPER");
        findProduk(kategoriiklan,kategorimarket);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mmetal:
                tvpaper.setTextColor(Color.parseColor("#FFFFFF"));
                tvmetal.setTextColor(Color.parseColor("#FFBF70"));
                tvplastic.setTextColor(Color.parseColor("#FFFFFF"));
                tvwaste.setTextColor(Color.parseColor("#FFFFFF"));
                tvglass.setTextColor(Color.parseColor("#FFFFFF"));
                tvjudul.setText("METAL");
                kategorimarket = "metal";
                findProduk(kategoriiklan,kategorimarket);
                break;
            case R.id.mplastic:
                tvpaper.setTextColor(Color.parseColor("#FFFFFF"));
                tvmetal.setTextColor(Color.parseColor("#FFFFFF"));
                tvplastic.setTextColor(Color.parseColor("#FFBF70"));
                tvwaste.setTextColor(Color.parseColor("#FFFFFF"));
                tvglass.setTextColor(Color.parseColor("#FFFFFF"));
                tvjudul.setText("PLASTIC");
                kategorimarket = "plastic";
                findProduk(kategoriiklan,kategorimarket);
                break;
            case R.id.mglass:
                tvpaper.setTextColor(Color.parseColor("#FFFFFF"));
                tvmetal.setTextColor(Color.parseColor("#FFFFFF"));
                tvplastic.setTextColor(Color.parseColor("#FFFFFF"));
                tvwaste.setTextColor(Color.parseColor("#FFFFFF"));
                tvglass.setTextColor(Color.parseColor("#FFBF70"));
                tvjudul.setText("GLASS");
                kategorimarket = "glass";
                findProduk(kategoriiklan,kategorimarket);
                break;
            case R.id.mpaper:
                tvpaper.setTextColor(Color.parseColor("#FFBF70"));
                tvmetal.setTextColor(Color.parseColor("#FFFFFF"));
                tvplastic.setTextColor(Color.parseColor("#FFFFFF"));
                tvwaste.setTextColor(Color.parseColor("#FFFFFF"));
                tvglass.setTextColor(Color.parseColor("#FFFFFF"));
                tvjudul.setText("PAPER");
                kategorimarket = "paper";
                findProduk(kategoriiklan,kategorimarket);
                break;
            case R.id.mwaste:
                tvpaper.setTextColor(Color.parseColor("#FFFFFF"));
                tvmetal.setTextColor(Color.parseColor("#FFFFFF"));
                tvplastic.setTextColor(Color.parseColor("#FFFFFF"));
                tvwaste.setTextColor(Color.parseColor("#FFBF70"));
                tvglass.setTextColor(Color.parseColor("#FFFFFF"));
                tvjudul.setText("WASTE");
                kategorimarket = "waste";
                findProduk(kategoriiklan,kategorimarket);
                break;
            case R.id.back:
                Intent goutama = new Intent(mpaper.this, Menu_utana.class);
                startActivity(goutama);
                break;
        }
    }

    private void findProduk(String id, String name) {
        Query searchQuery = databasemarket.orderByChild(id).equalTo(name);
        searchQuery.addListenerForSingleValueEvent(valueEventListener);
    }

    ValueEventListener valueEventListener = new ValueEventListener(){
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            LinearLayout data404 = findViewById(R.id.data_not_found);
            RecyclerView list404 = findViewById(R.id.list_market);
            if (dataSnapshot.exists()) {
                list404.setVisibility(View.VISIBLE);
                data404.setVisibility(View.GONE);
                list = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    DataMarket mkt = dataSnapshot1.getValue(DataMarket.class);
                    list.add(mkt);
                }
                adapter = new AdapterMarket(getApplicationContext(), list);
                mRecycler.setAdapter(adapter);
            }
            else {
                list404.setVisibility(View.GONE);
                data404.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

            Toast.makeText(getApplicationContext(), "Terjadi kesalahan", Toast.LENGTH_LONG).show();

        }
    };
}
