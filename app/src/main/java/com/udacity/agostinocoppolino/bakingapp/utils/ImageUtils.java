package com.udacity.agostinocoppolino.bakingapp.utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.udacity.agostinocoppolino.bakingapp.model.Recipe;

public class ImageUtils {

    private static final String TAG = ImageUtils.class.getSimpleName();

    public static Uri getImage(Recipe recipe, Context context) {
        Uri imageUri;
        Log.d(TAG, "Recipe image: " + recipe.getImage());
        if (recipe.getImage() != "") { //image available
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
        Log.d(TAG, "imageUri: " + imageUri);
        return imageUri;

    }

}
