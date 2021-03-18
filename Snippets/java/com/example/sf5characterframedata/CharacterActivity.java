package com.example.sf5characterframedata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class CharacterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private HashMap<String, Map<String, Object>> documentMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character);

        Intent intentThatStartedThisActivity = getIntent(); // grabbing intent from previous activity
        ArrayList<String> tempArrayForSpinner = new ArrayList<>();
        Spinner moveSpinner = findViewById(R.id.move_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, tempArrayForSpinner);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        moveSpinner.setAdapter(adapter);
        moveSpinner.setOnItemSelectedListener(this);

        if (intentThatStartedThisActivity != null) {
            // Check that data has been passed from Main Activity
            if (intentThatStartedThisActivity.hasExtra("CharacterName")) {
                FirebaseFirestore db = FirebaseFirestore.getInstance(); // Create connection to database
                documentMap = new HashMap<>();                          // Creating map here to avoid writing over the map passed from a bundle

                // Retrieve every document in the collection selected from the Main Activity
                db.collection(intentThatStartedThisActivity.getStringExtra("CharacterName"))
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    // Cycle over every document in the collection
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d("DATA", task.getResult().toString());
                                        documentMap.put(document.getId(), document.getData()); // Adding all retrieved data to map for later access
                                        tempArrayForSpinner.add(document.getId());             // Filling temp array to be used with spinner adapter
                                    }
                                    adapter.notifyDataSetChanged();
                                } else {
                                    Log.d("TAG", "Error getting documents: ", task.getException());
                                }
                            }
                        });
            } else {
                TextView errorView = findViewById(R.id.error_view);
                errorView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Map<String, Object> resultMap = documentMap.get(parent.getItemAtPosition(position).toString());

        if (resultMap != null) {
            TextView startUp = findViewById(R.id.start_up_text_view);
            TextView active = findViewById(R.id.active_text_view);
            TextView recovery = findViewById(R.id.recovery_text_view);
            TextView onHit = findViewById(R.id.on_hit_text_view);
            TextView onBlock = findViewById(R.id.on_block_text_view);
            TextView damage = findViewById(R.id.damage_text_view);
            TextView stun = findViewById(R.id.stun_text_view);
            TextView type = findViewById(R.id.type_text_view);

            // setting textviews with map values
            startUp.setText(String.valueOf(resultMap.get("startup")));
            active.setText(String.valueOf(resultMap.get("active")));
            recovery.setText(String.valueOf(resultMap.get("recovery")));
            onHit.setText(String.valueOf(resultMap.get("onHit")));
            onBlock.setText(String.valueOf(resultMap.get("onBlock")));
            damage.setText(String.valueOf(resultMap.get("damage")));
            stun.setText(String.valueOf(resultMap.get("stun")));
            type.setText(String.valueOf(resultMap.get("type")));

            Pattern posNumber = Pattern.compile("^-?[1-9][0-9]*$"); // regex for color decider all numbers except 0

            // onHit color decider
            if (posNumber.matcher(onHit.getText().toString()).matches()) {
                onHit.setTextColor(Color.rgb(43, 255, 61));
                if (onHit.getText().toString().charAt(0) == '-')
                    onHit.setTextColor(Color.rgb(255, 43, 43));
            } else
                onHit.setTextColor(Color.rgb(255, 255, 255));

            // onBlock color decider
            if (posNumber.matcher(onBlock.getText().toString()).matches()) {
                onBlock.setTextColor(Color.rgb(43, 255, 61));
                if (onBlock.getText().toString().charAt(0) == '-')
                    onBlock.setTextColor(Color.rgb(255, 43, 43));
            } else
                onBlock.setTextColor(Color.rgb(255, 255, 255));


        } else {
            Log.d("MOVE_DROPDOWN", "Error getting move map from document map");
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}