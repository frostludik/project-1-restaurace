package com.engeto.restaurant.manager;

import com.engeto.restaurant.model.Dish;
import com.engeto.restaurant.model.Order;
import com.engeto.restaurant.model.Table;

import java.time.Duration;
import java.util.*;

public class RestaurantManager {

    private static List<Order> ordersList = new ArrayList<>();
    private static List<Table> tablesList = new ArrayList<>();


    public static List<Order> getOrdersList() {
        return ordersList;
    }

    public static void addOrderToOrderList(Order order) {
        ordersList.add(order);
    }

    public static List<Table> getTablesList() {
        return tablesList;
    }

    public static void addTable(Table table) {
        tablesList.add(table);
    }

    //how many orders are in progress and not served yet
    public static int getOrdersInProgress() {
        int ordersInProgress = 0;
        for (Order order : ordersList) {
            if (!order.isServed()) {
                ordersInProgress++;
            }
        }
        return ordersInProgress;
    }

    //sort orders by order time
    public static List<Order> sortOrdersByOrderTime() {
        List<Order> sortedOrders = new ArrayList<>(ordersList);
        sortedOrders.sort(Comparator.comparing(Order::getOrderTime));
        return sortedOrders;
    }

    //average time to fulfill an order
    public double getAverageTimeToServe() {
        long totalSeconds = 0;
        int ordersServed = 0;

        for (Order order : ordersList) {
            if (order.isServed() && order.getServedTime() != null && order.getOrderTime() != null) {
                Duration duration = Duration.between(order.getOrderTime(), order.getServedTime());
                totalSeconds += duration.getSeconds();
                ordersServed++;
            }
        }
        if (ordersServed == 0) {
            return 0;
        }
        return (double) totalSeconds / ordersServed;
    }

    //list of meals ordered today (show just unique meals, do not repeat the meal if it was ordered multiple times)
    public List<String> getUniqueMealsOrderedToday() {
        Set<String> uniqueMealsSet = new HashSet<>();
        for (Order order : ordersList) {
            if (order.getOrderTime().toLocalDate().equals(java.time.LocalDate.now())) {
                uniqueMealsSet.add(order.getDish().getTitle());
            }
        }
        return new ArrayList<>(uniqueMealsSet);
    }

}
