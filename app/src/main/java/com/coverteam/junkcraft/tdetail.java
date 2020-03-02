package com.coverteam.junkcraft;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.coverteam.junkcraft.Listener.IFirebaseLoadDone;
import com.coverteam.junkcraft.Transformer.DepthPageTransformer;
import com.coverteam.junkcraft.adapter.AdapterTutorSlide;
import com.coverteam.junkcraft.model.DataTutorialSlide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class tdetail extends AppCompatActivity implements IFirebaseLoadDone {

    ViewPager viewPager;
    AdapterTutorSlide adapterTutorSlide;
    TextView textkategoridetail;
    ImageView back;

    DatabaseReference reference;
    IFirebaseLoadDone iFirebaseLoadDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tdetail);
        textkategoridetail = findViewById(R.id.textkategoridetail);

        Intent intent = getIntent();
        String tutorial = intent.getStringExtra("tutorial");
        String kategori = intent.getStringExtra("kategoridetail");
        textkategoridetail.setText(kategori);

        viewPager = findViewById(R.id.pagertutor);
        reference = FirebaseDatabase.getInstance().getReference("Tutorial").child(tutorial);
        iFirebaseLoadDone = this;

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        loadTutor();
        viewPager.setPageTransformer(true,new DepthPageTransformer());
    }

    private void loadTutor() {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            List<DataTutorialSlide> dataTutorialSlides = new ArrayList<>();
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot tutorSnapShot:dataSnapshot.getChildren())
                    dataTutorialSlides.add(tutorSnapShot.getValue(DataTutorialSlide.class));
                iFirebaseLoadDone.onFirebaseLoadSuccess(dataTutorialSlides);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                iFirebaseLoadDone.onFirebaseLoadFailed(databaseError.getMessage());
            }
        });
    }

    @Override
    public void onFirebaseLoadSuccess(List<DataTutorialSlide> dataTutorialSlideList) {
        adapterTutorSlide = new AdapterTutorSlide(this,dataTutorialSlideList);
        viewPager.setAdapter(adapterTutorSlide);
    }

    @Override
    public void onFirebaseLoadFailed(String message) {
        Toast.makeText(this,""+message,Toast.LENGTH_SHORT).show();
    }
}
