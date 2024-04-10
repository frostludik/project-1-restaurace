import com.engeto.restaurant.manager.RestaurantManager;
import com.engeto.restaurant.model.CookBook;
import com.engeto.restaurant.model.Dish;
import com.engeto.restaurant.model.Order;
import com.engeto.restaurant.model.Table;
import com.engeto.restaurant.util.RestaurantException;
import com.engeto.restaurant.util.Settings;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
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
        System.out.println("\n** Celková cena konzumace pro daný stůl je: " + RestaurantManager.getTotalCostPerTable(15) + " Kč.");

        //4. Použij všechny připravené metody pro získání informací pro management — údaje vypisuj na obrazovku.
        System.out.println("\n** Počet rozpracovaných a nedokončených objednávek: " + RestaurantManager.getOrdersInProgress());
        System.out.println("\n** Objednávky seřazené podle času:");
        List<Order> sortedOrders = RestaurantManager.sortOrdersByOrderTime();
        for (Order order : sortedOrders) {
            System.out.println(order);
        }
        System.out.println("\n** Průměrná doba zpracování objednávek: " + RestaurantManager.getAverageTimeToServe());
        System.out.println("\n** Seznam jídel, která byla dnes objednána:");
        List<String> uniqueMealsOrderedToday = RestaurantManager.getUniqueMealsOrderedToday();
        for (String meal : uniqueMealsOrderedToday) {
            System.out.println(meal);
        }

        System.out.println("\n** Export seznamu objednávek pro jeden stůl:");
        RestaurantManager.getOrdersForTableOutput(15);

        //5. Změněná data ulož na disk.
        RestaurantManager.exportDataToFile();
        System.out.println("\n** Data byla exportována do souboru.");


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
        Dish rizek = CookBook.getDishFromCookBookById(1);
        Dish hranolky = CookBook.getDishFromCookBookById(2);
        Dish pstruh = CookBook.getDishFromCookBookById(3);
        Dish kofola = CookBook.getDishFromCookBookById(4);

        Order firstOrder = new Order(rizek, 2, 15);
        Order secondOrder = new Order(hranolky, 2, 15);
        Order thirdOrder = new Order(kofola, 2, 15);
        thirdOrder.serveOrder();

        Order fourthOrder = new Order(pstruh, 1, 2);
        Order fifthOrder = new Order(kofola, 1, 2);
        //fifthOrder.serveOrder();
    }


}
