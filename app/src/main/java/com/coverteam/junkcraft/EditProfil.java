package com.coverteam.junkcraft;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EditProfil extends AppCompatActivity implements View.OnClickListener{

    ImageView fotouseredit;
    EditText namauser, alamatuser, notelpuser, emailuser, passworduser;
    ProgressBar progressBar;
    Spinner spinner,spinner2;

    Uri photolocation;
    Integer photomax = 1;

    DatabaseReference reference;
    StorageReference storage;

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
    String username_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil);
        Kabupaten = new ArrayList<>();
        KabupatenID = new ArrayList<>();
        Kecamatan = new ArrayList<>();

        getUsernameLocal();

        fotouseredit = findViewById(R.id.fotouseredit);
        namauser = findViewById(R.id.namauseredit);
        //alamatuser = findViewById(R.id.alamatuseredit);
        notelpuser = findViewById(R.id.telpuseredit);
        emailuser = findViewById(R.id.emailuseredit);
        passworduser = findViewById(R.id.passworduseredit);
        progressBar = findViewById(R.id.progressbar);
        spinner = findViewById(R.id.spinnerkabupaten);
        spinner2 = findViewById(R.id.spinnerkecamatan);

        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.buttoneditphoto).setOnClickListener(this);
        findViewById(R.id.button_simpan).setOnClickListener(this);

        getInformationFromDB();
        loadSpinnerDataKabupaten(URLKabupaten);

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
                Intent profil = new Intent(EditProfil.this, Profile.class);
                startActivity(profil);
                break;
            case R.id.buttoneditphoto:
                findphoto();
                break;
            case R.id.button_simpan:
                saveDataToDB();
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
                    spinner.setAdapter(new ArrayAdapter<>(EditProfil.this, android.R.layout.simple_spinner_dropdown_item, Kabupaten));
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
                    spinner2.setAdapter(new ArrayAdapter<>(EditProfil.this, android.R.layout.simple_spinner_dropdown_item, Kecamatan));
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

    private void getInformationFromDB() {
        progressBar.setVisibility(View.VISIBLE);
        reference = FirebaseDatabase.getInstance().getReference()
                .child("User").child(username_key_new);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                namauser.setText(dataSnapshot.child("namalengkap").getValue().toString());
                notelpuser.setText(dataSnapshot.child("notelp").getValue().toString());
                emailuser.setText(dataSnapshot.child("email").getValue().toString());
                passworduser.setText(dataSnapshot.child("password").getValue().toString());
                Picasso.with(EditProfil.this)
                        .load(dataSnapshot.child("fotoprofil")
                                .getValue().toString()).centerCrop().fit().into(fotouseredit);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void saveDataToDB() {
        String namaz = namauser.getText().toString().trim();
        String notelpz = notelpuser.getText().toString().trim();
        String emaillz = emailuser.getText().toString().trim();
        String passwordz = passworduser.getText().toString().trim();

        if(validateInputs(namaz,notelpz,emaillz,passwordz)){
            progressBar.setVisibility(View.VISIBLE);
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    dataSnapshot.getRef().child("namalengkap").setValue(namauser.getText().toString());
                    dataSnapshot.getRef().child("alamat").setValue(kabkec.trim());
                    dataSnapshot.getRef().child("notelp").setValue(notelpuser.getText().toString());
                    dataSnapshot.getRef().child("email").setValue(emailuser.getText().toString());
                    dataSnapshot.getRef().child("password").setValue(passworduser.getText().toString());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            if (photolocation != null){
                storage = FirebaseStorage.getInstance().getReference().child("Photousers").child(username_key_new);
                final StorageReference storageReference = storage.child(System.currentTimeMillis()+"."+
                        getfileextension(photolocation));
                storageReference.putFile(photolocation).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Uri downloadUrl = uri;
                                reference.getRef().child("fotoprofil").setValue(downloadUrl.toString());
                            }
                        });
                    }
                }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Data Profil Telah Diupdate", Toast.LENGTH_SHORT).show();
                        Intent profil = new Intent(EditProfil.this, Profile.class);
                        startActivity(profil);
                    }
                });
            }
            else{
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Data Profil Telah Diupdate", Toast.LENGTH_SHORT).show();
                Intent profil = new Intent(EditProfil.this, Profile.class);
                startActivity(profil);
            }
        }
    }

    private boolean validateInputs(String namanya, String notelpnya, String emailnya, String passwordnya) {
        if (namanya.isEmpty()) {
            namauser.setError("Nama Belum Diisi");
            namauser.requestFocus();
            return false;
        }
        if (notelpnya.isEmpty()){
            notelpuser.setError("No HP/Whatsapp Belum Diisi");
            notelpuser.requestFocus();
            return false;
        }
        if (!notelpnya.substring(0,1).equals("0")){
            notelpuser.setError("Harap Isi No HP/Whatsapp Dengan Benar");
            notelpuser.requestFocus();
            return false;
        }
        if (notelpnya.length() <= 9){
            notelpuser.setError("Harap Isi No HP/Whatsapp Dengan Benar");
            notelpuser.requestFocus();
            return false;
        }
        if (emailnya.isEmpty()) {
            emailuser.setError("Email Belum Diisi");
            emailuser.requestFocus();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emailnya).matches()){
            emailuser.setError("Mohon Isi Email Dengan Benar");
            emailuser.requestFocus();
            return false;
        }
        if (passwordnya.isEmpty()) {
            passworduser.setError("Password Belum Diisi");
            passworduser.requestFocus();
            return false;
        }
        if(passwordnya.length()<6){
            passworduser.setError("Panjang Minimum Password adalah 6");
            passworduser.requestFocus();
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
            Picasso.with(this).load(photolocation).centerCrop().fit().into(fotouseredit);
        }
    }

    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key,"");
    }
}
