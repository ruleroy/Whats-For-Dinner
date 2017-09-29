package com.vannakittikun.whatsfordinner;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Rule on 9/29/2017.
 */

public class RecipeActivity extends AppCompatActivity implements FragmentRecipe.Communicator{
    FragmentRecipe f1;
    FragmentRecipe2 f2;
    FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        manager = getFragmentManager();
        f1 = (FragmentRecipe) manager.findFragmentById(R.id.fragment);
        f1.setCommunicator(this);
    }

    @Override
    public void respond(int index) {
        f2 = (FragmentRecipe2) manager.findFragmentById(R.id.fragment2);
        if(f2!=null && f2.isVisible()){
            f2.changeData();
        } else {
            Intent intent = new Intent(this, Recipes.class);
            intent.putExtra("index", index);
            startActivity(intent);
        }
    }

}
