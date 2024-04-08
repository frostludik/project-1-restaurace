package com.engeto.restaurant.model;

import com.engeto.restaurant.util.RestaurantException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DishList {

    List<Dish> dishes = new ArrayList<>();

    public DishList() {
        this.dishes = dishes;
    }

    public List<Dish> getDishes() {
        return new ArrayList<>(dishes);
    }

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
    }

    public void removeDish(Dish dish) {
        dishes.remove(dish);
    }

}

