package com.example.itube;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    EditText urlInput;
    Button logoutButton, playButton, addToPlaylistButton, goToPlaylistButton;
    TextView textView;
    FirebaseUser user;
    FirebaseFirestore fStore;

    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        urlInput = findViewById(R.id.urlInput);
        logoutButton = findViewById(R.id.logout);
        playButton = findViewById(R.id.playButton);
        addToPlaylistButton = findViewById(R.id.addToPlaylistButton);
        goToPlaylistButton = findViewById(R.id.goToPlaylistButton);
        textView = findViewById(R.id.userDetails);
        fStore = FirebaseFirestore.getInstance();

        user = auth.getCurrentUser();
        if(user == null){
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }else {
            textView.setText(user.getEmail());
        }

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        addToPlaylistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = urlInput.getText().toString();

                if (TextUtils.isEmpty(url)) {
                    Toast.makeText(MainActivity.this, "Enter url", Toast.LENGTH_SHORT).show();
                    return;
                }

                userId = auth.getCurrentUser().getUid();
                DocumentReference documentReference = fStore.collection("users").document(userId);
                documentReference.update("links", FieldValue.arrayUnion(url));


            }
        });

        goToPlaylistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PlaylistActivity.class);
                startActivity(intent);
                finish();
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(getApplicationContext(), WatchActivity.class);
                intent.putExtra("url", urlInput.getText().toString());
                startActivity(intent);
            }
        });





    }
}