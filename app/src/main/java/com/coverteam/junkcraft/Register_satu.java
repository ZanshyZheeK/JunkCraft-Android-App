package com.coverteam.junkcraft;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Register_satu extends AppCompatActivity implements View.OnClickListener{

    EditText email, password,password2, username;
    DatabaseReference reference;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_satu);

        username = findViewById(R.id.usernamelogin);
        email = findViewById(R.id.emailuser);
        password = findViewById(R.id.passworduser);
        password2 = findViewById(R.id.passworduser2);
        progressBar = findViewById(R.id.progressbar);
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.button_lanjut).setOnClickListener(this);

    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.back:
                Intent gologin = new Intent(Register_satu.this, Login.class);
                startActivity(gologin);
                break;
            case R.id.button_lanjut:
                RegistrasiChecker();
                break;
        }
    }

    private void RegistrasiChecker() {
        String user = username.getText().toString().trim();
        String pass = password.getText().toString().trim();
        String pass2 = password2.getText().toString().trim();
        String imel = email.getText().toString().trim();

        //check username to firebase
        if(validateInputs(user, pass,pass2,imel)){
            progressBar.setVisibility(View.VISIBLE);
            reference = FirebaseDatabase.getInstance().getReference()
                    .child("User").child(username.getText().toString());
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    progressBar.setVisibility(View.GONE);
                    if(dataSnapshot.exists()){
                        username.setError("Username Telah Teregistrasi!");
                        username.requestFocus();
                    }
                    else {
                        Intent intent = new Intent(Register_satu.this, Register_dua.class);
                        intent.putExtra("username",username.getText().toString());
                        intent.putExtra("email",email.getText().toString());
                        intent.putExtra("password",password.getText().toString());
                        startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private boolean validateInputs(String user, String pass,String pass2, String emailc){
        if (user.isEmpty()){
            username.setError("Username Belum Diisi");
            username.requestFocus();
            return false;
        }
        if (emailc.isEmpty()){
            email.setError("Email Belum Diisi");
            email.requestFocus();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emailc).matches()){
            email.setError("Mohon Isi Email Dengan Benar");
            email.requestFocus();
            return false;
        }
        if (pass.isEmpty()){
            password.setError("Password Belum Diisi");
            password.requestFocus();
            return false;
        }
        if (pass2.isEmpty()){
            password2.setError("Konfirmasi Password Belum Diisi");
            password2.requestFocus();
            return false;
        }
        if (pass.length()<6){
            password.setError("Panjang Minimum Password adalah 6");
            password.requestFocus();
            return false;
        }
        if (!pass.equals(pass2)){
            password2.setError("Konfirmasi Password tidak sama dengan Password");
            password2.requestFocus();
            return false;
        }
        return true;
    }
}
