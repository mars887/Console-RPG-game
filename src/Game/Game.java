package Game;

import Game.Entity.Items.ITEM_TYPE;
import Game.Entity.Items.ItemStack;
import Game.Entity.Player;
import Game.Rooms.Traider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class Game extends Thread {

    public static final BufferedReader scan = new BufferedReader(new InputStreamReader(System.in));
    public boolean gameEnded = false;

    public Player player;
    public String playerName;

    public Game() {
        player = new Player(100, 100, 10, 0.6f, 1, 0, 30);
        player.inventory.add(ITEM_TYPE.BONE, 37);
        player.inventory.add(ITEM_TYPE.HEALING_POTION_15HP, 1);
        player.inventory.add(ITEM_TYPE.FABRIC, 29);
    }

    @Override
    public void run() {

        System.out.print("Введите имя персонажа - ");   // ----------------------------------------------- create player
        try {
            playerName = scan.readLine();
        } catch (IOException e) {
            System.out.println("возникла ошибка поэтому персонаж теперь Вася\n\n\n");
            playerName = "Вася";
        }


        while (!gameEnded) {    // --------------------------------------------------------------------------- game loop
            System.out.println(MESSAGES.MENU_MESSAGE);

            int input = readUserInput(1, 4);
            System.out.println("\n\n\n\n\n\n\n\n\n");

            switch (input) {    // cases
                case 1:
                    Traider traider = new Traider(player);
                    traider.start();
                    try {
                        traider.join();
                    } catch (InterruptedException e) {
                    }
                    break;
                case 2:
                    System.out.println(MESSAGES.DANGEON_NOT_AVAILABLE);
                    break;
                case 3:
                    System.out.println("В инвентаре лежит...");
                    Stream<ItemStack> list = player.inventory.getStream();
                    AtomicInteger fullCost = new AtomicInteger();

                    list.peek(x -> fullCost.addAndGet(x.getQuantity() * x.itemType.costSell))
                            .forEach(x -> {
                                System.out.print("   " + x.itemType.getRuName());
                                countPrint(" ", 30 - x.itemType.getRuName().length());
                                System.out.println("количество - " + x.getQuantity());
                            });

                    System.out.println("Общая стоимость инвентаря - " + fullCost + " монет");
                    System.out.println("Монет имеется - " + player.getMoney());
                    System.out.println("Уровень - " + player.getLevel());
                    System.out.println("Опыт - " + player.getExp());
                    System.out.println("Нужно опыта до следующего уровня - " + player.getXpForNextLevel());
                    System.out.println("\n");
                    break;
                case 4:
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n--- ok ---\n\n\n\n\n\n\n\n\n");
                    gameEnded = true;
            }
        }
    }

    public static void countPrint(String message, int i) {
        for (int j = 0; j < i; j++) System.out.print(message);
    }

    public static int readUserInput(int a, int b) {
        while (true) {
            int input = 0;
            try {
                input = Integer.parseInt(readScanner());
            } catch (IOException e) {
                System.out.println("что-то не так...\nПопробуй заново\n\n\n");
            } catch (NumberFormatException e) {
                System.out.println("это не число...\nПопробуй заново\n\n\n");
            }
            if (input >= a && input <= b) {
                return input;
            } else {
                System.out.println("Нет такого варианта...\nПопробуй заново\n\n\n");
            }
        }
    }

    public static String readScanner() throws IOException {
        return scan.readLine();
    }
}
