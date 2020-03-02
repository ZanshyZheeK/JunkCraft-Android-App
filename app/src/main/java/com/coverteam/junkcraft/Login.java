package com.coverteam.junkcraft;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity implements View.OnClickListener{

    TextView btn_register;
    Button btn_login;
    EditText username, password;
    ProgressBar progressBar;

    DatabaseReference reference;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.usernamelogin);
        password = findViewById(R.id.passwordlogin);
        progressBar = findViewById(R.id.progressbar);

        findViewById(R.id.button_login).setOnClickListener(this);
        findViewById(R.id.button_register).setOnClickListener(this);

    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_login:
                getInformationFromDB();
                break;
            case R.id.button_register:
                Intent goregisterone = new Intent(Login.this, Register_satu.class);
                startActivity(goregisterone);
                break;
        }
    }

    private void getInformationFromDB() {
        final String password2 = password.getText().toString();
        String user = username.getText().toString().trim();
        String pass = password.getText().toString().trim();

        if (validateInputs(user,pass)){
            progressBar.setVisibility(View.VISIBLE);
            reference = FirebaseDatabase.getInstance().getReference()
                    .child("User").child(username.getText().toString());
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        progressBar.setVisibility(View.GONE);
                        String passwordfirebase = dataSnapshot.child("password").getValue().toString();
                        if(password2.equals(passwordfirebase)){
                            //save user local
                            saveuserInformation();
                            Intent gomenuutama = new Intent(Login.this, Menu_utana.class);
                            startActivity(gomenuutama);
                        }else{
                            Toast.makeText(getApplicationContext(),"Username/Password salah!",Toast.LENGTH_SHORT).show();
                            username.requestFocus();
                        }
                    }
                    else{
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),"Username/Password salah!",Toast.LENGTH_SHORT).show();
                        username.requestFocus();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
    public void onBackPressed() {
        //nothing bro
    }

    private void saveuserInformation() {
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(username_key,username.getText().toString());
        editor.apply();
    }

    private boolean validateInputs(String user, String pass) {
        if (user.isEmpty()){
            username.setError("Username Belum Diisi");
            username.requestFocus();
            return false;
        }
        if (pass.isEmpty()){
            password.setError("Password Belum Diisi");
            password.requestFocus();
            return false;
        }
        return true;
    }
}
