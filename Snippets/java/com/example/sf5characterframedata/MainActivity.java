package com.example.sf5characterframedata;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private TypedArray mImages;
    private String[] mNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImages = getResources().obtainTypedArray(R.array.images);     // retrieve all portraits used for character cards
        mNames = getResources().getStringArray(R.array.names_popular); // retrieve all names to label character cards

        GridView gridView = findViewById(R.id.main_grid_view);
        CustomAdapter customAdapter = new CustomAdapter(mNames, mImages, this);
        gridView.setAdapter(customAdapter);
        gridView.setOnItemClickListener(this);

        mNames = getResources().getStringArray(R.array.names_popular); // resetting mNames to remove uppercase modification from customAdapter
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Context context = this;
        Class destinationClass = CharacterActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra("CharacterName", (mNames[position])); // passing character name from button to next activity
        startActivity(intentToStartDetailActivity);
    }
}