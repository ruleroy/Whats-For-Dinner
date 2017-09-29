package com.vannakittikun.whatsfordinner;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Rule on 9/29/2017.
 */

public class Meal {
    private HashMap<String, Integer> dishes = new HashMap<String, Integer>();

    public void addDishToMeal(String dish){
        if(dishes.containsKey(dish)){
            this.dishes.put(dish, this.dishes.get(dish) + 1);
        } else {
            this.dishes.put(dish, 1);
        }
    }

    public void removeDish(String dish){
        if(dishes.containsKey(dish)) {
            if(dishes.get(dish) == 1){
                dishes.remove(dish);
            } else {
                this.dishes.put(dish, this.dishes.get(dish) - 1);
            }
        }
    }

    public HashMap<String, Integer> getDishes(){
        return this.dishes;
    }

    public int getDishAmt(String name){
        return this.dishes.get(name);
    }
}
