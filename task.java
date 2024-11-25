import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class task {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Ресторан розпочав роботу...");

        // runAsync() – імітує старт асинхронної задачі
        CompletableFuture<Void> startTasks = CompletableFuture.runAsync(() -> {
            System.out.println("Готуємося прийняти замовлення... (асинхронно)");
            sleep(1000);
        });

        // supplyAsync() – імітує завантаження списку замовлень
        CompletableFuture<List<String>> fetchOrders = CompletableFuture.supplyAsync(() -> {
            System.out.println("Отримуємо список замовлень...");
            sleep(2000);
            return Arrays.asList("Піца", "Бургер", "Салат", "Суші", "Десерт");
        });

        // thenApplyAsync() – модифікує замовлення
        CompletableFuture<List<String>> prepareOrders = fetchOrders.thenApplyAsync(orders -> {
            System.out.println("Готуємо замовлення...");
            sleep(2000);
            return orders.stream()
                    .map(order -> order + " (час приготування: " + (ThreadLocalRandom.current().nextInt(5, 15)) + " хв.)")
                    .collect(Collectors.toList());
        });

        // thenAcceptAsync() – виводить список готових замовлень
        CompletableFuture<Void> displayOrders = prepareOrders.thenAcceptAsync(preparedOrders -> {
            System.out.println("Замовлення готові до видачі:");
            preparedOrders.forEach(System.out::println);
        });

        // thenRunAsync() – виводить повідомлення про прийняття замовлення
        displayOrders.thenRunAsync(() -> {
            System.out.println("Усі задачі завершено. ");
        });

        // затримка основного потоку, щоб дочекався завершення асинхронних задач
        Thread.sleep(7000);
    }

    private static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
