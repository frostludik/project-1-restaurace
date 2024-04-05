package com.engeto.restaurant.model;

import java.time.LocalDateTime;

public class Order {

    private long id;
    private Table table;
    private LocalDateTime orderTime;
    private LocalDateTime servedTime;
    private Dish dish;
    private int quantity;
    private boolean isServed;
    private boolean isPaid;

    public Order(long id, Table table, LocalDateTime orderTime, LocalDateTime servedTime, Dish dish, int quantity, boolean isServed, boolean isPaid) {
        this.id = id;
        this.table = table;
        this.orderTime = orderTime;
        this.servedTime = servedTime;
        this.dish = dish;
        this.quantity = quantity;
        this.isServed = isServed;
        this.isPaid = isPaid;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(LocalDateTime orderTime) {
        this.orderTime = orderTime;
    }

    public LocalDateTime getServedTime() {
        return servedTime;
    }

    public void setServedTime(LocalDateTime servedTime) {
        this.servedTime = servedTime;
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isServed() {
        return isServed;
    }

    public void setServed(boolean served) {
        isServed = served;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }
}
