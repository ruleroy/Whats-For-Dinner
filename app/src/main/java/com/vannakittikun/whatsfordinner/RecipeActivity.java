package com.vannakittikun.whatsfordinner;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by Rule on 9/29/2017.
 */

public class RecipeActivity extends AppCompatActivity {

    FragmentManager manager;
    Recipes recipeFrag;
    Recipes2 recipeFrag2;
    Button hideFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        manager = getFragmentManager();
        recipeFrag = (Recipes) manager.findFragmentById(R.id.fragment);
        recipeFrag2 = (Recipes2) manager.findFragmentById(R.id.fragment2);
        hideFrag = (Button) findViewById(R.id.buttonHide);

        getSupportActionBar().setHomeButtonEnabled(true);



        //f1 = (FragmentRecipe) manager.findFragmentById(R.id.fragment);
        //f1.setCommunicator(this);
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
                recipeFrag.removeAll(recipeFrag.parentLinearLayout);
                break;

            case R.id.item_add:
                recipeFrag.startNewDish(recipeFrag.parentLinearLayout);
                break;

            case R.id.item_edit:
                recipeFrag.editTools();
                break;
        }
        return true;
    }

    public void hideFrag(View v){
        showHideFragment(recipeFrag);
    }
    public void showHideFragment(final Recipes fragment){

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        //ft.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right);
         ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);

        if (fragment.isHidden()) {
            ft.show(fragment);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(80,LinearLayout.LayoutParams.MATCH_PARENT);
            hideFrag.setLayoutParams(params);
            hideFrag.setText(">");
            Log.d("hidden","Show");
        } else {
            ft.hide(fragment);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(160,LinearLayout.LayoutParams.MATCH_PARENT);
            hideFrag.setLayoutParams(params);
            hideFrag.setText("<");
            Log.d("Shown","Hide");
        }

        ft.commit();
    }

}
