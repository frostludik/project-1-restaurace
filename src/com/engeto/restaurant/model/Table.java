package com.engeto.restaurant.model;

import com.engeto.restaurant.manager.RestaurantManager;
import com.engeto.restaurant.util.RestaurantException;
import java.util.List;

public class Table {
    private int tableNumber;

    public Table(int tableNumber) {
        this.tableNumber = tableNumber;
        RestaurantManager.addTable(this);
    }

    public int getTableNumber() {
        return this.tableNumber;
    }

    public static Table getTableByNumber(int tableNumber, List<Table> tables) throws RestaurantException {
        for (Table table : tables) {
            if (table.getTableNumber() == tableNumber) {
                return table;
            }
        }
        throw new RestaurantException("Table with number: " + tableNumber + " not found!");
    }

    @Override
    public String toString() {
        return String.valueOf(tableNumber);
    }
}
