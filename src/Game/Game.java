package Game;

import Game.Entity.DataSupplier;
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
        player = new Player(100, 100, 20, DataSupplier.DexterityFunction.apply(1,0.7f), 1, 0, 400);
        player.inventory.add(ITEM_TYPE.BONE, 137);
        player.inventory.add(ITEM_TYPE.HEALING_POTION_15HP, 2);
        player.inventory.add(ITEM_TYPE.FABRIC, 29);
    }

    @Override
    public void run() {

        player.playerName = enterPlayerName();
        initGameConsoleOutput();
        printHelp();

        System.out.println(MESSAGES.ENTERS_15);
        while (!gameEnded) {    // --------------------------------------------------------------------------- game loop
            System.out.print(MESSAGES.MENU_MESSAGE);
            int input = readUserInput(1, 6, 0);
            System.out.println(MESSAGES.ENTERS_10);

            switch (input) {    // cases
                case 1:
                    while(goToTraider()) {}
                    break;
                case 2:
                    System.out.println(MESSAGES.MOB_RUSH_NOT_AVAILABLE);
                    break;
                case 3:
                    while(goToForest()) {}
                    break;
                case 4:
                    goToMedic();
                    break;
                case 5:
                    Inventory.printInventory(player);
                    break;
                case 0:
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n--- ok ---\n\n\n\n\n\n\n\n\n");
                    gameEnded = true;
            }
        }
    }

    private void printHelp() {
        System.out.println(MESSAGES.ENTERS_5);
        System.out.println("0 это всегда назад или выход");
        System.out.println("даже если нет такого варианта");
        System.out.println(MESSAGES.ENTERS_5);
        System.out.println("enter для продолжения");
        try {
            scan.readLine();
        } catch (IOException e) {
        }
    }

    private boolean goToForest() {
        FightSession fightSession = new FightSession(player);
        fightSession.start();
        try {
            fightSession.join();
        } catch (InterruptedException e) {
        }
        if(fightSession.continueFight) return true;
        if (!fightSession.isWin) player.restoreHp();
        return false;
    }

    private boolean goToTraider() {
        Traider traider = new Traider(player);
        traider.start();
        try {
            traider.join();
        } catch (InterruptedException e) {
        }
        return false;
    }

    private void goToMedic() {
        System.out.println(MESSAGES.ENTERS_10);
        if (player.getHealth() == player.getMaxHealth()) {
            System.out.println(MESSAGES.ENTERS_10);
            System.out.println(player.playerName + " полностью здоров\n");
            System.out.println(MESSAGES.ENTERS_5);
        } else if (player.getMoney() >= 10) {
            Game.healPlayer(player);
        } else {
            System.out.println(MESSAGES.ENTERS_10);
            System.out.println("Недостаточно монет...");
            System.out.println(MESSAGES.ENTERS_5);
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

    private String enterPlayerName() {
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

    public static int readUserInput(int a, int b, int extra) {
        while (true) {
            int input;
            try {
                input = Integer.parseInt(scan.readLine());
            } catch (IOException e) {
                System.out.print("что-то не так...\nПопробуй заново - ");
                continue;
            } catch (NumberFormatException e) {
                System.out.print("это не число...\nПопробуй заново - ");
                continue;
            }
            if(extra == input) return extra;
            if (input >= a && input <= b) {
                return input;
            } else {
                System.out.print("Нет такого варианта...\nПопробуй заново - ");
            }
        }
    }
}
