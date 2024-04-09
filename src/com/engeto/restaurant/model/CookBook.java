package com.engeto.restaurant.model;
import com.engeto.restaurant.util.RestaurantException;

import java.util.*;

public class CookBook {

    private static Map<String, Dish> dishMap = new LinkedHashMap<>();

    public static void addDishToCookBook(Dish dish) {
        dishMap.put(dish.getTitle(), dish);
    }

    public void removeDishById(long dishId) throws RestaurantException {
        try {
            Dish dishToRemove = getDishById(dishId);
            dishMap.remove(dishToRemove.getTitle());
            System.out.println("Pokrm č. " + dishId + " - " + dishToRemove.getTitle() + " byl úspěšně odstraněn.");
        } catch (RestaurantException e) {
            throw new RestaurantException("Chyba při pokusu o odstranění pokrmu: " + e.getMessage());
        }
    }
    public static Dish getDishById(long dishId) throws RestaurantException {
        for (Dish dish : dishMap.values()) {
            if (dish.getId() == dishId) {
                return dish;
            }
        }
        throw new RestaurantException("Dish with ID " + dishId + " not found!");
    }
    public Collection<Dish> getAllDishes() {
        return dishMap.values();
    }
}
