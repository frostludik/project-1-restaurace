package com.engeto.restaurant.model;
import com.engeto.restaurant.util.RestaurantException;

import java.util.HashMap;
import java.util.Map;
import java.util.Collection;
import java.util.Optional;

public class CookBook {

    private static Map<String, Dish> dishMap = new HashMap<>();

    public static void addDish(Dish dish) {
        dishMap.put(dish.getTitle(), dish);
    }

    public static void addDishToCookBook(Dish dish) {
        dishMap.put(dish.getTitle(), dish);
    }

    public void removeDishById(long id) throws RestaurantException {
        if (!dishMap.containsKey(id)) {
            throw new RestaurantException("Dish with ID " + id + " not found.");
        }
        dishMap.remove(id);
    }
    public static Optional<Dish> getDishById(long id) {
        return Optional.ofNullable(dishMap.get(id));
    }


    public Collection<Dish> getAllDishes() {
        return dishMap.values();
    }
}
