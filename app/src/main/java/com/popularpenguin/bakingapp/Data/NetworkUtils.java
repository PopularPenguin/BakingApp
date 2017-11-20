package com.popularpenguin.bakingapp.Data;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String JSON_URL =
            "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    /** Fetch the recipes from the network */
    public static List<Recipe> getRecipes(Context ctx) {
        // Check for a network connection, if there is none, return an empty array
        if (!isConnected(ctx)) {
            return new ArrayList<>();
        }

        List<Recipe> recipeList = null;

        try {
            recipeList = parseJSON(getJson(JSON_URL));
        }
        catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return recipeList;
    }

    /** Check the network connection */
    public static boolean isConnected(Context ctx) {
        // Check if there is an active network connection
        ConnectivityManager cm = (ConnectivityManager)
                ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();

        return info != null && info.isConnectedOrConnecting();
    }

    //-------- Private methods called only by the above public methods ------------------------/

    /** Request the JSON from the server and return it as a String
     * This code was taken from the OKHttp main page*/
    private static String getJson(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();

        return response.body().string();
    }

    /** Parse the JSON String into a list of Recipe objects */
    private static List<Recipe> parseJSON(String jsonString) throws JSONException {
        JSONArray results = new JSONArray(jsonString);

        List<Recipe> recipes = new ArrayList<>();

        for (int i = 0; i < results.length(); i++) {
            JSONObject recipeObject = results.getJSONObject(i);

            int id = recipeObject.getInt("id");
            String name = recipeObject.getString("name");

            JSONArray ingredientsJSON = recipeObject.getJSONArray("ingredients");
            List<Ingredients> ingredients = getIngredients(ingredientsJSON);

            JSONArray stepsJSON = recipeObject.getJSONArray("steps");
            List<Step> steps = getSteps(stepsJSON);

            Recipe recipe = new Recipe(id, name, ingredients, steps);
            recipes.add(recipe);
        }

        return recipes;
    }

    /** Extract a list of ingredients from the Recipe's JSON array */
    private static List<Ingredients> getIngredients(JSONArray json) throws JSONException {
        List<Ingredients> ingredients = new ArrayList<>();

        for (int i = 0; i < json.length(); i++) {
            JSONObject ingredientObject = json.getJSONObject(i);

            String quantity = ingredientObject.getString("quantity");
            String measure = ingredientObject.getString("measure");
            String ingredientString = ingredientObject.getString("ingredient");

            Ingredients ingredient = new Ingredients(quantity, measure, ingredientString);
            ingredients.add(ingredient);
        }

        return ingredients;
    }

    /** Extract a list of steps from the Recipe's JSON array */
    private static List<Step> getSteps(JSONArray json) throws JSONException {
        List<Step> steps = new ArrayList<>();

        for (int i = 0; i < json.length(); i++) {
            JSONObject stepObject = json.getJSONObject(i);

            int stepsId = stepObject.getInt("id");
            String shortDescription = stepObject.getString("shortDescription");
            String description = stepObject.getString("description");
            String videoURL = stepObject.getString("videoURL");
            String thumbnailURL = stepObject.getString("thumbnailURL");

            Step step = new Step(stepsId, shortDescription, description, videoURL, thumbnailURL);
            steps.add(step);
        }

        return steps;
    }
}
