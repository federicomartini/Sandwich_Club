package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final String SAVED_SCROLL_POS_Y_KEY = "SAVED_SCROLL_POS_Y_KEY";
    private static final int DEFAULT_POSITION = -1;
    private ScrollView detailScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView ingredientsIv = findViewById(R.id.image_iv);
        detailScrollView = findViewById(R.id.activity_detail_scroll_view);
        Toolbar toolbar = findViewById(R.id.toolbar);

        int scrollPosY = 0;

        if (savedInstanceState != null) {
            scrollPosY = savedInstanceState.getInt(SAVED_SCROLL_POS_Y_KEY, 0);
        }

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI(sandwich);
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(ingredientsIv);

        toolbar.setTitle(sandwich.getMainName());
        setSupportActionBar(toolbar);

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        //Set the scroll position on the Y axis
        detailScrollView.scrollTo(0, scrollPosY);
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {

        if(sandwich == null)
            return;

        TextView alsoKnownAsTv = findViewById(R.id.also_known_tv);
        TextView descriptionTv = findViewById(R.id.description_tv);
        TextView ingredientsTv = findViewById(R.id.ingredients_tv);
        TextView placeOfOriginTv = findViewById(R.id.origin_tv);
        TextView alsoKnownAsTitleTv = findViewById(R.id.also_known_title_tv);
        TextView descriptionTitleTv = findViewById(R.id.description_title_tv);
        TextView ingredientsTitleTv = findViewById(R.id.ingredients_title_tv);
        TextView placeOfOriginTitleTv = findViewById(R.id.origin_title_tv);
        TextView imageTitleTv = findViewById(R.id.image_title_text_view);

        imageTitleTv.setText(sandwich.getMainName());

        if (sandwich.getDescription().isEmpty()) {
            //If there's no info to show, hide the section
            descriptionTv.setVisibility(View.GONE);
            descriptionTitleTv.setVisibility(View.GONE);
        } else {
            descriptionTv.setText(sandwich.getDescription());
        }

        if (sandwich.getPlaceOfOrigin().isEmpty()) {
            //If there's no info to show, hide the section
            placeOfOriginTv.setVisibility(View.GONE);
            placeOfOriginTitleTv.setVisibility(View.GONE);
        } else {
            placeOfOriginTv.setText(sandwich.getPlaceOfOrigin());
        }

        if (sandwich.getAlsoKnownAs() == null) {
            //If there's no info to show, hide the section
            alsoKnownAsTv.setVisibility(View.GONE);
            alsoKnownAsTitleTv.setVisibility(View.GONE);
        } else {
            alsoKnownAsTv.setText(android.text.TextUtils.join(", ", sandwich.getAlsoKnownAs()));
        }

        if (sandwich.getIngredients() == null) {
            //If there's no info to show, hide the section
            ingredientsTv.setVisibility(View.GONE);
            ingredientsTitleTv.setVisibility(View.GONE);
        } else {
            ingredientsTv.setText(android.text.TextUtils.join(", ", sandwich.getIngredients()));
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putInt(SAVED_SCROLL_POS_Y_KEY, detailScrollView.getScrollY());
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();  return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
