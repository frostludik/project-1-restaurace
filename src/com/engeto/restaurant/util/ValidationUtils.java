package com.engeto.restaurant.util;

import java.time.LocalDateTime;

public class ValidationUtils {


    public static void validatePreparationTime(int preparationTime) throws RestaurantException {
        if (preparationTime <= 0) {
            throw new RestaurantException("Preparation time must be greater than 0. You entered: " + preparationTime);
        }
    }

    public static String validateDishImage(String dishImage, String defaultImage) throws RestaurantException {
        if ((dishImage == null || dishImage.isEmpty()) && (defaultImage == null || defaultImage.isEmpty())) {
            throw new RestaurantException("Image cannot be empty! Replaced by default image.");
        }
        return (dishImage == null || dishImage.isEmpty()) ? defaultImage : dishImage;
    }

    public static void validateTime(LocalDateTime time, String fieldName) throws RestaurantException {
        LocalDateTime currentDateTime = LocalDateTime.now();

        if (time.isAfter(currentDateTime)) {
            throw new RestaurantException(fieldName + " cannot be in future. Value set: " + time);
        }
    }

    public static void validateNumberOfFields(String[] parts, int expectedNrOfFields) throws RestaurantException {
        int numberOfFields = parts.length;
        if (numberOfFields != expectedNrOfFields) {
            throw new RestaurantException(
                    String.format("Incorrect number of fields in the line! Expected: %d, found: %d.", expectedNrOfFields, numberOfFields)
            );
        }
    }
}
