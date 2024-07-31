package com.example.animalcontinentapplication;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;

public class AnimalDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "animal.db";
    private static final int DATABASE_VERSION = 1;

    public AnimalDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE animal (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "continent TEXT," +
                "imageUrl TEXT," +
                "url TEXT" +
                ")";
        db.execSQL(sql);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS animal";
        db.execSQL(sql);
        onCreate(db);
    }

    public ArrayList<Animal> fetchAnimals() {
        ArrayList<Animal> animalList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("animal", null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
            @SuppressLint("Range") String continent = cursor.getString(cursor.getColumnIndex("continent"));
            @SuppressLint("Range") String imageURL = cursor.getString(cursor.getColumnIndex("imageUrl"));
            @SuppressLint("Range") String url = cursor.getString(cursor.getColumnIndex("url"));

            Animal animal = new Animal(id, name, continent, url, imageURL);
            animalList.add(animal);
        }

        cursor.close();
        return animalList;
    }

    public void fetchAnimalsAsync(OnAnimalsFetchedListener listener) {
        FetchAnimalsTask task = new FetchAnimalsTask(listener);
        task.execute();
    }

    public int addAnimal(Animal animal) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", animal.getName());
        values.put("continent", animal.getContinent());
        values.put("imageUrl", animal.getImageUrl());
        values.put("url", animal.getUrl());
        Log.d("AddAnimal", "Name: " + values.get("name") + ", Continent: " + values.get("continent") + ", ImageUrl: " + values.get("imageUrl"));

        long newRowId = db.insert("animal", null, values);
          if (newRowId != -1) {
            return 1;
        } else {
            return -1;
        }
    }

    public int updateAnimal(Animal animal) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", animal.getName());
        values.put("continent", animal.getContinent());
        values.put("imageUrl", animal.getImageUrl());
        values.put("url", animal.getUrl());

        // Define the WHERE clause to specify which animal to update based on its ID
        String selection = "id=?";
        String[] selectionArgs = { String.valueOf(animal.getId()) };

        // Perform the update operation
        int rowsAffected = db.update("animal", values, selection, selectionArgs);

        return rowsAffected;
    }

    public boolean deleteAnimal(long animalId) {
        SQLiteDatabase db = getWritableDatabase();
        String selection = "id=?";
        String[] selectionArgs = {String.valueOf(animalId)};
        int deletedRows = db.delete("animal", selection, selectionArgs);
        return deletedRows > 0;
    }




//    public int addAnimal(Animal animal) {
//        SQLiteDatabase db = getWritableDatabase();
//        String sql = "INSERT INTO animal (name, continent, imageUrl) VALUES (?, ?, ?)";
//        SQLiteStatement statement = db.compileStatement(sql);
//        statement.bindString(1, animal.getName());
//        statement.bindString(2, animal.getContinent());
//        statement.bindString(3, animal.getImageUrl());
//
//        long newRowId = statement.executeInsert();
//        statement.close();
//
//        if (newRowId != -1) {
//            return 1;
//        } else {
//            return -1;
//        }
//    }



    private class FetchAnimalsTask extends AsyncTask<Void, Void, ArrayList<Animal>> {

        private final OnAnimalsFetchedListener listener;

        public FetchAnimalsTask(OnAnimalsFetchedListener listener) {
            this.listener = listener;
        }

        @Override
        protected ArrayList<Animal> doInBackground(Void... voids) {
            ArrayList<Animal> animalList = new ArrayList<>();
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.query("animal", null, null, null, null, null, null);

            while (cursor.moveToNext()) {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") String continent = cursor.getString(cursor.getColumnIndex("continent"));
                @SuppressLint("Range") String imageURL = cursor.getString(cursor.getColumnIndex("imageUrl"));
                @SuppressLint("Range") String url = cursor.getString(cursor.getColumnIndex("url"));

                Animal animal = new Animal(id, name, continent, url, imageURL);
                animalList.add(animal);
            }

            cursor.close();
            return animalList;
        }

        @Override
        protected void onPostExecute(ArrayList<Animal> animalList) {
            if (listener != null) {
                listener.onAnimalsFetched(animalList);
            }
        }
    }

    public interface OnAnimalsFetchedListener {
        void onAnimalsFetched(ArrayList<Animal> animalList);
    }

}
