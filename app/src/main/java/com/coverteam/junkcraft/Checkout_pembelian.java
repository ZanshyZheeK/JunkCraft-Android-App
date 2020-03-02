package com.coverteam.junkcraft;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.coverteam.junkcraft.model.DataCheckout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Checkout_pembelian extends AppCompatActivity implements View.OnClickListener{

    ImageView btn_back,fotobarang;
    Button btn_konfirm;
    TextView status_bayar,judul,ket,jmlhbeli,totalbayar,idpembelian,tanggalpembelian,alamat;

    String passid,notelp,status,noresi,usernamepenjual,idtransaksi;
    Long saldopenjual,total;
    Integer posisisaldo;

    DatabaseReference reference;
    DatabaseReference databaseCheckout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_pembelian);

        databaseCheckout = FirebaseDatabase.getInstance().getReference("Checkout");

        status_bayar = findViewById(R.id.statusbayar);
        btn_konfirm = findViewById(R.id.button_dashboar);
        judul = findViewById(R.id.judul);
        ket = findViewById(R.id.ket);
        jmlhbeli = findViewById(R.id.jmlhbeli);
        totalbayar = findViewById(R.id.totalbayar);
        idpembelian = findViewById(R.id.idpembelian);
        tanggalpembelian = findViewById(R.id.tanggalpembelian);
        alamat = findViewById(R.id.alamat);
        fotobarang = findViewById(R.id.fotobarang);

        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.buttonwa).setOnClickListener(this);
        findViewById(R.id.button_dashboar).setOnClickListener(this);

        Intent intent = getIntent();
        passid = intent.getStringExtra("checkoutid");
        databaseCheckout = FirebaseDatabase.getInstance().getReference("Checkout").child(passid);

        getDataCheckoutFromDB();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                Intent goutama = new Intent(Checkout_pembelian.this, Profile.class);
                startActivity(goutama);
                break;
            case R.id.buttonwa:
                if (notelp == null){
                    break;
                }
                else{
                    Intent browserIntent = new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://wa.me/62"+notelp.substring(1)));
                    startActivity(browserIntent);
                    break;
                }
            case R.id.button_dashboar:
                pesananDiterima();
                break;
        }
    }
    private void getDataCheckoutFromDB() {
        databaseCheckout.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    Locale localeID = new Locale("in", "ID");
                    NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
                    usernamepenjual = dataSnapshot.child("checkoutUsernamePenjual").getValue().toString();
                    judul.setText(dataSnapshot.child("checkoutJudul").getValue().toString());
                    ket.setText(dataSnapshot.child("checkoutDesc").getValue().toString());
                    jmlhbeli.setText("x"+dataSnapshot.child("checkoutJumlah").getValue().toString());
                    idtransaksi = dataSnapshot.child("checkoutIDPembelian").getValue().toString();
                    idpembelian.setText(idtransaksi);
                    tanggalpembelian.setText(dataSnapshot.child("checkoutDateTime").getValue().toString());
                    alamat.setText(dataSnapshot.child("checkoutAlamat").getValue().toString());
                    notelp = dataSnapshot.child("checkoutNoHP").getValue().toString();
                    total = (long)dataSnapshot.child("checkoutTotalHarga").getValue();
                    totalbayar.setText(formatRupiah.format(total));
                    status = dataSnapshot.child("checkoutStatus").getValue().toString();
                    noresi = dataSnapshot.child("checkoutNoResi").getValue().toString();
                    loadPosisiData();
                    Checkstatus();
                    Picasso.with(Checkout_pembelian.this)
                            .load(dataSnapshot.child("checkoutFotoBarang").getValue().toString()).centerCrop().fit()
                            .into(fotobarang, new Callback() {
                                @Override
                                public void onSuccess() {

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

    private void loadPosisiData(){
        reference = FirebaseDatabase.getInstance().getReference()
                .child("Saldo").child(usernamepenjual);
        //addValueEventListener
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                posisisaldo = (int)dataSnapshot.getChildrenCount();
                Toast.makeText(getApplicationContext(), Integer.toString(posisisaldo), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Terjadi Kesalahan", Toast.LENGTH_LONG).show();
            }
        });
    }
    private void saveSaldoHistory() {
        final Integer pos = posisisaldo + 1;
        reference = FirebaseDatabase.getInstance().getReference()
                .child("Saldo").child(usernamepenjual).child(pos.toString());

        //addValueEventListener
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().child("saldoTanggal").setValue(generateDateTimeNow());
                dataSnapshot.getRef().child("saldoJumlah").setValue(total);
                dataSnapshot.getRef().child("saldoFotoTransfer").setValue("");
                dataSnapshot.getRef().child("saldoStatus").setValue(idtransaksi);
                dataSnapshot.getRef().child("saldoType").setValue("Jual");
                dataSnapshot.getRef().child("saldoIDTransaksi").setValue(passid);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void Checkstatus() {
        if (status.equals("Diproses")){
            status_bayar.setTextColor(Color.parseColor("#d1206b"));
            status_bayar.setText("Pesanan sedang diproses");
        }
        else if (status.equals("Dikemas")){
            status_bayar.setTextColor(Color.parseColor("#d1206b"));
            status_bayar.setText("Pesanan telah dikonfirmasi penjual dan akan dikemas");
        }
        else if (status.equals("Dikirim")){
            status_bayar.setTextColor(Color.parseColor("#5ABD8C"));
            status_bayar.setText("Pesanan telah dikirim oleh penjual\ndengan No Resi "+noresi);
            btn_konfirm.setVisibility(View.VISIBLE);
            btn_konfirm.setEnabled(true);
            btn_konfirm.animate().translationY(0).alpha(1).setDuration(350).start();
        }
        else {
            status_bayar.setTextColor(Color.parseColor("#5ABD8C"));
            status_bayar.setText("Pesanan sudah diterima");
        }
    }

    private void pesananDiterima() {
        databaseCheckout.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    dataSnapshot.getRef().child("checkoutStatus").setValue("Diterima");
                    dataSnapshot.getRef().child("checkoutDateTime").setValue(generateDateTimeNow());
                    btn_konfirm.setEnabled(false);
                    btn_konfirm.animate().translationY(250).alpha(0).setDuration(350).start();
                    status_bayar.setTextColor(Color.parseColor("#5ABD8C"));
                    status_bayar.setText("Pesanan sudah diterima");
                    saveSaldoPenjual();
                    saveSaldoHistory();
                    getDataCheckoutFromDB();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Terjadi Kesalahan", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void saveSaldoPenjual() {
        reference = FirebaseDatabase.getInstance().getReference()
                .child("User").child(usernamepenjual);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    saldopenjual = (long)dataSnapshot.child("saldo").getValue();
                    dataSnapshot.getRef().child("saldo").setValue(saldopenjual+total);
                }
                catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Terjadi Kesalahan ngurang", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private static String generateDateTimeNow() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime;
    }
}
