package com.engeto.restaurant.manager;

import com.engeto.restaurant.model.Dish;
import com.engeto.restaurant.model.Order;
import com.engeto.restaurant.model.Table;
import com.engeto.restaurant.util.RestaurantException;
import com.engeto.restaurant.util.Settings;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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


    public static int getOrdersInProgress() {
        int ordersInProgress = 0;
        for (Order order : ordersList) {
            if (!order.isServed()) {
                ordersInProgress++;
            }
        }
        return ordersInProgress;
    }

    public static List<Order> sortOrdersByOrderTime() {
        List<Order> sortedOrders = new ArrayList<>(ordersList);
        sortedOrders.sort(Comparator.comparing(Order::getOrderTime));
        return sortedOrders;
    }


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


    public List<String> getUniqueMealsOrderedToday() {
        Set<String> uniqueMealsSet = new HashSet<>();
        for (Order order : ordersList) {
            if (order.getOrderTime().toLocalDate().equals(java.time.LocalDate.now())) {
                uniqueMealsSet.add(order.getDish().getTitle());
            }
        }
        return new ArrayList<>(uniqueMealsSet);
    }

    private String getOrderHeaderForTableOutput(int tableNumber) {
        String header = "** Objednávky pro stůl č. " + tableNumber;
        if (tableNumber < 9) {
            header = "** Objednávky pro stůl č. " + " " + tableNumber;
        }
        return header;
    }


    public List<Order> getOrdersForTableOutput(int tableNumber) {
        int orderNumber = 1;
        List<Order> ordersForTable = new ArrayList<>();
        System.out.println(getOrderHeaderForTableOutput(tableNumber));
        System.out.println("****");

        for (Order order : ordersList) {
            if (order.getTable().getTableNumber() == tableNumber) {
                System.out.println(orderNumber + order.getOrderFormattedForPrint());
                orderNumber += 1;
            }
        }
        System.out.println("******");
        return ordersForTable;
    }
    public void exportToFile() throws RestaurantException {
        String fileName = Settings.getOrdersFile();
        try (PrintWriter outputWriter = new PrintWriter(new BufferedWriter(new FileWriter(fileName)))) {
            for (Order order : ordersList) {
                outputWriter.println(order.exportOrderToString());
            }
        } catch (IOException e) {
            throw new RestaurantException("Saving data to a file: " + fileName + " failed!");
        }
    }


}
