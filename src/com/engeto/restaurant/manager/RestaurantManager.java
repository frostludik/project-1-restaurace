package com.engeto.restaurant.manager;

import com.engeto.restaurant.model.Dish;
import com.engeto.restaurant.model.Order;
import com.engeto.restaurant.model.Table;
import com.engeto.restaurant.util.RestaurantException;
import com.engeto.restaurant.util.Settings;

import java.io.*;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class RestaurantManager {

    private static final List<Order> ordersList = new ArrayList<>();
    private static final List<Table> tablesList = new ArrayList<>();
    private static final Order order = new Order();
    private static final List<Dish> dishes = new ArrayList<>();



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


//    public static double getAverageTimeToServe() {
//        long totalSeconds = 0;
//        int ordersServed = 0;
//
//        for (Order order : ordersList) {
//            if (order.isServed() && order.getServedTime() != null && order.getOrderTime() != null) {
//                Duration duration = Duration.between(order.getOrderTime(), order.getServedTime());
//                totalSeconds += duration.getSeconds();
//                ordersServed++;
//            }
//        }
//        if (ordersServed == 0) {
//            return 0;
//        }
//        return (double) totalSeconds / ordersServed;
//    }

    public static double getAverageTimeToServe() {
        long totalSeconds = 0;
        int ordersServed = 0;

        for (Order order : ordersList) {
            if (order.isServed() && order.getOrderTime() != null) {
                Optional<LocalDateTime> servedTimeOpt = order.getServedTime();
                if (servedTimeOpt.isPresent()) {
                    Duration duration = Duration.between(order.getOrderTime(), servedTimeOpt.get());
                    totalSeconds += duration.getSeconds();
                    ordersServed++;
                }
            }
        }
        if (ordersServed == 0) {
            return 0;
        }
        return (double) totalSeconds / ordersServed;
    }



    public static List<String> getUniqueMealsOrderedToday() {
        Set<String> uniqueMealsSet = new HashSet<>();
        for (Order order : ordersList) {
            if (order.getOrderTime().toLocalDate().equals(java.time.LocalDate.now())) {
                uniqueMealsSet.add(order.getDish().getTitle());
            }
        }
        return new ArrayList<>(uniqueMealsSet);
    }

    private static String getOrderHeaderForTableOutput(int tableNumber) {
        String header = "** Objednávky pro stůl č. " + tableNumber;
        if (tableNumber < 9) {
            header = "** Objednávky pro stůl č. " + " " + tableNumber;
        }
        return header;
    }


    public static List<Order> getOrdersForTableOutput(int tableNumber) {
        int orderNumber = 1;
        List<Order> ordersForTable = new ArrayList<>();
        System.out.println(getOrderHeaderForTableOutput(tableNumber));
        System.out.println("****");

        for (Order order : ordersList) {
            if (Table.getTableNumber() == tableNumber) {
                System.out.println(orderNumber + order.getOrderFormattedForPrint());
                orderNumber += 1;
            }
        }
        System.out.println("******");
        return ordersForTable;
    }
    private static void exportOrdersToFile() throws RestaurantException {
        String orderFileName = Settings.getOrdersFile();
        try (PrintWriter outputWriter = new PrintWriter(new BufferedWriter(new FileWriter(orderFileName)))) {
            for (Order order : ordersList) {
                outputWriter.println(order.exportOrderToString());
            }
        } catch (IOException e) {
            throw new RestaurantException("Saving data to a file: " + orderFileName + " failed!");
        }
    }

    private static void exportDishesToFile() throws RestaurantException {
        String dishesFileName = Settings.getCookbookFile();
        try (PrintWriter outputWriter = new PrintWriter(new BufferedWriter(new FileWriter(dishesFileName)))) {
            for (Dish dish : dishes) {
                outputWriter.println(dish.exportToString());
            }
        } catch (IOException e) {
            throw new RestaurantException("Saving data to file: " + dishesFileName + " failed!");
        }
    }
    private static void importOrdersFromFile() throws RestaurantException {
        String orderFileName = Settings.getOrdersFile();
        try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(orderFileName)))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                ordersList.add(order.parseOrder(line));}
        } catch (FileNotFoundException e) {
            throw new RestaurantException("File " + orderFileName + " not found!");
        }
    }

    public static List<Dish> importDishesFromFile() throws RestaurantException {
        String dishesFileName = Settings.getCookbookFile();
        try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(dishesFileName)))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                dishes.add(Dish.parseDish(line)); }
        } catch (FileNotFoundException e) {
            throw new RestaurantException("File" + dishesFileName + " not found!");
        }
        return null;
    }

    public static void importDataFromFile() throws RestaurantException {
        try {
            importOrdersFromFile();
            importDishesFromFile();
        } catch (Exception e) {
            System.err.println("Error reading from the file: " + e.getMessage());
        }
    }


    public static void exportDataToFile() {
        try {
            exportOrdersToFile();
            exportDishesToFile();
        } catch (Exception e) {
            System.err.println("Error saving data to file file: " + e.getMessage());
        }
    }

    public static BigDecimal getTotalCostPerTable(int tableNumber) {
        BigDecimal totalCost = BigDecimal.ZERO;

        for (Order order : ordersList) {
            if (Table.getTableNumber() == tableNumber) {
                totalCost = totalCost.add(order.getOrderedDish().getPrice().multiply(BigDecimal.valueOf(order.getQuantity())));
            }
        }
        return totalCost;
    }

    private static void createOrder(Dish dish, int quantity, int tableNumber, LocalDateTime orderedTime, LocalDateTime fulfilmentTime,
                                    boolean isPaid) throws RestaurantException {
        Order order = new Order(dish, quantity, tableNumber);
        order.setOrderedTime(orderedTime);
        order.setServedTime(fulfilmentTime);
        order.setPaid(isPaid);
    }
}
