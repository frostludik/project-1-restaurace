package com.engeto.restaurant.util;

import java.time.LocalDateTime;

public class ValidationUtils {


    public static void validatePreparationTime(int preparationTime) throws RestaurantException {
        if (preparationTime <= 0) {
            throw new RestaurantException("Preparation time must be greater than 0. You entered: " + preparationTime);
        }
    }

    public static String validateDishImage(String dishImage, String defaultImage) {
        return (dishImage == null || dishImage.isEmpty()) ? defaultImage : dishImage;
    }


    public static void validateTime(LocalDateTime time, String fieldName) throws RestaurantException {
        LocalDateTime currentDateTime = LocalDateTime.now();

        if (time.isAfter(currentDateTime)) {
            throw new RestaurantException(fieldName + " cannot be in future. Value set: " + time);
        }
    }
}
