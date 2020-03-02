package com.coverteam.junkcraft;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SyncAdapterType;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.coverteam.junkcraft.adapter.AdapterTutor;
import com.coverteam.junkcraft.model.DataTutorial;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class tpaper extends AppCompatActivity {
    private DatabaseReference reference;

    ArrayList<DataTutorial> list;
    AdapterTutor adapter;

    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;

    ImageView btn_back;
    TextView textkategori;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tpaper);
        textkategori = findViewById(R.id.textkategori);

        Intent intent = getIntent();
        String kategori = intent.getStringExtra("kategori");
        String kategoridetail = intent.getStringExtra("kategoridetail");
        textkategori.setText(kategoridetail);

        btn_back = findViewById(R.id.back);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goutama = new Intent(tpaper.this, Menu_utana.class);
                startActivity(goutama);
            }
        });

        mRecycler = findViewById(R.id.list_tutorial);
        mRecycler.setHasFixedSize(true);

        mManager = new LinearLayoutManager(this);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        reference = FirebaseDatabase.getInstance().getReference("TutorialList").child(kategori);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new ArrayList<>();
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    DataTutorial ttr = dataSnapshot1.getValue(DataTutorial.class);
                    list.add(ttr);
                }
                adapter = new AdapterTutor(getApplicationContext(), list);
                mRecycler.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getApplicationContext(), "Terjadi kesalahan", Toast.LENGTH_LONG).show();

            }
        });

    }
}
