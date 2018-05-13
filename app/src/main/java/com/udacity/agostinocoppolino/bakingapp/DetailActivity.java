package com.udacity.agostinocoppolino.bakingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.udacity.agostinocoppolino.bakingapp.model.Recipe;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();

    private Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        recipe = intent != null ? (Recipe) intent.getParcelableExtra("Recipe") : null;

        if (recipe != null) {
            this.setTitle(recipe.getName());
        }

//        Log.d(TAG, "Recipe: " + recipe.toString());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

}
