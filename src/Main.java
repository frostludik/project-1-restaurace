import com.engeto.restaurant.manager.RestaurantManager;
import com.engeto.restaurant.model.CookBook;
import com.engeto.restaurant.model.Dish;
import com.engeto.restaurant.model.Order;
import com.engeto.restaurant.model.Table;
import com.engeto.restaurant.util.RestaurantException;
import com.engeto.restaurant.util.Settings;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

public class Main {
    public static void main(String[] args) throws RestaurantException {

        createTables(15);

        //1.Načti stav evidence z disku
        RestaurantManager.importDataFromFile();

        //2.Připrav testovací data
        createDishes();
        createOrders();


        //3. Vypiš celkovou cenu konzumace pro stůl číslo 15.
        System.out.println("\nCelková cena konzumace pro daný stůl je: " + RestaurantManager.getTotalCostPerTable(15) + " Kč.");

        //4. Použij všechny připravené metody pro získání informací pro management — údaje vypisuj na obrazovku.
        System.out.println("\nPočet rozpracovaných a nedokončených objednávek: " + RestaurantManager.getOrdersInProgress());
        System.out.println("\nObjednávky seřazené podle času: \n" + RestaurantManager.sortOrdersByOrderTime());
        System.out.println("\nPrůměrná doba zpracování objednávek: " + RestaurantManager.getAverageTimeToServe());
        System.out.println("\nSeznam jídel, která byla dnes objednána: \n" + RestaurantManager.getUniqueMealsOrderedToday());
        System.out.println("\nExport seznamu objednávek pro jeden stůl: \n" + RestaurantManager.getOrdersForTableOutput(15));

        //5. Změněná data ulož na disk.
        RestaurantManager.exportDataToFile();


    }
    private static void createTables(int numberOfTables) {
        for (int i = 1; i <= numberOfTables; i++) {
            new Table(i);
        }
    }

    private static void createDishes() throws RestaurantException {
        Dish rizek = new Dish("Kuřecí řízek obalovaný 150 g", BigDecimal.valueOf(185), 25);
        Dish hranolky = new Dish("Hranolky 150 g", BigDecimal.valueOf(70), 10);
        Dish pstruh = new Dish("Pstruh na víně 200 g", BigDecimal.valueOf(220), 40);
        Dish kofola = new Dish("Kofola 0,5 l", BigDecimal.valueOf(45), 5);
    }

    private static void createOrders() throws RestaurantException {
        Dish rizek = CookBook.getDishById(1);
        Dish hranolky = CookBook.getDishById(2);
        Dish pstruh = CookBook.getDishById(3);
        Dish kofola = CookBook.getDishById(4);

        Order firstOrder = new Order(rizek, 2, 15);
        Order secondOrder = new Order(hranolky, 2, 15);
        Order thirdOrder = new Order(kofola, 2, 15);
        thirdOrder.fulfilOrder();
    }
}
