package com.coverteam.junkcraft;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class iklan_pesanan extends AppCompatActivity implements View.OnClickListener{

    ImageView btn_back,fotobarang;
    Button btn_konfirm;
    TextView status_bayar,judul,ket,jmlhbeli,totalbayar,idpembelian,tanggalpembelian,alamat,usernamepembeli;
    EditText noresi;

    String passid,status,statusnew,vresi;
    String idpenjual,idtransaksi;

    LinearLayout layoutalamat;

    DatabaseReference databaseCheckout;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iklan_pesanan);

        status_bayar = findViewById(R.id.statusbayar);
        btn_konfirm = findViewById(R.id.button_konfirmasi);
        judul = findViewById(R.id.judul);
        ket = findViewById(R.id.ket);
        jmlhbeli = findViewById(R.id.jmlhbeli);
        totalbayar = findViewById(R.id.totalbayar);
        idpembelian = findViewById(R.id.idpembelian);
        tanggalpembelian = findViewById(R.id.tanggalpembelian);
        alamat = findViewById(R.id.alamat);
        fotobarang = findViewById(R.id.fotobarang);
        layoutalamat = findViewById(R.id.layoutalamat);
        noresi = findViewById(R.id.noresi);
        usernamepembeli = findViewById(R.id.userpembeli);

        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.button_konfirmasi).setOnClickListener(this);

        Intent intent = getIntent();
        passid = intent.getStringExtra("checkoutid");
        databaseCheckout = FirebaseDatabase.getInstance().getReference("Checkout").child(passid);

        getDataCheckoutFromDB();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                Intent intent = new Intent(iklan_pesanan.this, Profile.class);
                startActivity(intent);
                break;
            case R.id.button_konfirmasi:
                Changestatus();
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
                    usernamepembeli.setText(dataSnapshot.child("checkoutUsernamePelanggan").getValue().toString());
                    judul.setText(dataSnapshot.child("checkoutJudul").getValue().toString());
                    ket.setText(dataSnapshot.child("checkoutDesc").getValue().toString());
                    jmlhbeli.setText("x"+dataSnapshot.child("checkoutJumlah").getValue().toString());
                    idtransaksi = dataSnapshot.child("checkoutIDPembelian").getValue().toString();
                    idpembelian.setText(idtransaksi);
                    tanggalpembelian.setText(dataSnapshot.child("checkoutDateTime").getValue().toString());
                    alamat.setText(dataSnapshot.child("checkoutAlamat").getValue().toString());
                    status = dataSnapshot.child("checkoutStatus").getValue().toString();
                    vresi = dataSnapshot.child("checkoutNoResi").getValue().toString();
                    idpenjual = dataSnapshot.child("checkoutUsernamePenjual").getValue().toString();
                    Checkstatus();
                    Picasso.with(iklan_pesanan.this)
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

    private void Checkstatus() {
        if (status.equals("Diproses")){
            status_bayar.setTextColor(Color.parseColor("#d1206b"));
            status_bayar.setText("Pesanan sedang diproses");
            statusnew = "Dikemas";
            btn_konfirm.setVisibility(View.VISIBLE);
        }
        else if (status.equals("Dikemas")){
            status_bayar.setTextColor(Color.parseColor("#d1206b"));
            status_bayar.setText("Pesanan telah dikonfirmasi dan harus dikirim\ndalam waktu kurang dari 3x24 Jam");
            layoutalamat.setVisibility(View.VISIBLE);
            statusnew = "Dikirim";
            btn_konfirm.setText("KIRIM PESANAN");
            btn_konfirm.setVisibility(View.VISIBLE);
        }
        else if (status.equals("Dikirim")){
            status_bayar.setTextColor(Color.parseColor("#5ABD8C"));
            status_bayar.setText("Pesanan telah dikirim ke pembeli\ndengan No Resi "+vresi);
            layoutalamat.setVisibility(View.GONE);
            btn_konfirm.setVisibility(View.GONE);
        }
        else {
            status_bayar.setTextColor(Color.parseColor("#5ABD8C"));
            status_bayar.setText("Pesanan sudah diterima");
        }
    }

    private void Changestatus() {
        final String resi = noresi.getText().toString().trim();
        databaseCheckout.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    if(status.equals("Dikemas")){
                        if (validateInputs(resi)){
                            dataSnapshot.getRef().child("checkoutNoResi").setValue(resi);
                            dataSnapshot.getRef().child("checkoutStatus").setValue(statusnew);
                            dataSnapshot.getRef().child("checkoutDateTime").setValue(generateDateTimeNow());
                            getDataCheckoutFromDB();
                            Toast.makeText(getApplicationContext(), "Status Pesanan Diperbarui!", Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        dataSnapshot.getRef().child("checkoutStatus").setValue(statusnew);
                        dataSnapshot.getRef().child("checkoutDateTime").setValue(generateDateTimeNow());
                        getDataCheckoutFromDB();
                        Toast.makeText(getApplicationContext(), "Status Pesanan Diperbarui!", Toast.LENGTH_LONG).show();
                    }
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

    private static String generateDateTimeNow() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime;
    }

    private boolean validateInputs(String alamatnya) {
        if (alamatnya.isEmpty()) {
            noresi.setError("Kode Resi belum Diisi");
            noresi.requestFocus();
            return false;
        }
        return true;
    }
}
