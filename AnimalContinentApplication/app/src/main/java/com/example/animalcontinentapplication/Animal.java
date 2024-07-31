package com.example.animalcontinentapplication;

import java.io.Serializable;
import java.util.Objects;

public class Animal implements Serializable {
    private int id;
    private String name;
    private String continent;
    private String url;
    private String imageUrl;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }


    public Animal(int id, String name, String continent, String url, String imageUrl) {
        this.id = id;
        this.name = name;
        this.continent = continent;
        this.url = url;
        this.imageUrl = imageUrl;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getContinent() {
        return continent;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return name + "\n" + continent;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Animal other = (Animal) obj;
        return Objects.equals(name, other.name) && Objects.equals(continent, other.continent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, continent);
    }


}
