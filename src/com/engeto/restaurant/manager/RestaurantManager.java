package com.engeto.restaurant.manager;

import com.engeto.restaurant.model.CookBook;
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


    public static double getAverageTimeToServe() {
        long totalMinutes = 0;
        int ordersServed = 0;

        for (Order order : ordersList) {
            if (order.isServed() && order.getOrderTime() != null) {
                LocalDateTime servedTime = order.getServedTime();
                if (servedTime != null) {
                    Duration duration = Duration.between(order.getOrderTime(), servedTime);
                    totalMinutes += duration.toMinutes();
                    ordersServed++;
                }
            }
        }
        if (ordersServed == 0) {
            return 0;
        }
        return (double) totalMinutes / ordersServed;
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
        String header = "\n** Objednávky pro stůl č. " + tableNumber;
        if (tableNumber < 9) {
            header = "\n** Objednávky pro stůl č. " + " " + tableNumber;
        }
        return header;
    }

    public static void getOrdersForTableOutput(int tableNumber) {
        int orderNumber = 1;
        List<Order> ordersForTable = new ArrayList<>();
        System.out.println(getOrderHeaderForTableOutput(tableNumber));
        System.out.println("****");

        for (Order order : ordersList) {
            if (order.getTable().getTableNumber() == tableNumber) {
                System.out.println(orderNumber + ". " + order.getOrderFormattedForPrint());
                orderNumber += 1;
            }
        }
        System.out.println("******");
    }

    public static void exportOrdersToFile() {
        String orderFileName = Settings.getOrdersFile();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(orderFileName, false))) {
            for (Order order : ordersList) {
                Table orderTable = order.getTable();
                int tableNumber = order.getTable().getTableNumber();
                String line = String.format("%s;%s;%s;%s;%s;%s;%s",
                        order.getId(), tableNumber, order.getOrderedDish().getId(),
                        order.getQuantity(), order.getOrderTime(), order.getServedTime(), order.isPaid());
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error exporting orders to file: " + e.getMessage());
        }
    }


    public static void exportDishesToFile() {
        String dishesFileName = Settings.getCookbookFile();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(dishesFileName))) {
            for (Map.Entry<Long, Dish> entry : CookBook.getAllDishes().entrySet()) {
                Dish dish = entry.getValue();
                String line = String.format("%s;%s;%s;%s;%s",
                        dish.getId(), dish.getTitle(), dish.getPrice(),
                        dish.getPreparationTime(), dish.getDishImage());
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error exporting dishes to file: " + e.getMessage());
        }
    }



    private static void importOrdersFromFile() throws RestaurantException {
        String orderFileName = Settings.getOrdersFile();
        int lineNumber = 1;
        String line = null;
        try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(orderFileName)))) {
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                Order.parseOrderLine(line);
                lineNumber++;
            }
        } catch (FileNotFoundException e) {
            throw new RestaurantException("File " + orderFileName + " not found!" + e.getLocalizedMessage());
        } catch (Exception e) {
            throw new RestaurantException("An error occurred while processing file " + orderFileName + ": " + e.getMessage());
        }
    }


    public static void importDishesFromFile() throws RestaurantException {
        String dishesFileName = Settings.getCookbookFile();
        try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(dishesFileName)))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                dishes.add(Dish.parseDish(line));
            }
        } catch (FileNotFoundException e) {
            throw new RestaurantException("File" + dishesFileName + " not found!");
        } catch (Exception e) {
            throw new RestaurantException("An error occurred while processing file " + dishesFileName + ": " + e.getMessage());
        }
    }

    public static void importDataFromFile() throws RestaurantException {
        try {
            importDishesFromFile();
            importOrdersFromFile();
        } catch (RestaurantException e) {
            System.err.println("Error reading from the file: " + e.getMessage());
        }
    }


    public static void exportDataToFile() {
        try {
            exportDishesToFile();
            exportOrdersToFile();
        } catch (Exception e) {
            System.err.println("Error saving data to file file: " + e.getMessage());
        }
    }

    public static BigDecimal getTotalCostPerTable(int tableNumber) {
        BigDecimal totalCost = BigDecimal.ZERO;

        for (Order order : ordersList) {
            if (order.getTable().getTableNumber() == tableNumber) {
                totalCost = totalCost.add(order.getOrderedDish().getPrice().multiply(BigDecimal.valueOf(order.getQuantity())));
            }
        }
        return totalCost;
    }

}
