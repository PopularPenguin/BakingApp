package com.popularpenguin.bakingapp.controller;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.popularpenguin.bakingapp.R;
import com.popularpenguin.bakingapp.RecipeActivity;
import com.popularpenguin.bakingapp.data.Ingredients;
import com.popularpenguin.bakingapp.data.Recipe;

import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
@SuppressWarnings("WeakerAccess")
public class RecipeWidget extends AppWidgetProvider {

    private static Recipe mRecipe;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);

        if (mRecipe == null) {
            views.setTextViewText(R.id.tv_widget_ingredients, widgetText);
        }
        else {
            List<Ingredients> ingredients = mRecipe.getIngredients();

            // build the text to display in the TextView
            StringBuilder displayText = new StringBuilder();

            String servingsString = mRecipe.getServings() + " " +
                    context.getResources().getString(R.string.text_servings);
            displayText.append(String.format("%s: %s%n%n", mRecipe.getName(), servingsString));

            for (Ingredients ingredient : ingredients) {
                String quantityText = ingredient.getQuantity();
                String ingredientText = ingredient.getIngredient();
                displayText.append(String.format("%s %s%n%n", quantityText, ingredientText));
            }

            views.setTextViewText(R.id.tv_widget_ingredients, displayText);
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(intent.getAction())) {
            mRecipe = intent.getParcelableExtra(RecipeActivity.RECIPE_BROADCAST_EXTRA);

            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            ComponentName provider = new ComponentName(context, RecipeWidget.class);
            int[] appWidgetIds = manager.getAppWidgetIds(provider);

            onUpdate(context, manager, appWidgetIds);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

