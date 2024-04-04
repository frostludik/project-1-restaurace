package com.engeto.restaurant.model;

import java.math.BigDecimal;

public class Dish {
    private long id;
    private String title;
    private BigDecimal price;
    private int preparationTime;
    private String dishImage;


    public Dish(String title, BigDecimal price, int preparationTime, String dishImage) {
        this.title = title;
        this.price = price;
        this.preparationTime = preparationTime;
        this.dishImage = dishImage;
    }


    public Dish(String title, BigDecimal price, int preparationTime) {
        this(title, price, preparationTime, "blank");
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

    public void setPreparationTime(int preparationTime) {
        this.preparationTime = preparationTime;
    }

    public String getDishImage() {
        return dishImage;
    }

    public void setDishImage(String dishImage) {
        this.dishImage = dishImage;
    }
}

