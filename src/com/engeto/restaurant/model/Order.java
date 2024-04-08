package com.engeto.restaurant.model;

import com.engeto.restaurant.util.RestaurantException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Order {

    private long id;
    private static long currentId = 1;
    private Dish dish;
    private int quantity;
    private Table table;
    private LocalDateTime orderTime;
    private LocalDateTime servedTime;
    private boolean isServed;
    private boolean isPaid;

    public Order(Dish dish, int quantity, Table table, LocalDateTime orderTime, LocalDateTime servedTime, boolean isServed, boolean isPaid) throws RestaurantException {
        this.id = currentId++;
        this.dish = dish;
        setQuantity(quantity);
        this.table = table;
        this.orderTime = orderTime;
        this.servedTime = servedTime;
        this.isServed = isServed;
        this.isPaid = isPaid;
    }

    public Order(Dish dish, int quantity, Table table) throws RestaurantException {
        this(dish, quantity, table, LocalDateTime.now(), null, false, false);
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

    public void setQuantity(int quantity) throws RestaurantException {
        if (quantity <= 0) {
            throw new RestaurantException("Quantity must be greater than 0. You entered: " + quantity);
        }
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

    public String exportOrderToString() {
        return table + "\t" + orderTime + "\t" + servedTime + "\t" + dish + "\t" + quantity + "\t" + isServed + "\t" + isPaid;
    }

    public String getOrderTimeFormatted() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return orderTime.format(formatter);
    }
    public String getServedTimeFormatted() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return servedTime.format(formatter);
    }
    public String getOrderFormattedForPrint() {

        String paidText = isPaid ? "zaplaceno" : "";
        return "." + " " + dish + " " + quantity + "x (" + dish.getPrice() + " Kč) " + ":" + "\t"
                + getOrderTimeFormatted() + "-" + getServedTimeFormatted() + "\t" + paidText;
    }

}
