package com.coverteam.junkcraft;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CheckOut_Saldo0 extends AppCompatActivity implements View.OnClickListener{

    TextView stat,statdetail,tanggal,topup;
    ImageView btn_back,fototopup;
    Integer passposition;
    ProgressBar progressBar;
    String status;
    Button uploadtf,batalkan;

    Uri photolocation;
    Integer photomax = 1;
    DatabaseReference reference;
    StorageReference storage;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out__saldo0);

        getUsernameLocal();

        stat = findViewById(R.id.statusutama);
        statdetail = findViewById(R.id.statusdetail);
        tanggal = findViewById(R.id.tanggaltopup);
        topup = findViewById(R.id.jumlahtopup);
        fototopup = findViewById(R.id.fototopup);
        progressBar = findViewById(R.id.progressbar);
        uploadtf = findViewById(R.id.btnbuktitf);
        batalkan = findViewById(R.id.btntopupbatal);

        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.btnbuktitf).setOnClickListener(this);
        findViewById(R.id.btntopupbatal).setOnClickListener(this);
        findViewById(R.id.fototopup).setOnClickListener(this);

        Intent intent = getIntent();
        passposition = intent.getIntExtra("posisi",0);
        reference = FirebaseDatabase.getInstance().getReference("Saldo")
                .child(username_key_new).child(passposition.toString());
        getInformationFromDB();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                Intent goutama = new Intent(CheckOut_Saldo0.this, Saldo.class);
                startActivity(goutama);
                break;
            case R.id.fototopup:
                if (status.equals("transfer")){
                    findphoto();
                    break;
                }
                break;
            case R.id.btnbuktitf:
                sendBuktiTransfer();
                break;
            case R.id.btntopupbatal:
                batalTransfer();
                break;
        }
    }

    private void getInformationFromDB() {
        progressBar.setVisibility(View.VISIBLE);
        //Toast.makeText(getApplicationContext(), reference.toString(), Toast.LENGTH_SHORT).show();
        //addListenerForSingleValueEvent
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Locale localeID = new Locale("in", "ID");
                NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
                topup.setText(formatRupiah.format(dataSnapshot.child("saldoJumlah").getValue()));
                tanggal.setText(dataSnapshot.child("saldoTanggal").getValue().toString());
                status = dataSnapshot.child("saldoStatus").getValue().toString();
                if (status.equals("transfer")){
                    stat.setText("Permintaan Diterima");
                    stat.setTextColor(Color.parseColor("#BF55EC"));
                    statdetail.setText("Silahkan Transfer Sejumlah Uang sesuai dengan\nSaldo Top-up yang Anda Inputkan");
                    uploadtf.setVisibility(View.VISIBLE);
                    batalkan.setVisibility(View.VISIBLE);
                }
                else if (status.equals("verifikasi")){
                    stat.setText("Sedang Diverifikasi");
                    stat.setTextColor(Color.parseColor("#0099FF"));
                    statdetail.setText("Bukti Transfer sedang Diverifikasi\nSilahkan Tunggu");
                    Picasso.with(CheckOut_Saldo0.this)
                            .load(dataSnapshot.child("saldoFotoTransfer")
                                    .getValue().toString()).centerCrop().fit().into(fototopup);
                    uploadtf.setVisibility(View.GONE);
                    batalkan.setVisibility(View.GONE);
                }
                else if (status.equals("berhasil")){
                    stat.setText("Top-up Berhasil");
                    stat.setTextColor(Color.parseColor("#5ABD8C"));
                    statdetail.setText("Top-up Telah Diverifikasi dan Saldo\ntelah Ditambahkan. Selamat Berbelanja");
                    Picasso.with(CheckOut_Saldo0.this)
                            .load(dataSnapshot.child("saldoFotoTransfer")
                                    .getValue().toString()).centerCrop().fit().into(fototopup);
                    uploadtf.setVisibility(View.GONE);
                    batalkan.setVisibility(View.GONE);
                }
                else{
                    stat.setText("Top-up Dibatalkan");
                    stat.setTextColor(Color.parseColor("#D1206B"));
                    statdetail.setText("Top-up Telah Dibatalkan berdasarkan\nPermintaan Anda");
                    uploadtf.setVisibility(View.GONE);
                    batalkan.setVisibility(View.GONE);
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    private void batalTransfer() {
        progressBar.setVisibility(View.VISIBLE);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                reference.getRef().child("saldoStatus").setValue("batal");
                getInformationFromDB();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendBuktiTransfer() {
        progressBar.setVisibility(View.VISIBLE);
        storage = FirebaseStorage.getInstance().getReference().child("Photousers").child(username_key_new).child("BuktiTransfer");
        //valid foto
        if (photolocation != null) {
            final StorageReference storageReference = storage.child("BTF"+System.currentTimeMillis() + "." +
                    getfileextension(photolocation));
            storageReference.putFile(photolocation).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Uri downloadUrl = uri;
                            reference.getRef().child("saldoFotoTransfer").setValue(downloadUrl.toString());
                            reference.getRef().child("saldoStatus").setValue("verifikasi");
                            reference.getRef().child("saldoTanggal").setValue(generateDateTimeNow());
                        }
                    });
                }
            }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    Toast.makeText(getApplicationContext(), "Bukti Transfer berhasil dikirim!", Toast.LENGTH_SHORT).show();
                    getInformationFromDB();
                    progressBar.setVisibility(View.GONE);
                }
            });
        } else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "Silahkan Pilih Foto Bukti Transfer", Toast.LENGTH_SHORT).show();
        }
    }
    private static String generateDateTimeNow() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime;
    }

    public void findphoto(){
        Intent pic = new Intent();
        pic.setType("image/*");
        pic.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(pic,photomax);
    }

    String getfileextension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == photomax && resultCode == RESULT_OK && data != null && data.getData() != null){
            photolocation = data.getData();
            Picasso.with(this).load(photolocation).centerCrop().fit().into(fototopup);
        }
    }



    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key,"");
    }
}
