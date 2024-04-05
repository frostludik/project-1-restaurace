package com.engeto.restaurant.util;

public class ValidationUtils {


    public static void validatePreparationTime(int preparationTime) throws RestaurantException {
        if (preparationTime <= 0) {
            throw new RestaurantException("Preparation time must be greater than 0. You entered: " + preparationTime);
        }
    }
}
