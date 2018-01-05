package com.udacity.sandwichclub.utils;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    public static Sandwich parseSandwichJson(String json) {

        Sandwich sandwich = null;

        try {
            JSONObject jsonSandwich = new JSONObject(json);
            JSONObject name = jsonSandwich .getJSONObject("name");
            String mainName = name.getString("mainName");
            JSONArray alsoKnownAs = name.getJSONArray("alsoKnownAs");
            String placeOfOrigin = jsonSandwich .getString("placeOfOrigin");
            String description = jsonSandwich .getString("description");
            String imageUrl = jsonSandwich .getString("image");
            JSONArray ingredients = jsonSandwich .getJSONArray("ingredients");

            //No exceptions were thrown, so I can create the sandwich object
            sandwich = new Sandwich();
            sandwich.setMainName(mainName);
            sandwich.setPlaceOfOrigin(placeOfOrigin);
            sandwich.setDescription(description);
            sandwich.setImage(imageUrl);

            if (alsoKnownAs.length() > 0) {
                List<String> alsoKnownAsList = new ArrayList<>();
                for(int i=0; i<alsoKnownAs.length(); i++) {
                    alsoKnownAsList.add(alsoKnownAs.getString(i));
                }
                sandwich.setAlsoKnownAs(alsoKnownAsList);
            }

            if (ingredients.length() > 0) {
                List<String> ingredientsList = new ArrayList<>();
                for(int i=0; i<ingredients.length(); i++) {
                    ingredientsList.add(ingredients.getString(i));
                }
                sandwich.setIngredients(ingredientsList);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return sandwich;
    }
}
