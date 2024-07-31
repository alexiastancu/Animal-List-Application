package com.example.animalcontinentapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AnimalDetailsActivity extends AppCompatActivity {

    private EditText animalNameEdit;
    private Spinner continentSpinner;
    private EditText imageURL;
    private EditText animalURL;
    private Button editButton;
    private Button webButton;

    private Animal animal;

    private AnimalDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_details);

        dbHelper = new AnimalDatabaseHelper(this);

        // Initialize views
        animalNameEdit = findViewById(R.id.animalNameEdit);
        continentSpinner = findViewById(R.id.continentSpinner);
        imageURL = findViewById(R.id.imageURL);
        animalURL = findViewById(R.id.animalURL);
        editButton = findViewById(R.id.button3);
        webButton = findViewById(R.id.webButton);

        // Get the animal data from MainActivity
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("animal")) {
            animal = (Animal) intent.getSerializableExtra("animal");
            if (animal != null) {
                // Set the data to the EditTexts and Spinner
                animalNameEdit.setText(animal.getName());
                // Select the continent in the Spinner
                String[] continents = getResources().getStringArray(R.array.continent_options);
                for (int i = 0; i < continents.length; i++) {
                    if (continents[i].equals(animal.getContinent())) {
                        continentSpinner.setSelection(i);
                        break;
                    }
                }
                imageURL.setText(animal.getImageUrl());
                animalURL.setText(animal.getUrl());
            }
        }

        // Set click listener for EDIT button
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the updated data from EditTexts and Spinner
                String updatedName = animalNameEdit.getText().toString();
                String updatedContinent = continentSpinner.getSelectedItem().toString();
                String updatedImageUrl = imageURL.getText().toString();
                String updatedUrl = animalURL.getText().toString();

                // Get the original animal data
                Animal originalAnimal = (Animal) getIntent().getSerializableExtra("animal");

                // Check if any field has been changed
                boolean isAnyFieldChanged = !updatedName.equals(originalAnimal.getName()) ||
                        !updatedContinent.equals(originalAnimal.getContinent()) ||
                        !updatedImageUrl.equals(originalAnimal.getImageUrl()) ||
                        !updatedUrl.equals((originalAnimal.getUrl()));

                // If no field has been changed, do nothing
                if (!isAnyFieldChanged) {
                    // Optionally show a message to the user that no changes were made
                    Toast.makeText(AnimalDetailsActivity.this, "No changes were made", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Update the animal data in the database
                originalAnimal.setName(updatedName);
                originalAnimal.setContinent(updatedContinent);
                originalAnimal.setImageUrl(updatedImageUrl);
                originalAnimal.setUrl(updatedUrl);

                // Update the animal in the database
                int result = dbHelper.updateAnimal(originalAnimal);

                // Check if the animal was successfully updated
                if (result != -1) {
                    // Optionally show a message to the user that the animal was updated successfully
                    Toast.makeText(AnimalDetailsActivity.this, "Animal updated successfully", Toast.LENGTH_SHORT).show();
                    // Pass the updated animal data back to MainActivity if needed
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("updatedAnimal", originalAnimal);
                    setResult(RESULT_OK, resultIntent);
                    finish(); // Finish the activity
                } else {
                    // Optionally show a message to the user that the update failed
                    Toast.makeText(AnimalDetailsActivity.this, "Failed to update animal", Toast.LENGTH_SHORT).show();
                }
            }
        });



        // Set click listener for GO ON THE WEB button
        webButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = animal.getUrl();
                Intent intent = new Intent(AnimalDetailsActivity.this, WebViewActivity.class);
                intent.putExtra("url", url);
                startActivity(intent);
            }
        });

//        webButton.setOnClickListener(v -> {
//            String animalName = animalNameEdit.getText().toString();
//            String wikipediaUrl = "wikipedia.org/wiki/" + animalName;
//            Intent intent1 = new Intent(AnimalDetailsActivity.this, WebViewActivity.class);
//            intent1.putExtra("url", wikipediaUrl);
//            startActivity(intent1);
//        });
    }
}
