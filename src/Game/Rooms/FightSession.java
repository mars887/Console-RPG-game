package Game.Rooms;

import Game.Entity.*;
import Game.Items.ITEM_TYPE;
import Game.*;

import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import static Game.Game.readUserInput;

public class FightSession extends Thread {

    public final Player player;
    public Monster monster;

    public boolean isWin = true;

    public boolean continueFight = false;

    private boolean isFighting = true; // 1 level
    private boolean isRunned = false;  // 2 level
    private boolean isPlayerHit;       // val 1

    public FightSession(Player player) {
        this.player = player;
        this.monster = getRandomMonster(player.getLevel() - 1);
    }

    @Override
    public void run() {

        while (isFighting) {
            printStats(player, monster);
            System.out.println("\n1. Начать бой\n2. Сбежать");
            if (readUserInput(1, 2, 0) == 1) { //---------------------------------
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
                                printHealths(player, monster);
                                System.out.println("\n\n\n");
                                isPlayerHit = !isPlayerHit;
                            }
                            case 2 -> {
                                winBattle(player, monster);
                                isFighting = isFindNextFight();
                                if (isFighting) {
                                    System.out.println("Ищем нового противника...");
                                    continueFight = true;
                                    return;
                                } else {
                                    return;
                                }

                            }
                        }

                    } else {

                        switch (monsterHit(rand)) {
                            case 0 -> {
                                System.out.println(monster.ruType + " промахивается\n");
                                isPlayerHit = !isPlayerHit;
                            }
                            case 1 -> {
                                printHealths(player,monster);
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
                                return;
                            }
                        }
                    }
                }   //----------------------------------------------------------------

                isFighting = false;
            } else {
                System.out.println(MESSAGES.ENTERS_10);
                System.out.println(player.playerName + " так бежал что из карманов всё повылетало...");
                System.out.println(MESSAGES.ENTERS_5 + " \n\n");
                player.lostInventory();
                isFighting = false;
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
        itemSet.forEach(item -> System.out.println(f.getAndIncrement() + ". " + item.ruName));
        System.out.println((itemSet.size() + 1) + ". Назад");
        int input2 = readUserInput(1, itemSet.size() + 1, 0);
        if (input2 == itemSet.size() + 1) {
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

    private void winBattle(Player player, Monster monster) {
        player.addExp(monster.getExp());
        player.inventory.add(monster.dropItemType, monster.itemCount);
        System.out.println("Вы получили:\n  Опыт - " + monster.getExp() + "\n  " + monster.dropItemType.ruName + " -  " + monster.itemCount + "\n\n\n");
    }

    public static Monster getRandomMonster(int playerLevel) {
        Random random = new Random();
        MONSTER_TYPE type = MONSTER_TYPE.values()[random.nextInt(0, 3)];
        return new Monster(
                type.ruType,
                DataSupplier.HealthFunction.apply(playerLevel,type.healthFunc) + (type.health - 100),
                DataSupplier.StrengthFunction.apply(playerLevel,type.strengthFunc) + (type.strength - 20),
                type.dexterity,
                type.playerDexterityChange,
                DataSupplier.ExpToDropFunction.apply(playerLevel,type.expConst),
                type.dropItemType,
                type.itemCount + random.nextInt(-1, 2),
                playerLevel
        );
    }

    private boolean isFindNextFight() {
        System.out.println("Теперь перед ним выбор\n1. Вернутся в город\n2. Найти нового противника\n3. Зайти к целительнице\n");
        int input = readUserInput(1, 3, 0);

        switch (input) {
            case 1 -> {
                System.out.println(MESSAGES.ENTERS_10);
                System.out.println("Возвращаемся в город...\n\n\n\n");
                return false;
            }
            case 2 -> {
                return true;
            }
            case 3 -> {
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
                return isFindNextFight();
            }
        }
        return false;
    }

    private static void printStats(Player player, Monster monster) {
        int border = 30;
        printHealths(player, monster);
        System.out.print("Сила      -  ");
        TextWriter.printComparisonWithBorder(player.getStrength(), border, monster.getStrength());
        System.out.println();
    }

    private static void printHealths(Player player, Monster monster) {
        int border = 30;
        System.out.print("\nУчастник  -  ");
        TextWriter.printComparisonWithBorder(player.playerName, border, monster.ruType);
        System.out.print("\nЗдоровье  -  ");
        TextWriter.printComparisonWithBorder(player.getHealth(), border, monster.getHealth());
        System.out.println();
    }
}
