package com.engeto.restaurant.manager;

import com.engeto.restaurant.model.Order;
import com.engeto.restaurant.model.Table;

import java.util.ArrayList;
import java.util.List;

public class RestaurantManager {

    private static List<Order> ordersList = new ArrayList<>();
    private static List<Table> tablesList = new ArrayList<>();




    public static List<Order> getOrdersList() {
        return ordersList;
    }

    public static void setOrdersList(List<Order> ordersList) {
        RestaurantManager.ordersList = ordersList;
    }

    public static List<Table> getTablesList() {
        return tablesList;
    }

    public static void setTablesList(List<Table> tablesList) {
        RestaurantManager.tablesList = tablesList;
    }
}
