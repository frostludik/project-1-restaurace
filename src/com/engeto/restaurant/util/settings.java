package com.engeto.restaurant.util;

public class settings {
    private static final String COOKBOOK_FILE = "resources/cookbook.txt";
    private static final String ORDERS_FILE = "resources/orders.txt";

    public static String getCookbookFile() {
        return COOKBOOK_FILE;
    }
    public static String getOrdersFile() {
        return ORDERS_FILE;
    }
}
