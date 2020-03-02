package com.coverteam.junkcraft;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Register_dua extends AppCompatActivity implements View.OnClickListener {

    ImageView image_dummy;
    EditText nama, notelp;

    Uri photolocation;
    Integer photomax = 1;

    DatabaseReference reference;
    StorageReference storage;
    ProgressBar progressBar;
    Spinner spinner,spinner2;

    ArrayList<String> Kabupaten;
    ArrayList<String> KabupatenID;
    ArrayList<String> Kecamatan;
    String URLKabupaten = "http://dev.farizdotid.com/api/daerahindonesia/provinsi/18/kabupaten";
    String URLKecamatan = "http://dev.farizdotid.com/api/daerahindonesia/provinsi/kabupaten/";//1871/kecamatan";
    String kabupatenid;
    String kabupaten,kecamatan;
    String kabkec;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_dua);
        Kabupaten = new ArrayList<>();
        KabupatenID = new ArrayList<>();
        Kecamatan = new ArrayList<>();

        image_dummy = findViewById(R.id.dummyfoto);
        nama = findViewById(R.id.namauser);
        notelp = findViewById(R.id.teleponuser);
        progressBar = findViewById(R.id.progressbar);
        spinner = findViewById(R.id.spinnerkabupaten);
        spinner2 = findViewById(R.id.spinnerkecamatan);

        loadSpinnerDataKabupaten(URLKabupaten);

        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.button_daftar).setOnClickListener(this);
        findViewById(R.id.buttonaddphoto).setOnClickListener(this);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                kabupaten = spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
                kabupatenid = KabupatenID.get(spinner.getSelectedItemPosition());
                String a = URLKecamatan+kabupatenid+"/kecamatan";
                loadSpinnerDataKecamatan(a);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                kecamatan = spinner2.getItemAtPosition(spinner2.getSelectedItemPosition()).toString();
                kabkec = kecamatan+","+kabupaten.substring(4);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                Intent intent = new Intent(Register_dua.this, Register_satu.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.button_daftar:
                RegistrasiChecker2();
                break;
            case R.id.buttonaddphoto:
                findphoto();
                break;
        }
    }

    private void loadSpinnerDataKabupaten(String url) {
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject=new JSONObject(response);
                    if(jsonObject.getString("error").equals("false")){
                        JSONArray jsonArray=jsonObject.getJSONArray("kabupatens");
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject1=jsonArray.getJSONObject(i);
                            String kabupaten=jsonObject1.getString("nama");
                            String idkabupaten=jsonObject1.getString("id");
                            Kabupaten.add(kabupaten);
                            KabupatenID.add(idkabupaten);
                        }
                    }
                    spinner.setAdapter(new ArrayAdapter<>(Register_dua.this, android.R.layout.simple_spinner_dropdown_item, Kabupaten));
                }catch (JSONException e){e.printStackTrace();}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }

    private void loadSpinnerDataKecamatan(String url) {
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Kecamatan.clear();
                try{
                    JSONObject jsonObject=new JSONObject(response);
                    if(jsonObject.getString("error").equals("false")){
                        JSONArray jsonArray=jsonObject.getJSONArray("kecamatans");
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject1=jsonArray.getJSONObject(i);
                            String kecamatan=jsonObject1.getString("nama");
                            Kecamatan.add(kecamatan);
                        }
                    }
                    spinner2.setAdapter(new ArrayAdapter<>(Register_dua.this, android.R.layout.simple_spinner_dropdown_item, Kecamatan));
                }catch (JSONException e){e.printStackTrace();}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }

    private void saveuserInformation() {
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Intent intent = getIntent();
        final String usernamedaftar = intent.getStringExtra("username");
        editor.putString(username_key,usernamedaftar);
        editor.apply();
    }

    private void RegistrasiChecker2() {
        Intent intent = getIntent();
        final String usernamedaftar = intent.getStringExtra("username");
        final String passworddaftar = intent.getStringExtra("password");
        final String emaildaftar = intent.getStringExtra("email");
        String namaz = nama.getText().toString().trim();
        String notelpz = notelp.getText().toString().trim();

        if(validateInputs(namaz,notelpz)){
            progressBar.setVisibility(View.VISIBLE);
            //save firebase
            reference = FirebaseDatabase.getInstance().getReference()
                    .child("User").child(usernamedaftar);
            storage = FirebaseStorage.getInstance().getReference().child("Photousers").child(usernamedaftar);
            //valid foto
            if (photolocation != null){
                final StorageReference storageReference = storage.child(System.currentTimeMillis()+"."+
                        getfileextension(photolocation));
                storageReference.putFile(photolocation).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Uri downloadUrl = uri;
                                reference.getRef().child("password").setValue(passworddaftar);
                                reference.getRef().child("email").setValue(emaildaftar);
                                reference.getRef().child("fotoprofil").setValue(downloadUrl.toString());
                                reference.getRef().child("namalengkap").setValue(nama.getText().toString());
                                reference.getRef().child("alamat").setValue(kabkec.trim());
                                reference.getRef().child("notelp").setValue(notelp.getText().toString());
                                reference.getRef().child("saldo").setValue(0);
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                    }
                }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        saveuserInformation();
                        Intent intent = new Intent(Register_dua.this, Regis_berhasil.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
            }
            else {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Silahkan Pilih Foto", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean validateInputs(String namanya, String notelpnya) {
        if (namanya.isEmpty()){
            nama.setError("Nama Belum Diisi");
            nama.requestFocus();
            return false;
        }
        if (notelpnya.isEmpty()){
            notelp.setError("No HP/Whatsapp Belum Diisi");
            notelp.requestFocus();
            return false;
        }
        if (!notelpnya.substring(0,1).equals("0")){
            notelp.setError("Harap Isi No HP/Whatsapp Dengan Benar");
            notelp.requestFocus();
            return false;
        }
        if (notelpnya.length() < 9){
            notelp.setError("Harap Isi No HP/Whatsapp Dengan Benar");
            notelp.requestFocus();
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
            Picasso.with(this).load(photolocation).centerCrop().fit().into(image_dummy);

        }
    }
}
