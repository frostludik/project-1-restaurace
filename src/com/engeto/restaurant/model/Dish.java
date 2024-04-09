package com.engeto.restaurant.model;

import com.engeto.restaurant.util.RestaurantException;
import com.engeto.restaurant.util.ValidationUtils;

import java.math.BigDecimal;

public class Dish {
    private static long lastAssignedId = 1;
    private long id;
    private String title;
    private BigDecimal price;
    private int preparationTime;
    private String dishImage;
    private static final String defaultImage = "blank";



    public Dish(String title, BigDecimal price, int preparationTime, String dishImage) throws RestaurantException {
        this.id = lastAssignedId++;
        this.title = title;
        this.price = price;
        ValidationUtils.validatePreparationTime(preparationTime);
        this.preparationTime = preparationTime;
        this.setDishImage(dishImage);
        CookBook.addDishToCookBook(this);
    }


    public Dish(String title, BigDecimal price, int preparationTime) throws RestaurantException {
        this(title, price, preparationTime, defaultImage);
    }

    private Dish(long id, String title, BigDecimal price, int preparationTimeInMinutes, String dishImage) throws RestaurantException {
        this.id = id;
        this.title = title;
        setPrice(price);
        setPreparationTime(preparationTimeInMinutes);
        this.dishImage = dishImage;
    }

    public static Dish createDishWithId(long id, String title, BigDecimal price, int preparationTimeInMinutes, String image) throws RestaurantException {
        return new Dish(id, title, price, preparationTimeInMinutes, image);
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getPreparationTime() {
        return preparationTime;
    }

    public void setPreparationTime(int preparationTime) throws RestaurantException {
        ValidationUtils.validatePreparationTime(preparationTime);
        this.preparationTime = preparationTime;
    }

    public String getDishImage() {
        return dishImage;
    }

    public void setDishImage(String dishImage) {
        this.dishImage = ValidationUtils.validateDishImage(dishImage, Dish.defaultImage);
    }
    public static Dish parseDish(String data) throws RestaurantException {
        String [] items;
        try {
            items = data.split("\t");
            String title = items[0];
            BigDecimal price = new BigDecimal(items[1]);
            int preparationTime = Integer.parseInt(items[2]);
            String imageMain = items[3];
            return new Dish(title, price, preparationTime, defaultImage );
        } catch (IllegalArgumentException e) {
            throw new RestaurantException("Wrong data. Unable to parse dish from file!");
        }
    }
    public String exportToString() {
        return title + "\t" + price + "\t" + preparationTime + "\t" + defaultImage;
    }

    @Override
    public String toString() {
        return title;
    }


}

