package com.engeto.restaurant.model;

import com.engeto.restaurant.manager.RestaurantManager;
import com.engeto.restaurant.util.RestaurantException;
import com.engeto.restaurant.util.ValidationUtils;
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
    private boolean isServed = false;
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

    public LocalDateTime getServedTime() {
        return servedTime;
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
        return this.isServed;
    }

    public void setServed() {
        this.servedTime = LocalDateTime.now();
        this.isServed = true;
    }

    public void setServedOwnTime(LocalDateTime servedTime) {     //odebrat, jen pro testování
        this.servedTime = servedTime; }

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

    public String getServedTimeFormatted() {
        LocalDateTime servedTime = getServedTime();
        if (servedTime == null) {
            return "";
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            return servedTime.format(formatter);
        }
    }

    public String getOrderFormattedForPrint() {

        String paidText = isPaid ? "zaplaceno" : "";
        return "." + " " + dish + " " + quantity + "x (" + dish.getPrice() + " Kč) " + ":" + "\t"
                + getOrderTimeFormatted() + "-" + getServedTimeFormatted() + "\t" + paidText;
    }


    public static Dish getDishById(long dishId) throws RestaurantException {
        try {
            return CookBook.getDishFromCookBookById(dishId);
        } catch (RestaurantException e) {
            throw new RestaurantException(e.getMessage());
        }
    }
    public static void parseOrderLine(String line) throws RestaurantException {
        String[] parts = line.split(";");
        ValidationUtils.validateNumberOfFields(parts, 7);

        long id = Long.parseLong(parts[0]);
        int tableNumber = Integer.parseInt(parts[1]);
        long dishId = Long.parseLong(parts[2]);
        int quantity = Integer.parseInt(parts[3]);
        LocalDateTime orderTime = LocalDateTime.parse(parts[4]);
        LocalDateTime servedTime = parts[5].trim().equals("null") ? null : LocalDateTime.parse(parts[5]);
        boolean isPaid = Boolean.parseBoolean(parts[6]);

        Dish dish = getDishById(dishId);
        createOrder(dish, quantity, tableNumber, orderTime, servedTime, isPaid);
    }

    public static void createOrder(Dish dish, int quantity, int tableNumber, LocalDateTime orderedTime, LocalDateTime servedTime,
                                    boolean isPaid) throws RestaurantException {
        Order order = new Order(dish, quantity, tableNumber);
        order.setOrderedTime(orderedTime);
        order.setServedTime(servedTime);
        order.setPaid(isPaid);
    }

    @Override
    public String toString() {
        return String.format("Objednávka { id: %3d, jídlo: %-30s, ks: %2d, stůl: %-2s, čas objednání: %-30s, čas vyřízení: %-30s, isServed: %-5b, isPaid: %-5b}",
                id,
                dish,
                quantity,
                table.getTableNumber(),
                orderTime,
                (servedTime != null ? servedTime : ""),
                isServed,
                isPaid);
    }
}
