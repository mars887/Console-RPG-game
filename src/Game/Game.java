package Game;

import Game.Items.ITEM_TYPE;
import Game.Items.Inventory;
import Game.Entity.Player;
import Game.Rooms.FightSession;
import Game.Rooms.Traider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

public class Game extends Thread {

    public static final BufferedReader scan = new BufferedReader(new InputStreamReader(System.in));
    public boolean gameEnded = false;

    public Player player;

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
            player.setPlayerName(scan.readLine());
        } catch (IOException e) {
            System.out.println("возникла ошибка поэтому персонаж теперь Вася\n\n\n");
            player.setPlayerName("Вася");
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
                    FightSession fightSession = new FightSession(player);
                    fightSession.start();
                    try {
                        fightSession.join();
                    } catch (InterruptedException e) {
                    }
                    break;
                case 3:
                    Inventory.printInventory(player);
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
