package com.engeto.restaurant.model;

import com.engeto.restaurant.util.RestaurantException;
import com.engeto.restaurant.util.Settings;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
    private DishList dishList = new DishList();

    public Order() {
    }


    public Order(Dish dish, int quantity, Table table, LocalDateTime orderTime, LocalDateTime servedTime, boolean isServed, boolean isPaid, DishList dishList) throws RestaurantException {
        this.id = currentId++;
        this.dish = dish;
        setQuantity(quantity);
        this.table = table;
        this.orderTime = orderTime;
        this.servedTime = servedTime;
        this.isServed = isServed;
        this.isPaid = isPaid;
        this.dishList = dishList;
    }

    public Order(Dish dish, int quantity, Table table) throws RestaurantException {
        this(dish, quantity, table, LocalDateTime.now(), null, false, false, new DishList());
    }

    public Order(int table, LocalDateTime orderTime, LocalDateTime servedTime, Dish dish, int quantity, boolean isServed, boolean isPaid) {
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


    public List<Dish> getListOfMenuDishes() {
        try {
            dishList.importFromFile();
        } catch (RestaurantException e) {
            System.err.println(e.getLocalizedMessage());
        }
        return dishList.getDishes();
    }
    List<Dish> dishes = getListOfMenuDishes();
    public Dish getDishObjectFromStringTitle(String dishTitle) {
        for (Dish dish : dishes) {
            if (dishTitle.equals(dish.getTitle())) {
                return dish;
            }
        }
        return null;
    }

    public Order parseOrder(String data) throws RestaurantException {
        String [] items;
        try {
            items = data.split("\t");
            int table = Integer.parseInt(items[0]);
            LocalDateTime orderTime = LocalDateTime.parse(items[1]);
            LocalDateTime servedTime = LocalDateTime.parse(items[2]);
            Dish orderedDish = getDishObjectFromStringTitle(items[3]);
            int quantity = Integer.parseInt(items[4]);
            boolean isServed = Boolean.parseBoolean(items[5]);
            boolean isPaid = Boolean.parseBoolean(items[6]);
            return new Order(table, orderTime, servedTime, dish, quantity, isServed, isPaid);
        }
        catch (IllegalArgumentException e) {
            throw new RestaurantException("Unable to read data from file!");
        }
    }
}
