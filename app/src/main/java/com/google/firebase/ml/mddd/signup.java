package com.google.firebase.ml.mddd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class signup extends AppCompatActivity {
    ProgressBar progressBar;
    EditText editTextEmail, editTextPassword,conpass;
    EditText editName,Editphone,EditCountry;
    private FirebaseAuth mAuth;
    DatabaseReference dbreference;
    user_data ud;
    String gen;
    String name,country,phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);
        conpass = findViewById(R.id.conpassword);
        LoginActivity.check = true;
        editName = findViewById(R.id.name);
        Editphone = findViewById(R.id.phone);
        EditCountry = findViewById(R.id.con);

        progressBar = (ProgressBar) findViewById(R.id.pro1);

        mAuth = FirebaseAuth.getInstance();
        //mAuth.signOut();

       dbreference = FirebaseDatabase.getInstance().getReference("Userdata");
    }
    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String errpass = conpass.getText().toString().trim();
        name = editName.getText().toString().trim();
        phone = Editphone.getText().toString().trim();
        country = EditCountry.getText().toString().trim();
        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please enter a valid email");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("Minimum lenght of password should be 6");
            editTextPassword.requestFocus();
            return;
        }
       if(!password.equals(errpass))
        {
            conpass.setError("Not matching the password");
            conpass.requestFocus();
            return;
       }
        if (name.isEmpty()) {
            editName.setError("Email is required");
            editName.requestFocus();
            return;
        }

        if (country.isEmpty()) {
            EditCountry.setError("Country name required");
            EditCountry.requestFocus();
            return;
        }

        if (phone.isEmpty()) {
            Editphone.setError("Password is required");
            Editphone.requestFocus();
            return;
        }

        user_data user1 = new user_data(name,email,phone,gen,country,"");

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    progressBar.setVisibility(View.VISIBLE);
                    String uid = mAuth.getCurrentUser().getUid();
                    dbreference.child(uid).setValue(user1).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){

                                Intent i = new Intent(signup.this,profile.class);
                                startActivity(i);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                finish();
                            }
                            else {
                                Toast.makeText(signup.this, "problem", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {

                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getApplicationContext(), "You are already registered", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(this, profile.class));
        }
    }

    public void fun1(View view) {
        registerUser();
    }

    public void new1(View view) {
        startActivity(new Intent(signup.this,LoginActivity.class));
    }

    public void radioclick(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.man:
                if (checked)
                {
                    gen = "m";
                }
                break;
            case R.id.female:
                if (checked) {
                    gen = "f";
                }
                break;
            case R.id.other:
                if (checked)
                {
                    gen = "o";
                }
                break;
        }
    }
}



