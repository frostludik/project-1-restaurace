package com.engeto.restaurant.model;
import com.engeto.restaurant.util.RestaurantException;

import java.util.LinkedHashMap;
import java.util.Map;

public class CookBook {

    private static Map<Long, Dish> dishMap = new LinkedHashMap<>();

    public static void addDishToCookBook(Dish dish) {
        dishMap.put(dish.getId(), dish);
    }

    public static void removeDishById(long dishId) throws RestaurantException {
        if (dishMap.containsKey(dishId)) {
            Dish dishToRemove = dishMap.remove(dishId);
            System.out.println("Dish nr. " + dishId + " - " + dishToRemove.getTitle() + " was successfully removed!");
        } else {
            throw new RestaurantException("Dish with ID " + dishId + " not found, cannot remove.");
        }
    }

    public static Dish getDishFromCookBookById(long dishId) throws RestaurantException {
        Dish dish = dishMap.get(dishId);
        if (dish != null) {
            return dish;
        } else {
            throw new RestaurantException("Dish with ID " + dishId + " not found!");
        }
    }

    public static Map<Long, Dish> getAllDishes() {
        return new LinkedHashMap<>(dishMap); // Return a copy to prevent modification
    }
}

