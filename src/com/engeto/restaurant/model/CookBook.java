package com.engeto.restaurant.model;
import com.engeto.restaurant.util.RestaurantException;

import java.util.HashMap;
import java.util.Map;
import java.util.Collection;
import java.util.Optional;

public class CookBook {

    private Map<Long, Dish> dishes = new HashMap<>();

    public void addDish(Dish dish) {
        dishes.put(dish.getId(), dish);
    }

    public void removeDishById(long id) throws RestaurantException {
        if (!dishes.containsKey(id)) {
            throw new RestaurantException("Dish with ID " + id + " not found.");
        }
        dishes.remove(id);
    }
    public Optional<Dish> getDishById(long id) {
        return Optional.ofNullable(dishes.get(id));
    }


    public Collection<Dish> getAllDishes() {
        return dishes.values();
    }
}
