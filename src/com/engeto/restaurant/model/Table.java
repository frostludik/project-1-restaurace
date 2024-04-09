package com.engeto.restaurant.model;

import com.engeto.restaurant.manager.RestaurantManager;
import com.engeto.restaurant.util.RestaurantException;
import java.util.List;

public class Table {
    private static int tableNumber;

    public Table(int tableNumber) {
        this.tableNumber = tableNumber;
        RestaurantManager.addTable(this);
    }

    public static int getTableNumber() {
        return tableNumber;
    }

    public static Table getTableByNumber(int tableNumber, List<Table> tables) throws RestaurantException {
        for (Table table : tables) {
            if (table.getTableNumber() == tableNumber) {
                return table;
            }
        }
        throw new RestaurantException("Table with number: " + tableNumber + " not found!");
    }

}
