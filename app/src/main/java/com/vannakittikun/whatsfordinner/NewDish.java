package com.vannakittikun.whatsfordinner;

import android.app.ActionBar;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rule on 9/26/2017.
 */

public class NewDish extends AppCompatActivity {
    private ScrollView scroll;
    private ImageView newDishImg;
    private LinearLayout parentLinearLayout;
    private int ingredientAmt;
    private String strIngredientAmtFormat = null;
    private String strIngredientMsg = null;
    private EditText recipeName;
    private EditText recipeDescription;
    private boolean editingMode;
    private int editingId;
    private LinearLayout emptyLayout;
    private Button deleteDish;
    private boolean canSave = false;
    MyDBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newdish);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        getSupportActionBar().setHomeButtonEnabled(true);

        parentLinearLayout = (LinearLayout) findViewById(R.id.parent_linear_layout);
        emptyLayout = (LinearLayout) findViewById(R.id.emptyLayout);

        recipeName = (EditText) findViewById(R.id.recipeName);
        recipeDescription = (EditText) findViewById(R.id.directions);
        deleteDish = (Button) findViewById(R.id.deleteDish);

        newDishImg = (ImageView) findViewById(R.id.newDishImage);
        ImageButton newImg = (ImageButton) findViewById(R.id.newImage);

        scroll = (ScrollView) findViewById(R.id.scroll);
        editingId = getIntent().getIntExtra("EDIT_RECIPE_ID", 0);
        editingMode = getIntent().getBooleanExtra("EDITING_MODE", false);
        dbHandler = new MyDBHandler(this, null, null, 1);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        if(editingMode){
            Dish editingObj = new Dish();
            try {
                editingObj = dbHandler.dbToObject(editingId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            recipeName.setText(editingObj.getName());
            recipeDescription.setText(editingObj.getDirections());

            AutoCompleteTextView text1 = (AutoCompleteTextView) parentLinearLayout.getChildAt(0).findViewById(R.id.ingredient1);
            if(editingObj.getIngredients().size() > 0){
                text1.setText(editingObj.getIngredients().get(0));
            }


            for (int i = 1; i < editingObj.getIngredients().size(); i++) {
                onAddField(editingObj.getIngredients().get(i));
            }

            newDishImg.setImageBitmap(editingObj.getImage());
            deleteDish.setVisibility(View.VISIBLE);
        }

        recipeName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!editingMode) {
                    if (!b && dbHandler.nameExists(recipeName.getText().toString())) {
                        canSave = false;
                        Context context = getApplicationContext();
                        CharSequence text = "Recipe name already exists!";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                        recipeName.setTextColor(ResourcesCompat.getColor(getResources(), R.color.red, null));

                    } else {
                        canSave = true;
                        recipeName.setTextColor(ResourcesCompat.getColor(getResources(), R.color.black, null));
                    }
                } else {
                    if (!b && dbHandler.getId(recipeName.getText().toString()) != editingId && dbHandler.nameExists(recipeName.getText().toString())) {
                        canSave = false;
                        Context context = getApplicationContext();
                        CharSequence text = "Recipe name already exists!";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                        recipeName.setTextColor(ResourcesCompat.getColor(getResources(), R.color.red, null));
                    } else {
                        canSave = true;
                        recipeName.setTextColor(ResourcesCompat.getColor(getResources(), R.color.black, null));
                    }
                }
            }
        });

        recipeName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!editingMode) {
                    if (dbHandler.nameExists(recipeName.getText().toString())) {
                        canSave = false;
                        Context context = getApplicationContext();
                        CharSequence text = "Recipe name already exists!";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                        recipeName.setTextColor(ResourcesCompat.getColor(getResources(), R.color.red, null));

                    } else {
                        canSave = true;
                        recipeName.setTextColor(ResourcesCompat.getColor(getResources(), R.color.black, null));
                    }
                } else {
                    if (dbHandler.getId(recipeName.getText().toString()) != editingId && dbHandler.nameExists(recipeName.getText().toString())) {
                        canSave = false;
                        Context context = getApplicationContext();
                        CharSequence text = "Recipe name already exists!";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                        recipeName.setTextColor(ResourcesCompat.getColor(getResources(), R.color.red, null));
                    } else {
                        canSave = true;
                        recipeName.setTextColor(ResourcesCompat.getColor(getResources(), R.color.black, null));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        newImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPickImage(v);
            }
        });


        Button addI = (Button) findViewById(R.id.addIngredient);
        addI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddField(v);
                View lastChild = scroll.getChildAt(scroll.getChildCount() - 1);
                int bottom = lastChild.getBottom() + scroll.getPaddingBottom();
                int sy = scroll.getScrollY();
                int sh = scroll.getHeight();
                int delta = bottom - (sy + sh);

                scroll.smoothScrollBy(0, delta);
            }
        });

    }

    @Override
    public void onBackPressed() {
        if(!editingMode){
            NewDish.this.finish();
            startActivity(new Intent(NewDish.this, MainActivity.class));
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(!editingMode){
                    NewDish.this.finish();
                    startActivity(new Intent(NewDish.this, MainActivity.class));
                } else {
                    super.onBackPressed();
                }
                break;
        }
        return true;
    }

    public void deleteDish(View v) throws IOException {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        dbHandler.deleteDish(dbHandler.getName(editingId));
                        startActivity(new Intent(NewDish.this, RecipeActivity.class));
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(NewDish.this);
        builder.setTitle("Delete " + recipeName.getText().toString());
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    public void saveDish(View v) throws IOException {
        if (!editingMode) {
            if (dbHandler.nameExists(recipeName.getText().toString())) {
                canSave = false;
                recipeName.setTextColor(ResourcesCompat.getColor(getResources(), R.color.red, null));
            } else {
                canSave = true;
                recipeName.setTextColor(ResourcesCompat.getColor(getResources(), R.color.black, null));
            }
        } else {
            if (dbHandler.getId(recipeName.getText().toString()) != editingId && dbHandler.nameExists(recipeName.getText().toString())) {
                canSave = false;
                recipeName.setTextColor(ResourcesCompat.getColor(getResources(), R.color.red, null));
            } else {
                canSave = true;
                recipeName.setTextColor(ResourcesCompat.getColor(getResources(), R.color.black, null));
            }
        }


        if(!recipeName.getText().toString().isEmpty()) {
            EditText recipeDirections = (EditText) findViewById(R.id.directions);
            AutoCompleteTextView recipeIngredient1 = (AutoCompleteTextView) findViewById(R.id.ingredient1);
            Dish newDish = new Dish();

            newDishImg.setDrawingCacheEnabled(true);
            newDish.setImage(newDishImg.getDrawingCache());
            newDish.setName(recipeName.getText().toString());
            newDish.setDirections(recipeDirections.getText().toString());

            if(!recipeIngredient1.getText().toString().equals("")) {
                newDish.addIngredient(recipeIngredient1.getText().toString());
            }

            for (int i = 1; i < parentLinearLayout.getChildCount() - 1; i++) {
                AutoCompleteTextView text = (AutoCompleteTextView) parentLinearLayout.getChildAt(i).findViewById(R.id.ingredient);
                if(!text.getText().toString().equals("")) {
                    newDish.addIngredient(text.getText().toString());
                }
            }

            //check for duplicate name
            if (!canSave) {
                Context context = getApplicationContext();
                CharSequence text = "Recipe name already exists!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            } else {
                if(editingMode){
                    dbHandler.addDish(newDish, editingId);
                    AlertDialog alertDialog = new AlertDialog.Builder(NewDish.this).create();
                    alertDialog.setTitle(recipeName.getText().toString());
                    alertDialog.setMessage("Dish saved!");

                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    NewDish.this.finish();
                                    startActivityForResult(new Intent(NewDish.this, RecipeActivity.class), 0);
                                }
                            });
                    alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            NewDish.this.finish();
                            startActivityForResult(new Intent(NewDish.this, RecipeActivity.class), 0);
                        }
                    });
                    alertDialog.show();
                } else {
                    dbHandler.addDish(newDish);
                    AlertDialog alertDialog = new AlertDialog.Builder(NewDish.this).create();
                    alertDialog.setTitle(recipeName.getText().toString());
                    alertDialog.setMessage("Dish saved!");

                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    NewDish.this.finish();
                                    startActivityForResult(new Intent(NewDish.this, NewDish.class), 0);
                                }
                            });
                    alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            NewDish.this.finish();
                            startActivityForResult(new Intent(NewDish.this, NewDish.class), 0);
                        }
                    });
                    alertDialog.show();
                }

            }
        } else {
            Context context = getApplicationContext();
            CharSequence text = "Please enter a recipe name!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }

    public void showDirections(View v) {
        LinearLayout directionsView = (LinearLayout) findViewById(R.id.directionsLayout);
        ImageView arrow = (ImageView) findViewById(R.id.arrow);
        if (directionsView.getVisibility() == View.GONE) {
            directionsView.setVisibility(View.VISIBLE);
            arrow.setImageResource(R.drawable.uparrow);
        } else {
            directionsView.setVisibility(View.GONE);
            arrow.setImageResource(R.drawable.downarrow);
        }

    }

    public void showIngredients(View v) {
        LinearLayout directionsView = (LinearLayout) findViewById(R.id.parent_linear_layout);
        ImageView arrow = (ImageView) findViewById(R.id.arrow2);
        if (directionsView.getVisibility() == View.GONE) {
            directionsView.setVisibility(View.VISIBLE);
            arrow.setImageResource(R.drawable.uparrow);
        } else {
            directionsView.setVisibility(View.GONE);
            arrow.setImageResource(R.drawable.downarrow);
        }

    }

    public void onAddField(View v) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.field, null);
        // Add the new row before the add field button.
        ingredientAmt = parentLinearLayout.getChildCount();
        strIngredientAmtFormat = getString(R.string.ingredientAmt);
        strIngredientMsg = String.format(strIngredientAmtFormat, ingredientAmt);


        parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount() - 1);
        AutoCompleteTextView text = (AutoCompleteTextView) rowView.findViewById(R.id.ingredient);

        text.setHint(strIngredientMsg);
        if (ingredientAmt >= 10) {
            Button addI = (Button) findViewById(R.id.addIngredient);
            //addI.setEnabled(false);
            addI.setVisibility(View.GONE);
        }
        text.requestFocus();
    }

    public void onAddField(String ing) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.field, null);
        // Add the new row before the add field button.
        ingredientAmt = parentLinearLayout.getChildCount();
        strIngredientAmtFormat = getString(R.string.ingredientAmt);
        strIngredientMsg = String.format(strIngredientAmtFormat, ingredientAmt);


        parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount() - 1);
        AutoCompleteTextView text = (AutoCompleteTextView) rowView.findViewById(R.id.ingredient);

        text.setHint(strIngredientMsg);
        text.setText(ing);
        if (ingredientAmt >= 10) {
            Button addI = (Button) findViewById(R.id.addIngredient);
            //addI.setEnabled(false);
            addI.setVisibility(View.GONE);
        }
        parentLinearLayout.requestFocus();
    }

    public void onDelete(View v) {
        parentLinearLayout.removeView((View) v.getParent());

        for (int i = 1; i < parentLinearLayout.getChildCount() - 1; i++) {
            AutoCompleteTextView text = (AutoCompleteTextView) parentLinearLayout.getChildAt(i).findViewById(R.id.ingredient);
            text.setHint("Ingredient " + (i + 1));
        }

        ingredientAmt = parentLinearLayout.getChildCount();
        strIngredientAmtFormat = getString(R.string.ingredientAmt);
        strIngredientMsg = String.format(strIngredientAmtFormat, ingredientAmt);

        if (ingredientAmt <= 10) {
            Button addI = (Button) findViewById(R.id.addIngredient);
            //addI.setEnabled(true);
            addI.setVisibility(View.VISIBLE);
        }

        emptyLayout.requestFocus();
    }


    private static final int PICK_IMAGE_ID = 234; // the number doesn't matter

    public void onPickImage(View view) {
        ImagePicker.pickImage(this);
    }

    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case PICK_IMAGE_ID:
                    Bitmap bitmap = ImagePicker.getImageFromResult(this, resultCode, data);
                    newDishImg.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 1000, 1000, false));
                    newDishImg.setAdjustViewBounds(true);
                    break;
                default:
                    super.onActivityResult(requestCode, resultCode, data);
                    break;
            }
        }
    }
    */


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_CANCELED) {
            if (resultCode == RESULT_OK && requestCode == ImagePicker.REQUEST_PICK) {
                ImagePicker.beginCrop(this, resultCode, data);
            } else if (requestCode == ImagePicker.REQUEST_CROP) {
                Bitmap bitmap = ImagePicker.getImageCropped(this, resultCode, data, ImagePicker.ResizeType.FIXED_SIZE, 500);
                newDishImg.setImageBitmap(bitmap);
                newDishImg.setAdjustViewBounds(true);
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

}
