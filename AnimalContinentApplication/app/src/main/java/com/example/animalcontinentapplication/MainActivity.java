package com.example.animalcontinentapplication;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RecyclerViewInterface{
    private RecyclerView recyclerView;
    private AnimalAdapter adapter;
    private Button addButton;
//    public static ArrayList<Animal> animalList;

    private AnimalDatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new AnimalDatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Initialize adapter
        adapter = new AnimalAdapter(this, this);
        adapter.setOnDeleteClickListener(position -> {

            boolean isDeleted = adapter.removeItem(position);
            if (isDeleted) {
                Toast.makeText(MainActivity.this, "The selected item was deleted successfully", Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(MainActivity.this, "An error was encountered while deleting the animal.", Toast.LENGTH_SHORT)
                        .show();
            }
        });


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Executați acțiunile dorite când butonul este apăsat
                // De exemplu, puteți deschide o nouă activitate
                Intent intent = new Intent(MainActivity.this, AddAnimalActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Actualizează lista de animale aici
        refreshAnimalList();
    }

    private void refreshAnimalList() {
        // Reîncarcă lista de animale din baza de date și actualizează RecyclerView-ul
        ArrayList<Animal> updatedAnimalList = dbHelper.fetchAnimals();
        adapter.setItems(updatedAnimalList);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onItemClick(int position) {
        // get the data to push to detail Activity
        Animal animal = adapter.getItem(position);

        // make an intent and a bundle
        Intent intent = new Intent(MainActivity.this, AnimalDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("animal", animal);
        intent.putExtras(bundle);

        // start activity 2
        startActivity(intent);
    }
}