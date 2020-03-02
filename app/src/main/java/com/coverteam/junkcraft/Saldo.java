package com.coverteam.junkcraft;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.LauncherActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.coverteam.junkcraft.adapter.AdapterMarket;
import com.coverteam.junkcraft.adapter.AdapterSaldo;
import com.coverteam.junkcraft.model.DataCheckout;
import com.coverteam.junkcraft.model.DataMarket;
import com.coverteam.junkcraft.model.DataSaldo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Saldo extends AppCompatActivity implements View.OnClickListener{

    ImageView btn_back;
    TextView txsaldo;
    EditText etsaldo;

    private LinearLayoutManager mManager;
    private RecyclerView recyclerView;
    private AdapterSaldo adapter;
    private List<DataSaldo> listItems;

    DatabaseReference reference;
    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saldo);
        getUsernameLocal();

        txsaldo = findViewById(R.id.saldo);
        etsaldo = findViewById(R.id.edit_topup);

        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.btntopup).setOnClickListener(this);

        recyclerView = findViewById(R.id.recyclerViewtopup);
        recyclerView.setHasFixedSize(true);

        mManager = new LinearLayoutManager(this);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLayoutManager(mManager);

        listItems = new ArrayList<>();

        getInformationFromDB();
        loadRecyclerViewData();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                Intent goutama = new Intent(Saldo.this, Menu_utana.class);
                startActivity(goutama);
                break;
            case R.id.btntopup:
                topupSaldo();
                break;
        }
    }

    private void getInformationFromDB() {
        reference = FirebaseDatabase.getInstance().getReference()
                .child("User").child(username_key_new);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Locale localeID = new Locale("in", "ID");
                NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
                txsaldo.setText(formatRupiah.format(dataSnapshot.child("saldo").getValue()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void topupSaldo() {
        final Integer pos = listItems.size() + 1;
        final String jumlahtopupstring = etsaldo.getText().toString();
        if(validateInputs(jumlahtopupstring)){
            final Double jumlahtopup = Double.parseDouble(jumlahtopupstring);
            if (jumlahtopup >= 10000){
                reference = FirebaseDatabase.getInstance().getReference("Saldo")
                        .child(username_key_new).child(pos.toString());

                //addValueEventListener
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().child("saldoTanggal").setValue(generateDateTimeNow());
                        dataSnapshot.getRef().child("saldoJumlah").setValue(jumlahtopup);
                        dataSnapshot.getRef().child("saldoFotoTransfer").setValue("");
                        dataSnapshot.getRef().child("saldoStatus").setValue("transfer");
                        dataSnapshot.getRef().child("saldoType").setValue("Top-up");
                        dataSnapshot.getRef().child("saldoIDTransaksi").setValue("");
                        Intent goctopupsaldo = new Intent(Saldo.this, CheckOut_Saldo0.class);
                        goctopupsaldo.putExtra("posisi",pos);
                        startActivity(goctopupsaldo);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            else {
                etsaldo.setError("Saldo Top-up minimal Rp10.000");
                etsaldo.requestFocus();
            }
        }
    }

    private boolean validateInputs(String jumlahtopup) {
        if (jumlahtopup.isEmpty()){
            etsaldo.setError("Saldo Belum Diisi");
            etsaldo.requestFocus();
            return false;
        }
        return true;
    }

    private void loadRecyclerViewData(){
        reference = FirebaseDatabase.getInstance().getReference()
                .child("Saldo").child(username_key_new);
        //addValueEventListener
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listItems = new ArrayList<>();
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    DataSaldo sld = dataSnapshot1.getValue(DataSaldo.class);
                    listItems.add(sld);
                }
                //Toast.makeText(getApplicationContext(), a.toString(), Toast.LENGTH_SHORT).show();
                adapter = new AdapterSaldo(listItems,getApplicationContext());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Terjadi Kesalahan", Toast.LENGTH_LONG).show();
            }
        });
    }

    private static String generateDateTimeNow() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime;
    }

    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key,"");
    }
}
