package com.engeto.restaurant.model;

import com.engeto.restaurant.manager.RestaurantManager;
import com.engeto.restaurant.util.RestaurantException;
import com.engeto.restaurant.util.ValidationUtils;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;


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

    public Order() {
    }


    public Order(Dish dish, int quantity, int tableNumber, LocalDateTime orderTime, LocalDateTime servedTime, boolean isPaid) throws RestaurantException {
        this.id = currentId++;
        this.dish = dish;
        setQuantity(quantity);
        this.table = Table.getTableByNumber(tableNumber, RestaurantManager.getTablesList());
        this.orderTime = orderTime;
        this.servedTime = servedTime;
        this.isPaid = isPaid;
        RestaurantManager.addOrderToOrderList(this);
    }

    public Order(Dish dish, int quantity, int tableNumber) throws RestaurantException {
        this(dish, quantity, tableNumber, LocalDateTime.now(), null,false);
    }

    public Order(int tableNumber, LocalDateTime orderTime, LocalDateTime servedTime, Dish dish, int quantity, boolean isServed, boolean isPaid) throws RestaurantException {
        this(dish, quantity, tableNumber, LocalDateTime.now(), null, false);
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


//    public LocalDateTime getServedTime() {
//        return this.servedTime;
//    }
    public Optional<LocalDateTime> getServedTime() {
        return Optional.ofNullable(this.servedTime);
    }
    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }
    public Dish getOrderedDish() {
        return dish;
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

    public void setOrderedTime(LocalDateTime orderedTime) throws RestaurantException {
        ValidationUtils.validateTime(orderedTime, "Ordered time");
        this.orderTime = orderedTime;
    }

    public void setServedTime(LocalDateTime servedTime) throws RestaurantException {
        if (servedTime != null) {
            ValidationUtils.validateTime(servedTime, "Served time");
            if (servedTime.isBefore(orderTime)) {
                throw new RestaurantException("Served time cannot be before order time!");
            }
        }
        this.servedTime = servedTime;
        this.isServed = true;
    }


    public String exportOrderToString() {
        return table + "\t" + orderTime + "\t" + servedTime + "\t" + dish + "\t" + quantity + "\t" + isServed + "\t" + isPaid;
    }

    public String getOrderTimeFormatted() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return orderTime.format(formatter);
    }
//    public String getServedTimeFormatted() {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
//        return getServedTime().format(formatter);
//    }
    public String getServedTimeFormatted() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return getServedTime()
                .map(servedTime -> servedTime.format(formatter))
                .orElse("Not served");
}



    public String getOrderFormattedForPrint() {

        String paidText = isPaid ? "zaplaceno" : "";
        return "." + " " + dish + " " + quantity + "x (" + dish.getPrice() + " Kč) " + ":" + "\t"
                + getOrderTimeFormatted() + "-" + getServedTimeFormatted() + "\t" + paidText;
    }

    public Order parseOrder(String data) throws RestaurantException {String [] items;
        try {
            items = data.split("\t");
            int table = Integer.parseInt(items[0]);
            LocalDateTime orderTime = LocalDateTime.parse(items[1]);
            LocalDateTime servedTime = LocalDateTime.parse(items[2]);
            Dish dish = Dish.parseDish(items[3]);
            int quantity = Integer.parseInt(items[4]);
            boolean isServed = Boolean.parseBoolean(items[5]);
            boolean isPaid = Boolean.parseBoolean(items[6]);
            return new Order(table, orderTime, servedTime, dish, quantity, isServed, isPaid);
        }
        catch (IllegalArgumentException e) {
            throw new RestaurantException("Unable to read data from file!");
        }
    }

    public void fulfilOrder() {
        servedTime = LocalDateTime.now();
    }
}
