package com.coverteam.junkcraft;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Random;

public class Menu_utana extends AppCompatActivity implements View.OnClickListener{

    ImageView fotouser,gambarhorizon,gambarhorizon2;
    TextView nama, alamat,saldo,namahorizon,hargahorizon,namahorizon2,hargahorizon2;
    ProgressBar progressBar,progressBar2,progressBar3;

    DatabaseReference reference;
    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    String kategoritutor = null;
    String kategoridetail = null;
    String alamatpenjual = null;
    String namapenjual = null;
    String nohppenjual = null;

    String id1 = "",id2 = "";
    String idp1 = "",idp2 = "";

    Locale localeID = new Locale("in", "ID");
    NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_utana);
        getUsernameLocal();

        findViewById(R.id.tpaper).setOnClickListener(this);
        findViewById(R.id.btnabout).setOnClickListener(this);
        findViewById(R.id.tplastic).setOnClickListener(this);
        findViewById(R.id.tmetal).setOnClickListener(this);
        findViewById(R.id.twaste).setOnClickListener(this);
        findViewById(R.id.tglass).setOnClickListener(this);
        findViewById(R.id.market1).setOnClickListener(this);
        findViewById(R.id.market2).setOnClickListener(this);
        findViewById(R.id.gambarhorizon).setOnClickListener(this);
        findViewById(R.id.gambarhorizon2).setOnClickListener(this);

        findViewById(R.id.saldomenu).setOnClickListener(this);
        findViewById(R.id.btnjual).setOnClickListener(this);
        findViewById(R.id.btnabout).setOnClickListener(this);
        findViewById(R.id.fotouserhome).setOnClickListener(this);

        findViewById(R.id.namamenu).setOnClickListener(this);
        findViewById(R.id.alamatmenu).setOnClickListener(this);
        findViewById(R.id.ivsaldo).setOnClickListener(this);

        nama = findViewById(R.id.namamenu);
        alamat = findViewById(R.id.alamatmenu);
        saldo = findViewById(R.id.saldomenu);
        fotouser = findViewById(R.id.fotouserhome);
        progressBar = findViewById(R.id.progressbar);
        progressBar2 = findViewById(R.id.progressbar2);
        progressBar3 = findViewById(R.id.progressbar3);
        gambarhorizon = findViewById(R.id.gambarhorizon);
        namahorizon = findViewById(R.id.txhorizon);
        hargahorizon = findViewById(R.id.txhorizonharga);
        namahorizon2 = findViewById(R.id.txhorizon2);
        hargahorizon2 = findViewById(R.id.txhorizonharga2);
        gambarhorizon2 = findViewById(R.id.gambarhorizon2);

        getInformationFromDB();
        randomProduk();
    }

    private void getInformationFromDB() {
        reference = FirebaseDatabase.getInstance().getReference()
                .child("User").child(username_key_new);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    namapenjual = dataSnapshot.child("namalengkap").getValue().toString();
                    nama.setText(namapenjual);
                    alamatpenjual = dataSnapshot.child("alamat").getValue().toString();
                    alamat.setText(alamatpenjual);
                    nohppenjual = dataSnapshot.child("notelp").getValue().toString();
                    saldo.setText(formatRupiah.format(dataSnapshot.child("saldo").getValue()));

                    Picasso.with(Menu_utana.this)
                            .load(dataSnapshot.child("fotoprofil").getValue().toString()).centerCrop().fit()
                            .into(fotouser, new Callback() {
                                @Override
                                public void onSuccess() {
                                    progressBar.setVisibility(View.GONE);
                                }

                                @Override
                                public void onError() {

                                }
                            });
                }
                catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Terjadi Kesalahan", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void onClick(View v){
        switch (v.getId()) {
            case R.id.tpaper:
                Intent paper = new Intent(Menu_utana.this, tpaper.class);
                kategoritutor = "tpaper";
                kategoridetail = "PAPER";
                paper.putExtra("kategoridetail",kategoridetail);
                paper.putExtra("kategori", kategoritutor);
                startActivity(paper);
                break;
            case R.id.tplastic:
                Intent plastic = new Intent(Menu_utana.this, tpaper.class);
                kategoritutor = "tplastic";
                kategoridetail = "PLASTIC";
                plastic.putExtra("kategoridetail",kategoridetail);
                plastic.putExtra("kategori", kategoritutor);
                startActivity(plastic);
                break;
            case R.id.tmetal:
                Intent metal = new Intent(Menu_utana.this, tpaper.class);
                kategoritutor = "tmetal";
                kategoridetail = "METAL";
                metal.putExtra("kategoridetail",kategoridetail);
                metal.putExtra("kategori", kategoritutor);
                startActivity(metal);
                break;
            case R.id.twaste:
                Intent waste = new Intent(Menu_utana.this, tpaper.class);
                kategoritutor = "twaste";
                kategoridetail = "WASTE";
                waste.putExtra("kategoridetail",kategoridetail);
                waste.putExtra("kategori", kategoritutor);
                startActivity(waste);
                break;
            case R.id.tglass:
                Intent glass = new Intent(Menu_utana.this, tpaper.class);
                kategoritutor = "tglass";
                kategoridetail = "GLASS";
                glass.putExtra("kategoridetail",kategoridetail);
                glass.putExtra("kategori", kategoritutor);
                startActivity(glass);
                break;
            case R.id.saldomenu:
                goSaldo();
                break;
            case R.id.ivsaldo:
                goSaldo();
                break;
            case R.id.gambarhorizon:
                if (id1.equals("")){
                    break;
                }
                if (idp1.equals("")){
                    break;
                }
                else {
                    Intent itemone = new Intent(Menu_utana.this, mainmarket.class);
                    itemone.putExtra("id",id1);
                    itemone.putExtra("idpenjual",idp1);
                    startActivity(itemone);
                    break;
                }
            case R.id.gambarhorizon2:
                if (id2.equals("")){
                    break;
                }
                if (idp2.equals("")){
                    break;
                }
                else {
                    Intent itemtwo = new Intent(Menu_utana.this, mainmarket.class);
                    itemtwo.putExtra("id",id2);
                    itemtwo.putExtra("idpenjual",idp2);
                    startActivity(itemtwo);
                    break;
                }
            case R.id.fotouserhome:
                goProfil();
                break;
            case R.id.namamenu:
                goProfil();
                break;
            case R.id.alamatmenu:
                goProfil();
                break;
            case R.id.btnjual:
                if (namapenjual == null){
                    break;
                }
                else if (alamatpenjual == null){
                    break;
                }
                else if (nohppenjual == null){
                    break;
                }
                else {
                    Intent gojual = new Intent(Menu_utana.this, Daftar_Jual.class);
                    gojual.putExtra("alamat",alamatpenjual);
                    gojual.putExtra("nama",namapenjual);
                    gojual.putExtra("nohp",nohppenjual);
                    gojual.putExtra("username",username_key_new);
                    startActivity(gojual);
                    break;
                }
            case R.id.market1:
                goMarket();
                break;
            case R.id.market2:
                goMarket();
                break;
            case R.id.btnabout:
                Intent about = new Intent(Menu_utana.this, about.class);
                startActivity(about);
                break;
        }
    }

    private void goSaldo() {
        Intent gosaldo = new Intent(Menu_utana.this, Saldo.class);
        startActivity(gosaldo);
    }


    private void goMarket() {
        Intent gomarket = new Intent(Menu_utana.this, mpaper.class);
        startActivity(gomarket);
    }


    private void goProfil() {
        Intent goprofil = new Intent(Menu_utana.this, Profile.class);
        startActivity(goprofil);
    }

    private void randomProduk() {
        reference = FirebaseDatabase.getInstance().getReference()
                .child("Market");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Random random = new Random();
                    int index = random.nextInt((int) dataSnapshot.getChildrenCount());
                    int index2 = random.nextInt((int) dataSnapshot.getChildrenCount());
                    int count = 0;
                    index++;
                    index2++;
                    if (index2 == index){
                        index2++;
                    }
                    if (index2 > (int)dataSnapshot.getChildrenCount()) {
                        index2=1;
                    }
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        count++;
                        if (count == index) {
                            id1 = snapshot.child("iklanID").getValue().toString();
                            idp1 = snapshot.child("iklanPenjualID").getValue().toString();
                            namahorizon.setText(snapshot.child("iklanJudul").getValue().toString());
                            hargahorizon.setText(formatRupiah.format(snapshot.child("iklanHarga").getValue()));
                            Picasso.with(Menu_utana.this)
                                    .load(snapshot.child("iklanFoto").getValue().toString()).centerCrop().fit()
                                    .into(gambarhorizon, new Callback() {
                                        @Override
                                        public void onSuccess() {
                                            progressBar2.setVisibility(View.GONE);
                                        }

                                        @Override
                                        public void onError() {

                                        }
                                    });
                        }
                        if (count == index2) {
                            id2 = snapshot.child("iklanID").getValue().toString();
                            idp2 = snapshot.child("iklanPenjualID").getValue().toString();
                            namahorizon2.setText(snapshot.child("iklanJudul").getValue().toString());
                            hargahorizon2.setText(formatRupiah.format(snapshot.child("iklanHarga").getValue()));
                            Picasso.with(Menu_utana.this)
                                    .load(snapshot.child("iklanFoto").getValue().toString()).centerCrop().fit()
                                    .into(gambarhorizon2, new Callback() {
                                        @Override
                                        public void onSuccess() {
                                            progressBar3.setVisibility(View.GONE);
                                        }

                                        @Override
                                        public void onError() {

                                        }
                                    });
                        }
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(),"Terjadi Kesalahan", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key,"");
    }
}
