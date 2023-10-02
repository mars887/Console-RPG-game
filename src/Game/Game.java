package Game;

import Game.Items.ITEM_TYPE;
import Game.Items.Inventory;
import Game.Entity.Player;
import Game.Rooms.FightSession;
import Game.Rooms.Traider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Game extends Thread {

    public static final BufferedReader scan = new BufferedReader(new InputStreamReader(System.in));
    public boolean gameEnded = false;

    public Player player;

    public Game() {
        player = new Player(100, 100, 20, 0.85f, 1, 0, 40);
        player.inventory.add(ITEM_TYPE.BONE, 37);
        player.inventory.add(ITEM_TYPE.HEALING_POTION_15HP, 2);
        player.inventory.add(ITEM_TYPE.FABRIC, 29);
    }

    @Override
    public void run() {

        player.playerName = enterPlayerName(player);
        initGameConsoleOutput();

        System.out.println(MESSAGES.ENTERS_15);
        while (!gameEnded) {    // --------------------------------------------------------------------------- game loop
            System.out.print(MESSAGES.MENU_MESSAGE);
            int input = readUserInput(1, 4);
            System.out.println(MESSAGES.ENTERS_10);

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
                    System.out.println(MESSAGES.ENTERS_10);
                    if (player.getMoney() >= 10) {
                        Game.healPlayer(player);
                    } else if (player.getHealth() == player.getMaxHealth()) {
                        System.out.println(MESSAGES.ENTERS_10);
                        System.out.println(player.playerName + " полностью здоров");
                        System.out.println(MESSAGES.ENTERS_5);
                    } else {
                        System.out.println(MESSAGES.ENTERS_10);
                        System.out.println("Недостаточно монет...");
                        System.out.println(MESSAGES.ENTERS_5);
                    }
                    break;
                case 3:
                    FightSession fightSession = new FightSession(player);
                    fightSession.start();
                    try {
                        fightSession.join();
                    } catch (InterruptedException e) {
                    }
                    if (!fightSession.isWin) player.restoreHp();
                    break;
                case 4:
                    Inventory.printInventory(player);
                    break;
                case 5:
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n--- ok ---\n\n\n\n\n\n\n\n\n");
                    gameEnded = true;
            }
        }
    }

    private void initGameConsoleOutput() {
        System.out.println("----------------------------------------------------");
        System.out.println("|\n|\n|\n|\n|\n|\n|\n|\n|\n|\n|  " + player.playerName
                + " будет очень рад если вы правильно настроите высоту консоли\n|\n|\n|\n|\n|\n|\n|\n|\n|\n|"
                + "  Введите что-нибуть для продолжения");
        System.out.print("----------------------------------------------------");
        try {
            scan.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String enterPlayerName(Player player) {
        System.out.println(MESSAGES.ENTERS_10);
        System.out.print("Введите имя персонажа - ");
        String name = null;
        do {
            if (name != null) System.out.print("Неправильное имя, попробуйте снова - ");
            try {
                name = scan.readLine();
            } catch (IOException e) {
                System.out.print("\nВозникла ошибка, попробуйте снова - ");
            }
        } while (name == null || name.length() <= 1);
        return (String.valueOf(name.charAt(0)).toUpperCase() + name.substring(1, name.length()));
    }

    public static void healPlayer(Player player) {
        player.setHealth(player.getMaxHealth());
        player.setMoney(player.getMoney() - 10);
        System.out.println("Теперь " + player.playerName + " полностью здоров!\n");
        System.out.println(MESSAGES.ENTERS_5);
    }

    public static void countPrint(String message, int i) {
        for (int j = 0; j < i; j++) System.out.print(message);
    }
    public static void printWithBorder(String message, int i) {
        System.out.print(message);
        countPrint(" ",i - message.length());
    }
    public static void printComparisonWithBorder(String message, int i,String message1) {
        printWithBorder(message,i - message1.length());
        System.out.print(message1);
    }
    public static void printComparisonWithBorder(int value,int i, int value1) {
        printComparisonWithBorder(String.valueOf(value),i,String.valueOf(value1));
    }

    public static int readUserInput(int a, int b) {
        while (true) {
            int input = 0;
            try {
                input = Integer.parseInt(readScanner());
            } catch (IOException e) {
                System.out.print("что-то не так...\nПопробуй заново - ");
                continue;
            } catch (NumberFormatException e) {
                System.out.print("это не число...\nПопробуй заново - ");
                continue;
            }
            if (input >= a && input <= b) {
                return input;
            } else {
                System.out.print("Нет такого варианта...\nПопробуй заново - ");
            }
        }
    }

    public static String readScanner() throws IOException {
        return scan.readLine();
    }
}
