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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ProgressBar;

import com.coverteam.junkcraft.model.DataIklan;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

public class Daftar_Jual extends AppCompatActivity implements View.OnClickListener{

    String kategoriselected = null;
    EditText ejudul,edesc,ebahan,eharga;
    ImageView fotobarangnya;

    TextView tvpaper,tvmetal,tvplastic,tvwaste,tvglass;

    Uri photolocation;
    Integer photomax = 1;
    DatabaseReference databaseMarket;
    StorageReference storage;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar__jual);

        databaseMarket = FirebaseDatabase.getInstance().getReference("Market");

        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.fotobarang).setOnClickListener(this);
        findViewById(R.id.btnpasangiklan).setOnClickListener(this);

        findViewById(R.id.mpaper).setOnClickListener(this);
        findViewById(R.id.mmetal).setOnClickListener(this);
        findViewById(R.id.mplastic).setOnClickListener(this);
        findViewById(R.id.mglass).setOnClickListener(this);
        findViewById(R.id.mwaste).setOnClickListener(this);
        kategoriselected = "paper";

        ejudul = findViewById(R.id.juduliklan);
        edesc = findViewById(R.id.desciklan);
        ebahan = findViewById(R.id.bahaniklan);
        eharga = findViewById(R.id.hargaiklan);
        fotobarangnya = findViewById(R.id.fotobarang);
        progressBar = findViewById(R.id.progressbar);
        tvpaper = findViewById(R.id.tvpaper);
        tvmetal = findViewById(R.id.tvmetal);
        tvplastic = findViewById(R.id.tvplastic);
        tvwaste = findViewById(R.id.tvwaste);
        tvglass = findViewById(R.id.tvglass);

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                Intent goutama = new Intent(Daftar_Jual.this, Menu_utana.class);
                startActivity(goutama);
                break;
            case R.id.mpaper:
                kategoriselected = "paper";
                tvpaper.setTextColor(Color.parseColor("#5ABD8C"));
                tvmetal.setTextColor(Color.parseColor("#212720"));
                tvplastic.setTextColor(Color.parseColor("#212720"));
                tvwaste.setTextColor(Color.parseColor("#212720"));
                tvglass.setTextColor(Color.parseColor("#212720"));
                break;
            case R.id.mmetal:
                kategoriselected = "metal";
                tvpaper.setTextColor(Color.parseColor("#212720"));
                tvmetal.setTextColor(Color.parseColor("#5ABD8C"));
                tvplastic.setTextColor(Color.parseColor("#212720"));
                tvwaste.setTextColor(Color.parseColor("#212720"));
                tvglass.setTextColor(Color.parseColor("#212720"));
                break;
            case R.id.mplastic:
                kategoriselected = "plastic";
                tvpaper.setTextColor(Color.parseColor("#212720"));
                tvmetal.setTextColor(Color.parseColor("#212720"));
                tvplastic.setTextColor(Color.parseColor("#5ABD8C"));
                tvwaste.setTextColor(Color.parseColor("#212720"));
                tvglass.setTextColor(Color.parseColor("#212720"));
                break;
            case R.id.mglass:
                kategoriselected = "glass";
                tvpaper.setTextColor(Color.parseColor("#212720"));
                tvmetal.setTextColor(Color.parseColor("#212720"));
                tvplastic.setTextColor(Color.parseColor("#212720"));
                tvwaste.setTextColor(Color.parseColor("#212720"));
                tvglass.setTextColor(Color.parseColor("#5ABD8C"));
                break;
            case R.id.mwaste:
                kategoriselected = "waste";
                tvpaper.setTextColor(Color.parseColor("#212720"));
                tvmetal.setTextColor(Color.parseColor("#212720"));
                tvplastic.setTextColor(Color.parseColor("#212720"));
                tvwaste.setTextColor(Color.parseColor("#5ABD8C"));
                tvglass.setTextColor(Color.parseColor("#212720"));
                break;
            case R.id.fotobarang:
                findphoto();
                break;
            case R.id.btnpasangiklan:
                saveADonDB();
                break;
        }
    }

    private void saveADonDB() {
        final String judul = ejudul.getText().toString().trim();
        final String desc = edesc.getText().toString().trim();
        final String bahan = ebahan.getText().toString().trim();
        final String hargainput = eharga.getText().toString().trim();

        if(validateInputs(judul,desc,bahan,hargainput)) {
            progressBar.setVisibility(View.VISIBLE);
            final Double harga = Double.parseDouble(hargainput);

            //save firebase
            databaseMarket = FirebaseDatabase.getInstance().getReference("Market");
            final String id = databaseMarket.push().getKey();
            storage = FirebaseStorage.getInstance().getReference().child("PhotoMarket").child(id);
            //valid foto
            if (photolocation != null) {
                final StorageReference storageReference = storage.child(System.currentTimeMillis() + "." +
                        getfileextension(photolocation));
                storageReference.putFile(photolocation).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Intent intent = getIntent();
                                String alamat = intent.getStringExtra("alamat");
                                String user = intent.getStringExtra("nama");
                                String nohp = intent.getStringExtra("nohp");
                                String userid = intent.getStringExtra("username");
                                Uri downloadUrl = uri;
                                DataIklan iklan = new DataIklan(id, judul, desc, bahan, downloadUrl.toString(), kategoriselected, user, userid, alamat, nohp, harga);
                                databaseMarket.child(id).setValue(iklan);
                            }
                        });
                    }
                }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        progressBar.setVisibility(View.GONE);
                        //Toast.makeText(getApplicationContext(), "Iklan berhasil didaftarkan!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Daftar_Jual.this, iklanberhasil.class);
                        startActivity(intent);
                        finish();
                    }
                });
            } else {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Silahkan Pilih Foto Barang", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean validateInputs(String judulnya, String descnya, String bahannya, String harganya) {
        if (judulnya.isEmpty()){
            ejudul.setError("Judul Barang Belum Diisi");
            ejudul.requestFocus();
            return false;
        }
        if (descnya.isEmpty()){
            edesc.setError("Deskripsi Barang Belum Diisi");
            edesc.requestFocus();
            return false;
        }
        if (bahannya.isEmpty()){
            ebahan.setError("Bahan Utama Belum Diisi");
            ebahan.requestFocus();
            return false;
        }
        if (harganya.isEmpty()){
            eharga.setError("Harga Barang Belum Diisi");
            eharga.requestFocus();
            return false;
        }
        return true;
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
            Picasso.with(this).load(photolocation).centerCrop().fit().into(fotobarangnya);
        }
    }
}
