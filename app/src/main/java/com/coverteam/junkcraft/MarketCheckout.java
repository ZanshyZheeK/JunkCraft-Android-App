package com.coverteam.junkcraft;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.coverteam.junkcraft.adapter.AdapterSaldo;
import com.coverteam.junkcraft.model.DataCheckout;
import com.coverteam.junkcraft.model.DataIklan;
import com.coverteam.junkcraft.model.DataSaldo;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class MarketCheckout extends AppCompatActivity implements View.OnClickListener{

    Button btn_sukses,btn_min,btn_plus;
    TextView jumlah,txtotalharga, txsaldo,txjudul,txdesc,txongkir,txhargaasli;
    EditText etxalamat;
    ImageView notice_uang;
    Integer nilaijum = 1;
    Integer posisisaldo;
    Long saldo = null;
    Integer harga = null;
    Integer total = null;
    Integer ongkir = 15000;
    String status = "Diproses";
    String NoResi = "";
    String idcheckout,idpembelian;

    DatabaseReference databasePembelian;
    DatabaseReference reference;
    ProgressBar progressBar;

    String passid,passjudul,passdesc,passnohp,passfoto,passidpenjual;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    Locale localeID = new Locale("in", "ID");
    NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_checkout);
        getUsernameLocal();
        getInformationFromDB();

        btn_min = findViewById(R.id.btnmin);
        btn_plus = findViewById(R.id.btnplus);
        btn_sukses = findViewById(R.id.button_lanjut);
        jumlah = findViewById(R.id.jumtiket);
        notice_uang = findViewById(R.id.notice);
        txsaldo = findViewById(R.id.txsaldo);
        txtotalharga = findViewById(R.id.total);
        txjudul = findViewById(R.id.judul);
        txdesc = findViewById(R.id.deskripsi);
        txongkir = findViewById(R.id.totalongkir);
        etxalamat = findViewById(R.id.alamatpengiriman);
        txhargaasli = findViewById(R.id.hargaasli);
        progressBar = findViewById(R.id.progressbar);

        findViewById(R.id.btnplus).setOnClickListener(this);
        findViewById(R.id.btnmin).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.button_lanjut).setOnClickListener(this);
        findViewById(R.id.notice).setOnClickListener(this);


        Intent intent = getIntent();
        passid = intent.getStringExtra("id");
        passjudul = intent.getStringExtra("judul");
        passdesc = intent.getStringExtra("desc");
        passnohp = intent.getStringExtra("nohp");
        passfoto = intent.getStringExtra("foto");
        passidpenjual = intent.getStringExtra("idpenjual");
        Long passharga = intent.getLongExtra("harga",0);

        harga = (int)(long)passharga;
        txjudul.setText(passjudul);
        txdesc.setText(passdesc);
        loadPosisiData();
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnplus:
                hitungTambah();
                break;
            case R.id.btnmin:
                hitungKurang();
                break;
            case R.id.back:
                onBackPressed();
                break;
            case R.id.button_lanjut:
                saveCheckoutOnDB();
                break;
            case R.id.notice:
                Intent gosaldo = new Intent(MarketCheckout.this, Saldo.class);
                startActivity(gosaldo);
                break;
        }
    }

    private void saveCheckoutOnDB() {
        String alamatpengirim = etxalamat.getText().toString();
        if(validateInputs(alamatpengirim)){
            progressBar.setVisibility(View.VISIBLE);
            databasePembelian = FirebaseDatabase.getInstance().getReference("Checkout");
            idcheckout = databasePembelian.push().getKey();
            idpembelian = getRandomString(8);
            DataCheckout checkout = new DataCheckout(idcheckout,idpembelian,passid,username_key_new,passidpenjual,generateDateTimeNow(),passjudul,passdesc,alamatpengirim,passnohp,passfoto,status,NoResi,nilaijum,total);
            databasePembelian.child(idcheckout).setValue(checkout);
            saveSaldoUser();
            saveSaldoHistory();
            progressBar.setVisibility(View.GONE);
            Intent intent = new Intent(MarketCheckout.this, pembelian_berhasil.class);
            intent.putExtra("checkoutid",idcheckout);
            startActivity(intent);
        }
    }

    private void saveSaldoHistory() {
        final Integer pos = posisisaldo + 1;
        reference = FirebaseDatabase.getInstance().getReference("Saldo")
                .child(username_key_new).child(pos.toString());

        //addValueEventListener
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().child("saldoTanggal").setValue(generateDateTimeNow());
                dataSnapshot.getRef().child("saldoJumlah").setValue(total);
                dataSnapshot.getRef().child("saldoFotoTransfer").setValue("");
                dataSnapshot.getRef().child("saldoStatus").setValue(idpembelian);
                dataSnapshot.getRef().child("saldoType").setValue("Beli");
                dataSnapshot.getRef().child("saldoIDTransaksi").setValue(idcheckout);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadPosisiData(){
        reference = FirebaseDatabase.getInstance().getReference()
                .child("Saldo").child(username_key_new);
        //addValueEventListener
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                posisisaldo = (int)dataSnapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Terjadi Kesalahan", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void saveSaldoUser() {
        reference = FirebaseDatabase.getInstance().getReference()
                .child("User").child(username_key_new);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    dataSnapshot.getRef().child("saldo").setValue(saldo-total);
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

    private boolean validateInputs(String alamatnya){
        if (alamatnya.isEmpty()){
            etxalamat.setError("Alamat Pengiriman Belum Diisi");
            etxalamat.requestFocus();
            return false;
        }
        return true;
    }

    private static final String ALLOWED_CHARACTERS ="0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static String getRandomString(final int sizeOfRandomString)
    {
        final Random random=new Random();
        final StringBuilder sb=new StringBuilder(sizeOfRandomString);
        for(int i=0;i<sizeOfRandomString;++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }

    private static String generateDateTimeNow() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime;
    }

    private void saldoToHargaChecker() {
        btn_min.setEnabled(false);
        btn_min.animate().alpha(0).setDuration(0).start();
        btn_min.setVisibility(View.VISIBLE);
        txhargaasli.setText(formatRupiah.format(harga));
        total = harga * nilaijum;
        total += ongkir;
        txongkir.setText(formatRupiah.format(ongkir));
        txtotalharga.setText(formatRupiah.format(total));
        if(total>saldo){
            btn_sukses.setEnabled(false);
            btn_sukses.animate().translationY(250).alpha(0).setDuration(350).start();
            txsaldo.setTextColor(Color.parseColor("#d1206b"));
            notice_uang.setVisibility(View.VISIBLE);
        }
    }

    private void hitungKurang() {
        nilaijum-=1;
        jumlah.setText(nilaijum.toString());
        if (nilaijum < 2){
            btn_min.setEnabled(false);
            btn_min.animate().alpha(0).setDuration(300).start();
        }
        total = harga * nilaijum;
        total += ongkir;
        txtotalharga.setText(formatRupiah.format(total));
        if(total<=saldo){
            btn_sukses.setEnabled(true);
            btn_sukses.animate().translationY(0).alpha(1).setDuration(350).start();
            txsaldo.setTextColor(Color.parseColor("#5ABD8C"));
            notice_uang.setVisibility(View.INVISIBLE);
        }
    }

    private void hitungTambah() {
        nilaijum+=1;
        jumlah.setText(nilaijum.toString());
        if (nilaijum > 1){
            btn_min.setEnabled(true);
            btn_min.animate().alpha(1).setDuration(300).start();
        }
        total = harga * nilaijum;
        total += ongkir;
        txtotalharga.setText(formatRupiah.format(total));
        if(total>saldo){
            btn_sukses.setEnabled(false);
            btn_sukses.animate().translationY(250).alpha(0).setDuration(350).start();
            txsaldo.setTextColor(Color.parseColor("#d1206b"));
            notice_uang.setVisibility(View.VISIBLE);
        }
    }

    private void getInformationFromDB() {
        databasePembelian = FirebaseDatabase.getInstance().getReference()
                .child("User").child(username_key_new);
        databasePembelian.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    saldo = (long)dataSnapshot.child("saldo").getValue();
                    txsaldo.setText(formatRupiah.format(saldo));
                    saldoToHargaChecker();
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

    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key,"");
    }
}
