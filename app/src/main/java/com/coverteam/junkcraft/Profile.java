package com.coverteam.junkcraft;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.coverteam.junkcraft.adapter.AdapterCheckout;
import com.coverteam.junkcraft.adapter.AdapterIklan;
import com.coverteam.junkcraft.adapter.AdapterSaldo;
import com.coverteam.junkcraft.model.DataCheckout;
import com.coverteam.junkcraft.model.DataIklan;
import com.coverteam.junkcraft.model.DataSaldo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class Profile extends AppCompatActivity implements View.OnClickListener{

    TextView namauser, alamatuser,saldouser;
    ImageView fotouser;
    ProgressBar progressBar;
    ListView listViewCheckout,listViewIklan,listViewPesanan;
    RecyclerView recyclerViewCheckout;

    DatabaseReference reference;
    DatabaseReference databaseCheckout;
    DatabaseReference databaseMarket;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";
    String passnama,passalamat,passnohp;

    private List<DataCheckout> listItems;
    private List<DataIklan> listItems2;
    private List<DataCheckout> listItems3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getUsernameLocal();
        listItems = new ArrayList<>();
        listItems2 = new ArrayList<>();
        listItems3 = new ArrayList<>();

        databaseCheckout = FirebaseDatabase.getInstance().getReference("Checkout");
        databaseMarket = FirebaseDatabase.getInstance().getReference("Market");

        namauser = findViewById(R.id.tvnamaprofil);
        alamatuser = findViewById(R.id.tvalamatprofil);
        fotouser = findViewById(R.id.imvfotoprofil);
        saldouser = findViewById(R.id.tvsaldoprofil);
        progressBar = findViewById(R.id.progressbar);
        listViewCheckout = findViewById(R.id.listviewCheckout);

        listViewIklan = findViewById(R.id.listviewIklan);
        listViewPesanan = findViewById(R.id.listviewPesanan);


        findViewById(R.id.button_editprofil).setOnClickListener(this);
        findViewById(R.id.buttonbackhome).setOnClickListener(this);
        findViewById(R.id.button_logout).setOnClickListener(this);
        findViewById(R.id.tvsaldoprofil).setOnClickListener(this);
        findViewById(R.id.tvnamaprofil).setOnClickListener(this);
        findViewById(R.id.tvalamatprofil).setOnClickListener(this);

        getInformationFromDB();
        findProduk("checkoutUsernamePelanggan",username_key_new);
        findProduk2("iklanPenjualID",username_key_new);
        findProduk3("checkoutUsernamePenjual",username_key_new);
        selectListView();
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_editprofil:
                goProfil();
                break;
            case R.id.tvnamaprofil:
                goProfil();
                break;
            case R.id.tvalamatprofil:
                goProfil();
                break;
            case R.id.buttonbackhome:
                Intent gohome = new Intent(Profile.this, Menu_utana.class);
                startActivity(gohome);
                break;
            case R.id.tvsaldoprofil:
                Intent gosaldo = new Intent(Profile.this, Saldo.class);
                startActivity(gosaldo);
                break;
            case R.id.button_logout:
                logout();
                break;
        }
    }

    private void goProfil() {
        Intent goeditprofil = new Intent(Profile.this, EditProfil.class);
        startActivity(goeditprofil);
    }

    private void selectListView() {
        listViewCheckout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DataCheckout posisidata = listItems.get(position);
                Intent intent = new Intent(Profile.this, Checkout_pembelian.class);
                intent.putExtra("checkoutid",posisidata.getCheckoutID());
                startActivity(intent);
            }
        });

        listViewPesanan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DataCheckout posisidata = listItems3.get(position);
                Intent intent = new Intent(Profile.this, iklan_pesanan.class);
                intent.putExtra("checkoutid",posisidata.getCheckoutID());
                startActivity(intent);
            }
        });
        listViewIklan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DataIklan posisidata = listItems2.get(position);
                Intent intent = new Intent(Profile.this, Edit_Jual.class);
                intent.putExtra("iklanid",posisidata.getIklanID());
                intent.putExtra("iklanpenjual",passnama);
                intent.putExtra("iklanalamat",passalamat);
                intent.putExtra("iklannohp",passnohp);
                startActivity(intent);
            }
        });
    }

    private void findProduk3(String id, String name) {
        Query searchQuery = databaseCheckout.orderByChild(id).equalTo(name);
        searchQuery.addListenerForSingleValueEvent(valueEventListener3);
    }

    private void findProduk2(String id, String name) {
        Query searchQuery = databaseMarket.orderByChild(id).equalTo(name);
        searchQuery.addListenerForSingleValueEvent(valueEventListener2);
    }

    private void findProduk(String id, String name) {
        Query searchQuery = databaseCheckout.orderByChild(id).equalTo(name);
        searchQuery.addListenerForSingleValueEvent(valueEventListener);
    }

    ValueEventListener valueEventListener = new ValueEventListener(){
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            LinearLayout komunitas404 = findViewById(R.id.komunitas_not_found);
            listItems.clear();
            if (dataSnapshot.exists()){
                komunitas404.setVisibility(View.GONE);
                listViewCheckout.setVisibility(View.VISIBLE);
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    DataCheckout dataCheckout = snapshot.getValue(DataCheckout.class);
                    listItems.add(dataCheckout);
                }
                AdapterCheckout adapter = new AdapterCheckout(Profile.this,listItems);
                Collections.reverse(listItems); // ADD THIS LINE TO REVERSE ORDER!
                adapter.notifyDataSetChanged();
                listViewCheckout.setAdapter(adapter);

            }
            else {
                listViewCheckout.setVisibility(View.GONE);
                komunitas404.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    ValueEventListener valueEventListener2 = new ValueEventListener(){
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            LinearLayout komunitas404 = findViewById(R.id.komunitas_not_found2);
            listItems2.clear();
            if (dataSnapshot.exists()){
                komunitas404.setVisibility(View.GONE);
                listViewIklan.setVisibility(View.VISIBLE);
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    DataIklan dataIklan = snapshot.getValue(DataIklan.class);
                    listItems2.add(dataIklan);
                }
                AdapterIklan adapter = new AdapterIklan(Profile.this,listItems2);
                Collections.reverse(listItems2);
                adapter.notifyDataSetChanged();
                listViewIklan.setAdapter(adapter);
            }
            else {
                listViewIklan.setVisibility(View.GONE);
                komunitas404.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    ValueEventListener valueEventListener3 = new ValueEventListener(){
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            LinearLayout komunitas404 = findViewById(R.id.komunitas_not_found3);
            listItems3.clear();
            if (dataSnapshot.exists()){
                komunitas404.setVisibility(View.GONE);
                listViewPesanan.setVisibility(View.VISIBLE);
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    DataCheckout dataCheckout = snapshot.getValue(DataCheckout.class);
                    listItems3.add(dataCheckout);
                }
                AdapterCheckout adapter = new AdapterCheckout(Profile.this,listItems3);
                Collections.reverse(listItems3);
                adapter.notifyDataSetChanged();
                listViewPesanan.setAdapter(adapter);
            }
            else {
                listViewPesanan.setVisibility(View.GONE);
                komunitas404.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private void getInformationFromDB() {
        reference = FirebaseDatabase.getInstance().getReference()
                .child("User").child(username_key_new);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                passnama = dataSnapshot.child("namalengkap").getValue().toString();
                namauser.setText(passnama);
                passalamat = dataSnapshot.child("alamat").getValue().toString();
                alamatuser.setText(passalamat);
                passnohp = dataSnapshot.child("notelp").getValue().toString();

                Locale localeID = new Locale("in", "ID");
                NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
                saldouser.setText(formatRupiah.format(dataSnapshot.child("saldo").getValue()));
                Picasso.with(Profile.this)
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

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void logout(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog_Alert);
        alertDialogBuilder
                .setTitle("Keluar dari JunkCraft")
                .setMessage("Apakah Anda ingin keluar?")
                .setCancelable(true)
                .setPositiveButton("Keluar",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY,MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(username_key,null);
                        editor.apply();
                        Intent gologin = new Intent(Profile.this, Login.class);
                        gologin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(gologin);
                        finish();
                    }
                })
                .setNegativeButton("Batal",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.dismiss();
                    }
                });
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        TextView pesan = alertDialog.findViewById(android.R.id.message);
        pesan.setTextSize(15);

        Button b = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        b.setTextColor(getResources().getColor(R.color.green1));

        Button c = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        c.setTextColor(getResources().getColor(R.color.black));
    }

    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key,"");
    }
}
