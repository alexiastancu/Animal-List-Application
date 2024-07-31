package com.example.animalcontinentapplication;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class AnimalAdapter extends RecyclerView.Adapter<AnimalAdapter.ViewHolder> implements  AnimalDatabaseHelper.OnAnimalsFetchedListener{
    private Context context;
    public static ArrayList<Animal> animalList;
    private LayoutInflater inflater;
    private View view;
    RecyclerViewInterface viewInterface;

    private AnimalDatabaseHelper dbHelper;

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    private static OnDeleteClickListener onDeleteClickListener;

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        onDeleteClickListener = listener;
    }

    public AnimalAdapter(Context context, RecyclerViewInterface viewInterface) {
        this.context = context;
        dbHelper = new AnimalDatabaseHelper(context);
        animalList = new ArrayList<>();
        fetchAnimals();
//        animalList = createDummyAnimalList(); // Initializare cu lista hardcodată
        inflater = LayoutInflater.from(context);
        this.viewInterface = viewInterface;
    }

    private void fetchAnimals() {
        dbHelper.fetchAnimalsAsync(this);
    }

    @Override
    public void onAnimalsFetched(ArrayList<Animal> animalList) {
        this.animalList = animalList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view, viewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Animal animal = animalList.get(position);
        holder.nameTextView.setText(animal.getName());
        holder.continentTextView.setText(animal.getContinent());
        Glide.with(context)
                .load(animal.getImageUrl())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return animalList.size();
    }

    public ArrayList<Animal> getAnimalList()
    {
        return this.animalList;
    }
    public void setItems(ArrayList<Animal> animalList) {
        this.animalList = animalList;
    }

    public Animal getItem(int position) {
        Iterator<Animal> iterator = animalList.iterator();
        int currentIndex = 0;

        while (iterator.hasNext()) {
            Animal animal = iterator.next();
            if (currentIndex == position) {
                return animal;
            }
            currentIndex++;
        }

        return null; // Return null if position is out of bounds
    }

    public boolean removeItem(int position) {
        Animal deletedAnimal = getItem(position);
        if(dbHelper.deleteAnimal(deletedAnimal.getId()))
        {
            animalList.remove(getItem(position));
            notifyItemRemoved(position);
            return true;
        }
        return false;
    }



    static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView continentTextView;
        public ImageView imageView;
        public Button deleteButton;

        public ViewHolder(@NonNull View itemView, RecyclerViewInterface viewInterface) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.animal_name);
            continentTextView = itemView.findViewById(R.id.continent_name);
            imageView = itemView.findViewById(R.id.imageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    viewInterface.onItemClick(position);
                }
            });

            deleteButton = itemView.findViewById(R.id.delete_button);
            deleteButton.setOnClickListener(v -> {
                if (onDeleteClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onDeleteClickListener.onDeleteClick(position);
                    }
                }
            });
        }
    }
}
