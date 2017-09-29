package com.vannakittikun.whatsfordinner;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;


/**
 * Created by Rule on 9/28/2017.
 */

public class Recipes extends AppCompatActivity  {
    private LinearLayout parentLinearLayout;
    private LinearLayout parent;
    private ScrollView scrollv;
    private MyDBHandler dbHandler;
    private TextView test;
    private EditText recipeName;
    private Button getRecipe;
    private ImageView getImg;
    private Button recipeBtn;
    private Button goToNewDish;
    private int editId;
    private boolean edit;
    private Meal mealPlanOptions;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        Intent intent = getIntent();
        int index = intent.getIntExtra("index", 0);

        test = (TextView) findViewById(R.id.test);
        recipeName = (EditText) findViewById(R.id.recipeInput);
        getRecipe = (Button) findViewById(R.id.getRecipe);
        getImg = (ImageView) findViewById(R.id.getImg);
        dbHandler = new MyDBHandler(this, null, null, 1);
        parentLinearLayout = (LinearLayout) findViewById(R.id.recipeLinearLayout);
        mealPlanOptions = new Meal();

        populateList();
        printDatabase();

        if(getIntent().getBooleanExtra("EDITING", false)){
            editTools();
        }

        getSupportActionBar().setHomeButtonEnabled(true);

        goToNewDish = (Button) findViewById(R.id.goToNewDish);
        if(dbHandler.getCountRecipes() == 0){
            goToNewDish.setVisibility(View.VISIBLE);
        } else {
            goToNewDish.setVisibility(View.GONE);
        }


    }

    public void onBackPressed() {
        Recipes.this.finish();
        startActivity(new Intent(Recipes.this, MainActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu); //inflate our menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                break;

            case R.id.item_delete:
                removeAll(parentLinearLayout);
                break;

            case R.id.item_add:
                startNewDish(parentLinearLayout);
                break;

            case R.id.item_edit:
                editTools();
                break;
        }
        return true;
    }

    private void editTools() {
        if(!edit) {
            LinearLayout editTools = (LinearLayout) findViewById(R.id.editTools);
            LinearLayout editTools2 = (LinearLayout) findViewById(R.id.recipeLinearLayout);

            for (int i = 0; i < editTools2.getChildCount(); i++) {
                editTools2.findViewWithTag(i).setVisibility(View.VISIBLE);
            }

            //editTools.setVisibility(View.VISIBLE);

            Context context = getApplicationContext();
            CharSequence text = Integer.toString(editTools2.getChildCount());
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            //toast.show();
            edit = true;
        } else {
            edit = false;
            LinearLayout editTools2 = (LinearLayout) findViewById(R.id.recipeLinearLayout);

            for (int i = 0; i < editTools2.getChildCount(); i++) {
                editTools2.findViewWithTag(i).setVisibility(View.GONE);
            }
        }
    }

    public void printDatabase(){
        String dbString = dbHandler.databaseToString();
        test.setText(dbString);
    }

    public void removeAll(View v){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        dbHandler.removeAll();
                        Recipes.this.finish();
                        startActivity(new Intent(Recipes.this, Recipes.class));

                        Context context = getApplicationContext();
                        CharSequence text = "Deleted all recipes.";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(Recipes.this);
        builder.setTitle("Delete all recipes");
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();



    }

    public void startNewDish(View v){
        Recipes.this.finish();
        startActivityForResult(new Intent(Recipes.this, NewDish.class), 0);
    }

    public void getRecipe(View v) throws JSONException {
        if(!recipeName.getText().toString().equals("")) {
            if(dbHandler.nameExists(recipeName.getText().toString())){
                Dish getDish = dbHandler.dbToObject(recipeName.getText().toString());
                getImg.setImageBitmap(getDish.getImage());

                AlertDialog alertDialog = new AlertDialog.Builder(Recipes.this).create();
                alertDialog.setTitle("Dish retrieved");
                alertDialog.setMessage(getDish.getName() + "\n" + getDish.getDirections() + "\n" + getDish.getIngredients() + "\n" + getDish.getImage() + "\n");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            } else {
                Context context = getApplicationContext();
                CharSequence text = "Recipe does not exist.";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }

        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(Recipes.this).create();
            alertDialog.setTitle("Error");
            alertDialog.setMessage("Please input recipe name!");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
    }

    public void populateList(){
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = null;

        // Add the new row before the add field button.
        for(int i = 0; i< dbHandler.getCountRecipes(); i++) {
            rowView = inflater.inflate(R.layout.recipelist, null);
            Button btn = (Button) rowView.findViewById(R.id.btnRecipeName);
            Button btn2 = (Button) rowView.findViewById(R.id.addToMealPlan);
            LinearLayout layout = (LinearLayout) rowView.findViewById(R.id.editTools);
            layout.setTag(i);
            btn.setText(dbHandler.getListNames().get(i));

            btn.setOnLongClickListener(new View.OnLongClickListener(){

                @Override
                public boolean onLongClick(View view) {
                    onEditRecipeLongClick(view);
                    return true;
                }
            });
            parentLinearLayout.addView(rowView);
        }
    }

    public void onAddField(View v) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.field, null);
        // Add the new row before the add field button.

        parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount());

    }

    public void onDelete(View v) {
        parentLinearLayout.removeView((View) v.getParent());
    }

    public void onAddToMealPlan(View v) throws JSONException {
        LinearLayout parent = (LinearLayout) v.getParent();
        Button recipeBtn = (Button) parent.findViewById(R.id.btnRecipeName);
        String recName = recipeBtn.getText().toString();

        mealPlanOptions.addDishToMeal(recName);

        Context context = getApplicationContext();
        CharSequence text = "Added " + recipeBtn.getText().toString() + " to meal plan options. " + "(" + mealPlanOptions.getDishAmt(recName) + ")";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public void onDeleteRecipe(View v){
        LinearLayout parent = (LinearLayout) v.getParent().getParent();
        recipeBtn = (Button) parent.findViewById(R.id.btnRecipeName);



        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        dbHandler.deleteDish(recipeBtn.getText().toString());
                        Recipes.this.finish();
                        //startActivity(new Intent(Recipes.this, Recipes.class));
                        Intent intent = new Intent(getBaseContext(), Recipes.class);
                        intent.putExtra("EDITING", true);
                        startActivityForResult(intent, 0);

                        Context context = getApplicationContext();
                        CharSequence text = "Deleted " + recipeBtn.getText().toString();
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();

                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(Recipes.this);
        builder.setTitle("Delete " + recipeBtn.getText().toString());
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();


    }

    public void onEditRecipe(View v){
        //View parent = (View) v.getParent();
        LinearLayout parent = (LinearLayout) v.getParent().getParent();
        Button recipeBtn = (Button) parent.findViewById(R.id.btnRecipeName);

        editId = dbHandler.getId(recipeBtn.getText().toString());
        Intent intent = new Intent(getBaseContext(), NewDish.class);
        intent.putExtra("EDIT_RECIPE_ID", editId);
        intent.putExtra("EDITING_MODE", true);
        startActivityForResult(intent, 0);
    }

    public void onEditRecipeLongClick(View v){
        //View parent = (View) v.getParent();
        LinearLayout parent = (LinearLayout) v.getParent();
        Button recipeBtn = (Button) parent.findViewById(R.id.btnRecipeName);

        editId = dbHandler.getId(recipeBtn.getText().toString());
        Intent intent = new Intent(getBaseContext(), NewDish.class);
        intent.putExtra("EDIT_RECIPE_ID", editId);
        intent.putExtra("EDITING_MODE", true);
        startActivityForResult(intent, 0);
    }


}
