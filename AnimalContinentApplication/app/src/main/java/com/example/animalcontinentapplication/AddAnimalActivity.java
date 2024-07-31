package com.example.animalcontinentapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AddAnimalActivity extends AppCompatActivity {

    private EditText animalNameEditText;
    private Spinner continentSpinner;
    private EditText imageUrlEditText;
    private EditText animalURLeditText;
    private Button addButton;

    private AnimalDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_animal);

        // Initialize views
        animalNameEditText = findViewById(R.id.editTextText);
        continentSpinner = findViewById(R.id.continentSpinner);
        imageUrlEditText = findViewById(R.id.editTextText2);
        animalURLeditText = findViewById(R.id.animalURLeditText);
        addButton = findViewById(R.id.button);

        // Set spinner options
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.continent_options,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        continentSpinner.setAdapter(adapter);

        // Initialize database helper
        dbHelper = new AnimalDatabaseHelper(this);

        // Set click listener for add button
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user inputs
                String animalName = animalNameEditText.getText().toString();
                String continent = continentSpinner.getSelectedItem().toString();
                String imageUrl = imageUrlEditText.getText().toString();
                String url = animalURLeditText.getText().toString();

                // Validate inputs
                if (animalName.isEmpty() || imageUrl.isEmpty() || url.isEmpty()) {
                    Toast.makeText(AddAnimalActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create a new Animal object
                Animal newAnimal = new Animal(-1, animalName, continent, url, imageUrl);

                // Add the new animal to the database
                int result = dbHelper.addAnimal(newAnimal);

                // Check if the animal was successfully added
                if (result != -1) {
                    Toast.makeText(AddAnimalActivity.this, "Animal added successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Finish the activity
                } else {
                    Toast.makeText(AddAnimalActivity.this, "Failed to add animal", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
