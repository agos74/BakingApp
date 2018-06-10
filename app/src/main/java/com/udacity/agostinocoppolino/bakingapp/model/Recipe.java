package com.udacity.agostinocoppolino.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

@SuppressWarnings("unchecked")
public class Recipe implements Parcelable {

    private String id;
    private String name;
    private List<Ingredient> ingredients;
    private List<Step> steps;
    private int servings;
    private String image;

    private Recipe(Parcel in) {
        id = in.readString();
        name = in.readString();
        servings = in.readInt();
        image = in.readString();

        ingredients = in.readArrayList(Ingredient.class.getClassLoader());
        steps = in.readArrayList(Step.class.getClassLoader());
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public String getId() {
        return id;
    }

    @SuppressWarnings("unused")
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @SuppressWarnings("unused")
    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    @SuppressWarnings("unused")
    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    @SuppressWarnings("unused")
    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public int getServings() {
        return servings;
    }

    @SuppressWarnings("unused")
    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    @SuppressWarnings("unused")
    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", ingredients=" + ingredients.toString() +
                ", steps=" + steps.toString() +
                ", servings=" + servings +
                ", image='" + image + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeInt(servings);
        dest.writeString(image);

        dest.writeList(ingredients);
        dest.writeList(steps);
    }
}
