package com.example.itube;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Map;

public class PlaylistActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore fStore;

    String userId;
    ListView listView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_playlist);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        auth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        user = auth.getCurrentUser();
        userId = auth.getCurrentUser().getUid();
        listView = findViewById(R.id.urlListView);

        DocumentReference docRef = fStore.collection("users").document(userId);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document != null) {
                        List<String> linksList = (List<String>) document.get("links");
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),
                                R.layout.list_item_layout, linksList);

                        listView.setAdapter(adapter);

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, android.view.View view, int position, long id) {
                                String selectedItem = linksList.get(position);


                                int equalsIndex = selectedItem.indexOf('=');
                                String videoId = selectedItem.substring(equalsIndex + 1);


                                Intent intent = new Intent(getApplicationContext(), WatchActivity.class);
                                intent.putExtra("url", videoId);
                                startActivity(intent);
                            }
                        });
                    }else{
                        return;
                    }







                } else {
                    return;
                }
            }
        });













    }
}