package com.example.itube;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    EditText editTextEmail, editTextPassword, editTextUsername, editTextFullName;

    Button registerButton;
    FirebaseAuth mAuth;
    FirebaseFirestore fStore;

    TextView loginNowTextView;

    String userId;


    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        registerButton = findViewById(R.id.registerButton);
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        loginNowTextView = findViewById(R.id.loginNow);
        editTextFullName = findViewById(R.id.fullName);
        editTextUsername = findViewById(R.id.userName);

        loginNowTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

                registerButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String email, password, username, fullName;
                        email = editTextEmail.getText().toString();
                        password = editTextPassword.getText().toString();
                        username = editTextUsername.getText().toString();
                        fullName = editTextFullName.getText().toString();



                        if (TextUtils.isEmpty(email)) {
                            Toast.makeText(Register.this, "Enter email", Toast.LENGTH_SHORT).show();

                            return;
                        }

                        if (TextUtils.isEmpty(password)) {
                            Toast.makeText(Register.this, "Enter password", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (TextUtils.isEmpty(username)) {
                            Toast.makeText(Register.this, "Enter username", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (TextUtils.isEmpty(fullName)) {
                            Toast.makeText(Register.this, "Enter full name", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        mAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(Register.this, "User created.",
                                                    Toast.LENGTH_SHORT).show();

                                            userId = mAuth.getCurrentUser().getUid();
                                            DocumentReference documentReference = fStore.collection("users").document(userId);
                                            Map<String, Object> user = new HashMap<>();
                                            user.put("fName", fullName);
                                            user.put("uName", username);
                                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Log.d("Register", "onSuccess: user profile is created for " + email + "/" + userId);
                                                }
                                            });
                                            Intent intent = new Intent(getApplicationContext(), Login.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(Register.this, "Error!",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });


                    }
                });
    }
}