package Game.Rooms.Fights;

import Game.Entity.*;
import Game.Items.ITEM_TYPE;
import Game.*;

import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import static Game.Game.readUserInput;

public class FightSession extends Thread {

    public final Player player;
    public final Monster monster;

    public boolean isWin = true;

    private boolean isFighting = true; // 1 level
    private boolean isRunned = false;  // 2 level
    private boolean isPlayerHit;       // val 1

    public boolean printStartStats = true;
    public boolean giveDrop = true;
    public boolean startQuestion = true;

    public FightSession(Player player, Monster monster) {
        this.player = player;
        this.monster = monster;
    }

    @Override
    public void run() {

        while (isFighting) {
            if(printStartStats) Fight.printStats(player, monster);
            if(startQuestion) System.out.println("\n1. Начать бой\n2. Сбежать");

            if (!startQuestion || readUserInput(1, 2, 0) == 1) {

                Random rand = new Random();
                isPlayerHit = rand.nextBoolean();

                System.out.println(MESSAGES.ENTERS_10);
                System.out.println("Первый удар наносит " + (isPlayerHit ? player.playerName : monster.ruType) + "\n");

                while (!isRunned) {
                    if (isPlayerHit) {

                        switch (playerHit(rand)) {
                            case 0 -> {
                                System.out.println(player.playerName + " промахивается\n");
                                isPlayerHit = !isPlayerHit;
                            }
                            case 1 -> {
                                Fight.printHealths(player, monster);
                                System.out.println("\n\n\n");
                                isPlayerHit = !isPlayerHit;
                            }
                            case 2 -> {
                                if(giveDrop) giveDrop(player, monster);
                                return;
                            }
                        }

                    } else {

                        switch (monsterHit(rand)) {
                            case 0 -> {
                                System.out.println(monster.ruType + " промахивается\n");
                                isPlayerHit = !isPlayerHit;
                            }
                            case 1 -> {
                                Fight.printHealths(player, monster);
                                System.out.println("\n\n");
                                if (afterMonsterHit(rand)) {
                                    isPlayerHit = !isPlayerHit;
                                } else {
                                    return;
                                }
                            }
                            case 2 -> {
                                System.out.println(player.playerName + " не сумел устоять перед ударом но ему удалось скрыться\n\n\n\n\n\n\n\n");
                                isFighting = false;
                                player.lostInventory();
                                isWin = false;
                                player.restoreHp();
                                return;
                            }
                        }
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                    }
                }

                isFighting = false;
            } else {
                System.out.println(MESSAGES.ENTERS_10);
                System.out.println(player.playerName + " так бежал что из карманов всё повылетало...");
                System.out.println(MESSAGES.ENTERS_5 + " \n\n");
                player.lostInventory();
                isWin = false;
                return;
            }
        }
    }

    private boolean afterMonsterHit(Random rand) {
        while (true) {
            System.out.println("1. Продолжить бой");
            System.out.println("2. Выпить зелье и продолжить бой");
            System.out.println("3. Сбежать");
            int input = readUserInput(1, 3, 0);
            System.out.println("\n\n\n");

            switch (input) {
                case 1 -> {
                    return true;
                }
                case 2 -> {
                    System.out.println(MESSAGES.ENTERS_10);
                    drinkPotion();
                    System.out.println(MESSAGES.ENTERS_5);
                }
                case 3 -> {
                    System.out.println(player.playerName + " решил сбежать, но может это и к лучшему...");
                    player.lostInventory();
                    isFighting = false;
                    isWin = false;
                    isRunned = true;
                    return false;
                }
            }
        }
    }

    private void drinkPotion() {
        AtomicInteger f = new AtomicInteger(1);
        HashSet<ITEM_TYPE> itemSet = player.inventory.getAvailablePotions();
        if (itemSet.size() == 0) {
            System.out.println("Нету больше зелий...");
            return;
        }
        itemSet.forEach(item -> System.out.println(f.getAndIncrement() + ". " + item.ruName));
        int input2 = readUserInput(1, itemSet.size(), 0);
        if (input2 == 0) {
            return;
        }
        player.usePotion((ITEM_TYPE) itemSet.toArray()[input2 - 1]);
    }

    private int playerHit(Random rand) {
        int[] data = makeHit(monster, player, rand);

        if (data[0] == 1) {    // attacked

            System.out.print(player.playerName + " наносит" + (data[2] == 1 ? " сокрушительный" : "") + " удар ");
            System.out.println(monster.ruType + " в " + data[1] + " едениц");

            if (data[3] == 1) {
                System.out.println("И одерживает победу в этом бою\n");
                return 2;
            } else {
                return 1;
            }

        }
        return 0;
        // 0 - промахнулся
        // 1 - успешная атака
        // 2 - победа
    }

    private int monsterHit(Random rand) {

        int[] data = makeHit(player, monster, rand);

        if (data[0] == 1) {

            System.out.print(monster.ruType + " наносит" + (data[2] == 1 ? " сокрушительный" : "") + " удар ");
            System.out.println(player.playerName + " в " + data[1] + " едениц\n");

            if (data[3] == 1) {
                return 2;
            } else {
                return 1;
            }
        }
        return 0;
    }

    protected int[] makeHit(Entity defender, Entity attacker, Random random) {
        int[] damage = attacker.attack(random, attacker);
        if (defender.getHealth() <= damage[0]) {
            return new int[]{damage[0] == 0 ? 0 : 1, damage[0], damage[1], 1};
        } else {
            defender.setHealth(defender.getHealth() - damage[0]);
            return new int[]{damage[0] == 0 ? 0 : 1, damage[0], damage[1], 0};
        }
    }

    private void giveDrop(Player player, Monster monster) {
        player.addExp(monster.getExp());
        player.inventory.add(monster.dropItemType, monster.itemCount);
        System.out.println("Вы получили:\n  Опыт - " + monster.getExp() + "\n  " + monster.dropItemType.ruName + " -  " + monster.itemCount + "\n\n\n");
    }

}
