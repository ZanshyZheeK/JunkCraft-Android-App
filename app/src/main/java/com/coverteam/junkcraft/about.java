package com.coverteam.junkcraft;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class about extends AppCompatActivity {

    LinearLayout id_indra, id_zansy, id_aldi, id_bambang, id_dima;
    TextView idnama, idtugas, idig, idno,tin,tzan, tbam,tal,tdim;
    ImageView fin,fzan,fbam,fal,fdim, btnbackhome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        btnbackhome = findViewById(R.id.back);
        btnbackhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gohome = new Intent(about.this, Menu_utana.class);
                startActivity(gohome);
            }
        });

        id_indra = findViewById(R.id.indra);
        id_zansy = findViewById(R.id.zansy);
        id_aldi = findViewById(R.id.aldi);
        id_bambang = findViewById(R.id.bambang);
        id_dima = findViewById(R.id.dima);

        idnama = findViewById(R.id.nama);
        idig = findViewById(R.id.ig);
        idtugas = findViewById(R.id.tugas);
        idno = findViewById(R.id.no);
        fin = findViewById(R.id.findra);
        fzan = findViewById(R.id.fzansi);
        fbam = findViewById(R.id.fbambang);
        fal = findViewById(R.id.faldi);
        fdim = findViewById(R.id.fdima);
        tbam = findViewById(R.id.txbam);
        tin = findViewById(R.id.txin);
        tzan = findViewById(R.id.txzan);
        tdim = findViewById(R.id.txdim);
        tal = findViewById(R.id.txal);


        idnama.setText("Bambang Satrio G");
        idig.setText("dandisatriogandhi");
        idtugas.setText("Scrum Master");
        idno.setText("082280307807");
        tbam.setTextColor(Color.parseColor("#5ABD8C"));
        tin.setTextColor(Color.parseColor("#8d8d8d"));
        tal.setTextColor(Color.parseColor("#8d8d8d"));
        tdim.setTextColor(Color.parseColor("#8d8d8d"));
        tzan.setTextColor(Color.parseColor("#8d8d8d"));

        //muncul
        fbam.animate().translationY(0).alpha(1).setDuration(2000).start();
        //fbam.setEnabled(true);
        fbam.setVisibility(View.VISIBLE);

        //ilang
        fin.animate().alpha(0).setDuration(2000).start();
        //fin.setEnabled(false);
        fin.setVisibility(View.GONE);
        fal.animate().alpha(0).setDuration(2000).start();
        fal.setVisibility(View.GONE);
        fzan.animate().alpha(0).setDuration(2000).start();
        fzan.setVisibility(View.GONE);
        fdim.animate().alpha(0).setDuration(2000).start();
        fdim.setVisibility(View.GONE);

        id_indra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                idnama.setText("Indra Yulianto");
                idig.setText("indrayulianto_");
                idtugas.setText("Design UI/UX");
                idno.setText("082282265650");

                //muncul
                fin.animate().translationY(0).alpha(1).setDuration(2000).start();
                fin.setVisibility(View.VISIBLE);
                tin.setTextColor(Color.parseColor("#5ABD8C"));
                tal.setTextColor(Color.parseColor("#8d8d8d"));
                tbam.setTextColor(Color.parseColor("#8d8d8d"));
                tdim.setTextColor(Color.parseColor("#8d8d8d"));
                tzan.setTextColor(Color.parseColor("#8d8d8d"));

                //ilang
                fzan.animate().alpha(0).setDuration(2000).start();
                fzan.setVisibility(View.GONE);
                fbam.animate().alpha(0).setDuration(2000).start();
                fbam.setVisibility(View.GONE);
                fal.animate().alpha(0).setDuration(2000).start();
                fal.setVisibility(View.GONE);
                fdim.animate().alpha(0).setDuration(2000).start();
                fdim.setVisibility(View.GONE);
            }
        });

        id_zansy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                idnama.setText("Zanshy P");
                idig.setText("zanshyyyy");
                idtugas.setText("Back-End Programmer");
                idno.setText("085384541796");

                //muncul
                fzan.animate().translationY(0).alpha(1).setDuration(2000).start();
                fzan.setVisibility(View.VISIBLE);
                tzan.setTextColor(Color.parseColor("#5ABD8C"));
                tin.setTextColor(Color.parseColor("#8d8d8d"));
                tbam.setTextColor(Color.parseColor("#8d8d8d"));
                tdim.setTextColor(Color.parseColor("#8d8d8d"));
                tal.setTextColor(Color.parseColor("#8d8d8d"));

                //ilang
                fin.animate().alpha(0).setDuration(2000).start();
                fin.setVisibility(View.GONE);
                fbam.animate().alpha(0).setDuration(2000).start();
                fbam.setVisibility(View.GONE);
                fal.animate().alpha(0).setDuration(2000).start();
                fal.setVisibility(View.GONE);
                fdim.animate().alpha(0).setDuration(2000).start();
                fdim.setVisibility(View.GONE);
            }
        });

        id_aldi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                idnama.setText("Aldi Wijaya");
                idig.setText("aldifisabilillah");
                idtugas.setText("Analyst System");
                idno.setText("0895606108509");

                //muncul
                fal.animate().translationY(0).alpha(1).setDuration(2000).start();
                fal.setVisibility(View.VISIBLE);
                tal.setTextColor(Color.parseColor("#5ABD8C"));
                tin.setTextColor(Color.parseColor("#8d8d8d"));
                tbam.setTextColor(Color.parseColor("#8d8d8d"));
                tdim.setTextColor(Color.parseColor("#8d8d8d"));
                tzan.setTextColor(Color.parseColor("#8d8d8d"));

                //ilang
                fin.animate().alpha(0).setDuration(2000).start();
                fin.setVisibility(View.GONE);
                fbam.animate().alpha(0).setDuration(2000).start();
                fbam.setVisibility(View.GONE);
                fzan.animate().alpha(0).setDuration(2000).start();
                fzan.setVisibility(View.GONE);
                fdim.animate().alpha(0).setDuration(2000).start();
                fdim.setVisibility(View.GONE);
            }
        });

        id_bambang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                idnama.setText("Bambang Satrio G");
                idig.setText("dandisatriogandhi");
                idtugas.setText("Scrum Master");
                idno.setText("082280307807");

                //muncul
                fbam.animate().translationY(0).alpha(1).setDuration(2000).start();
                fbam.setVisibility(View.VISIBLE);
                tbam.setTextColor(Color.parseColor("#5ABD8C"));
                tin.setTextColor(Color.parseColor("#8d8d8d"));
                tal.setTextColor(Color.parseColor("#8d8d8d"));
                tdim.setTextColor(Color.parseColor("#8d8d8d"));
                tzan.setTextColor(Color.parseColor("#8d8d8d"));

                //ilang
                fin.animate().alpha(0).setDuration(2000).start();
                fin.setVisibility(View.GONE);
                fal.animate().alpha(0).setDuration(2000).start();
                fal.setVisibility(View.GONE);
                fzan.animate().alpha(0).setDuration(2000).start();
                fzan.setVisibility(View.GONE);
                fdim.animate().alpha(0).setDuration(2000).start();
                fdim.setVisibility(View.GONE);
            }
        });

        id_dima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                idnama.setText("Dima Musa");
                idig.setText("diimamusa");
                idtugas.setText("Designer");
                idno.setText("082377008045");

                //muncul
                fdim.animate().translationY(0).alpha(1).setDuration(2000).start();
                fdim.setVisibility(View.VISIBLE);
                tdim.setTextColor(Color.parseColor("#5ABD8C"));
                tin.setTextColor(Color.parseColor("#8d8d8d"));
                tbam.setTextColor(Color.parseColor("#8d8d8d"));
                tal.setTextColor(Color.parseColor("#8d8d8d"));
                tzan.setTextColor(Color.parseColor("#8d8d8d"));

                //ilang
                fin.animate().alpha(0).setDuration(2000).start();
                fin.setVisibility(View.GONE);
                fbam.animate().alpha(0).setDuration(2000).start();
                fbam.setVisibility(View.GONE);
                fzan.animate().alpha(0).setDuration(2000).start();
                fzan.setVisibility(View.GONE);
                fal.animate().alpha(0).setDuration(2000).start();
                fal.setVisibility(View.GONE);
            }
        });
    }
}
