package com.udacity.agostinocoppolino.bakingapp.utils;

import android.content.Context;
import android.net.Uri;

import com.udacity.agostinocoppolino.bakingapp.model.Recipe;

import timber.log.Timber;

public class ImageUtils {

    public static Uri getImage(Recipe recipe, Context context) {
        Uri imageUri;
        Timber.d("Recipe image: ".concat(recipe.getImage()));
        if (!recipe.getImage().equals("")) { //image available
            imageUri = Uri.parse(recipe.getImage()).buildUpon()
                    .build();
        } else { //image not available, get from drawable resources
            String imageName = null;
            switch (recipe.getId()) {
                case "1":
                    imageName = "nutella_pie";
                    break;
                case "2":
                    imageName = "brownies";
                    break;
                case "3":
                    imageName = "yellow_cake";
                    break;
                case "4":
                    imageName = "cheesecake";
                    break;
            }
            int recipeImageId = context.getResources().getIdentifier(
                    imageName, "drawable", context.getPackageName());
            imageUri = Uri.parse(String.valueOf(recipeImageId)).buildUpon()
                    .build();
        }
        Timber.d("imageUri: ".concat(String.valueOf(imageUri)));
        return imageUri;

    }

}
