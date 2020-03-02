package com.coverteam.junkcraft;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
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

public class Edit_Jual extends AppCompatActivity implements View.OnClickListener{

    String kategoriselected = null;
    String passid,passnama,passalamat,passnohp;
    EditText ejudul,edesc,ebahan,eharga;
    ImageView fotobarangnya;

    TextView tvpaper,tvmetal,tvplastic,tvwaste,tvglass;

    Uri photolocation;
    Integer photomax = 1;
    StorageReference storage;
    DatabaseReference databaseMarket;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_jual);

        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.fotobarang).setOnClickListener(this);
        findViewById(R.id.btnsaveiklan).setOnClickListener(this);
        findViewById(R.id.btndeleteiklan).setOnClickListener(this);

        findViewById(R.id.mpaper).setOnClickListener(this);
        findViewById(R.id.mmetal).setOnClickListener(this);
        findViewById(R.id.mplastic).setOnClickListener(this);
        findViewById(R.id.mglass).setOnClickListener(this);
        findViewById(R.id.mwaste).setOnClickListener(this);

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

        Intent intent = getIntent();
        passid = intent.getStringExtra("iklanid");
        passnama = intent.getStringExtra("iklanpenjual");
        passalamat = intent.getStringExtra("iklanalamat");
        passnohp = intent.getStringExtra("iklannohp");
        databaseMarket = FirebaseDatabase.getInstance().getReference("Market").child(passid);

        getInformationFromDB();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                Intent goprofil = new Intent(Edit_Jual.this, Profile.class);
                startActivity(goprofil);
                break;
            case R.id.fotobarang:
                findphoto();
                break;
            case R.id.btnsaveiklan:
                saveADonDB();
                break;
            case R.id.btndeleteiklan:
                deleteADonDB();
                break;
            case R.id.mpaper:
                kategoriPaper();
                break;
            case R.id.mmetal:
                kategoriMetal();
                break;
            case R.id.mplastic:
                kategoriPlastic();
                break;
            case R.id.mglass:
                kategoriGlass();
                break;
            case R.id.mwaste:
                kategoriWaste();
                break;
        }
    }

    private void kategoriWaste() {
        kategoriselected = "waste";
        tvpaper.setTextColor(Color.parseColor("#212720"));
        tvmetal.setTextColor(Color.parseColor("#212720"));
        tvplastic.setTextColor(Color.parseColor("#212720"));
        tvwaste.setTextColor(Color.parseColor("#5ABD8C"));
        tvglass.setTextColor(Color.parseColor("#212720"));
    }

    private void kategoriGlass() {
        kategoriselected = "glass";
        tvpaper.setTextColor(Color.parseColor("#212720"));
        tvmetal.setTextColor(Color.parseColor("#212720"));
        tvplastic.setTextColor(Color.parseColor("#212720"));
        tvwaste.setTextColor(Color.parseColor("#212720"));
        tvglass.setTextColor(Color.parseColor("#5ABD8C"));
    }

    private void kategoriPlastic() {
        kategoriselected = "plastic";
        tvpaper.setTextColor(Color.parseColor("#212720"));
        tvmetal.setTextColor(Color.parseColor("#212720"));
        tvplastic.setTextColor(Color.parseColor("#5ABD8C"));
        tvwaste.setTextColor(Color.parseColor("#212720"));
        tvglass.setTextColor(Color.parseColor("#212720"));
    }

    private void kategoriMetal() {
        kategoriselected = "metal";
        tvpaper.setTextColor(Color.parseColor("#212720"));
        tvmetal.setTextColor(Color.parseColor("#5ABD8C"));
        tvplastic.setTextColor(Color.parseColor("#212720"));
        tvwaste.setTextColor(Color.parseColor("#212720"));
        tvglass.setTextColor(Color.parseColor("#212720"));
    }

    private void kategoriPaper() {
        kategoriselected = "paper";
        tvpaper.setTextColor(Color.parseColor("#5ABD8C"));
        tvmetal.setTextColor(Color.parseColor("#212720"));
        tvplastic.setTextColor(Color.parseColor("#212720"));
        tvwaste.setTextColor(Color.parseColor("#212720"));
        tvglass.setTextColor(Color.parseColor("#212720"));
    }

    private void getInformationFromDB() {
        progressBar.setVisibility(View.VISIBLE);
        databaseMarket.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    ejudul.setText(dataSnapshot.child("iklanJudul").getValue().toString());
                    edesc.setText(dataSnapshot.child("iklanDeskripsi").getValue().toString());
                    ebahan.setText(dataSnapshot.child("iklanBahan").getValue().toString());
                    eharga.setText(dataSnapshot.child("iklanHarga").getValue().toString());
                    Picasso.with(Edit_Jual.this)
                            .load(dataSnapshot.child("iklanFoto")
                                    .getValue().toString()).centerCrop().fit().into(fotobarangnya);
                    kategoriselected = dataSnapshot.child("iklanKategori").getValue().toString();
                    if(kategoriselected.equals("paper")){
                        kategoriPaper();
                    }
                    else if (kategoriselected.equals("metal")){
                        kategoriMetal();
                    }
                    else if (kategoriselected.equals("plastic")){
                        kategoriPlastic();
                    }
                    else if (kategoriselected.equals("glass")){
                        kategoriGlass();
                    }
                    else {
                        kategoriWaste();
                    }
                    progressBar.setVisibility(View.GONE);
                }
                catch (Exception e){
                    //Toast.makeText(getApplicationContext(), "Terjadi Kesalahan", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void saveADonDB() {
        final String judul = ejudul.getText().toString().trim();
        final String desc = edesc.getText().toString().trim();
        final String bahan = ebahan.getText().toString().trim();
        final String hargainput = eharga.getText().toString().trim();
        final Double harga = Double.parseDouble(hargainput);

        if(validateInputs(judul,desc,bahan,hargainput)) {
            progressBar.setVisibility(View.VISIBLE);
            databaseMarket.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    databaseMarket.child("iklanJudul").setValue(judul);
                    databaseMarket.child("iklanDeskripsi").setValue(desc);
                    databaseMarket.child("iklanBahan").setValue(bahan);
                    databaseMarket.child("iklanHarga").setValue(harga);
                    databaseMarket.child("iklanPenjual").setValue(passnama);
                    databaseMarket.child("iklanAlamat").setValue(passalamat);
                    databaseMarket.child("iklanNoHP").setValue(passnohp);
                    databaseMarket.child("iklanKategori").setValue(kategoriselected);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            if (photolocation != null){
                storage = FirebaseStorage.getInstance().getReference().child("PhotoMarket").child(passid);
                final StorageReference storageReference = storage.child(System.currentTimeMillis()+"."+
                        getfileextension(photolocation));
                storageReference.putFile(photolocation).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Uri downloadUrl = uri;
                                databaseMarket.child("iklanFoto").setValue(downloadUrl.toString());
                            }
                        });
                    }
                }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Iklan Berhasil Diupdate!", Toast.LENGTH_SHORT).show();
                        Intent profil = new Intent(Edit_Jual.this, Profile.class);
                        startActivity(profil);
                    }
                });
            }
            else{
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Iklan Berhasil Diupdate!", Toast.LENGTH_SHORT).show();
                Intent profil = new Intent(Edit_Jual.this, Profile.class);
                startActivity(profil);
            }
        }
    }

    private void deleteADonDB() {
        databaseMarket.setValue(null);
        Toast.makeText(getApplicationContext(), "Iklan Berhasil Dihapus!", Toast.LENGTH_SHORT).show();
        Intent profil = new Intent(Edit_Jual.this, Profile.class);
        startActivity(profil);
        finish();
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
