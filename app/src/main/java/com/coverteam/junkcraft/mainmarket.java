package com.coverteam.junkcraft;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.hotspot2.pps.HomeSp;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.coverteam.junkcraft.model.DataIklan;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Locale;

public class mainmarket extends AppCompatActivity implements View.OnClickListener{

    TextView judul,desc,harga,bahan,namapenjual,nohp,alamat;
    ImageView foto;
    ProgressBar progressBar;

    String judulstring,descstring,nohpstring,fotostring;
    Long hargalong;

    String id,idpenjual;

    DatabaseReference databaseMarket;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmarket);

        findViewById(R.id.button_lanjut).setOnClickListener(this);
        findViewById(R.id.wa).setOnClickListener(this);

        judul = findViewById(R.id.txjudul);
        desc = findViewById(R.id.txjudulbahan);
        harga = findViewById(R.id.txharga);
        bahan = findViewById(R.id.txbahan);
        namapenjual = findViewById(R.id.txpenjual);
        nohp = findViewById(R.id.txnohp);
        alamat = findViewById(R.id.txalamat);
        foto = findViewById(R.id.headerf);
        progressBar = findViewById(R.id.progressbar);

        getInformationFromDB();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_lanjut:
                if (judulstring == null){
                    break;
                }
                else if (descstring == null){
                    break;
                }
                else if (hargalong == null){
                    break;
                }
                else if (fotostring == null){
                    break;
                }
                else if (idpenjual == null){
                    break;
                }
                else {
                    Intent intent = new Intent(mainmarket.this, MarketCheckout.class);
                    intent.putExtra("judul",judulstring);
                    intent.putExtra("desc",descstring);
                    intent.putExtra("harga",hargalong);
                    intent.putExtra("id",id);
                    intent.putExtra("nohp",nohpstring);
                    intent.putExtra("foto",fotostring);
                    intent.putExtra("idpenjual",idpenjual);
                    startActivity(intent);
                    break;
                }
            case R.id.wa:
                if (nohpstring == null) {
                    break;
                } else {
                    copyToClipBoard();
                    Intent browserIntent = new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://wa.me/62" + nohpstring.substring(1)));
                    startActivity(browserIntent);
                    break;
                }
        }
    }

    private void getInformationFromDB() {
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        idpenjual = intent.getStringExtra("idpenjual");
        databaseMarket = FirebaseDatabase.getInstance().getReference("Market").child(id);
        databaseMarket.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    judulstring = dataSnapshot.child("iklanJudul").getValue().toString();
                    judul.setText(judulstring);
                    descstring = dataSnapshot.child("iklanDeskripsi").getValue().toString();
                    desc.setText(descstring);
                    bahan.setText(dataSnapshot.child("iklanBahan").getValue().toString());
                    namapenjual.setText(dataSnapshot.child("iklanPenjual").getValue().toString());
                    nohpstring = dataSnapshot.child("iklanNoHP").getValue().toString();
                    nohp.setText(nohpstring);
                    alamat.setText(dataSnapshot.child("iklanAlamat").getValue().toString());

                    Locale localeID = new Locale("in", "ID");
                    NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
                    hargalong = (long)dataSnapshot.child("iklanHarga").getValue();
                    harga.setText(formatRupiah.format(hargalong));

                    fotostring = dataSnapshot.child("iklanFoto").getValue().toString();
                    Picasso.with(mainmarket.this)
                            .load(fotostring).fit()
                            .into(foto, new Callback() {
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
                    Toast.makeText(getApplicationContext(), "Terjadi Kesalahan", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void copyToClipBoard() {
        ClipboardManager clipMan = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", nohpstring);
        clipMan.setPrimaryClip(clip);
        Toast.makeText(getApplicationContext(), "No HP "+nohpstring+" Telah Disalin", Toast.LENGTH_LONG).show();
    }
}
