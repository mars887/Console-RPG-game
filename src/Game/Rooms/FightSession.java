package Game.Rooms;

import Game.Entity.Entity;
import Game.Entity.MONSTER_TYPE;
import Game.Entity.Monster;
import Game.Entity.Player;
import Game.Game;
import Game.Items.ITEM_TYPE;
import Game.MESSAGES;

import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import static Game.Game.*;

public class FightSession extends Thread {

    public final Player player;
    public Monster monster;

    public boolean isWin = true;

    private final float playerSavedDexterity;

    public FightSession(Player player) {
        this.player = player;
        playerSavedDexterity = player.getDexterity();
        this.monster = getRandomMonster(player.getLevel() - 1);
        player.setDexterity(playerSavedDexterity * monster.playerDexterityChange);
    }

    @Override
    public void run() {
        boolean isFighting = true;

        while (isFighting) {
            printStats(player,monster);
            System.out.println("\n1. Начать бой\n2. Сбежать");
            if (readUserInput(1, 2) == 1) { //---------------------------------
                Random rand = new Random();

                boolean isPlayerHit = rand.nextBoolean();
                System.out.println("\n\n\nПервый удар наносит " + (isPlayerHit ? player.playerName : monster.ruType) + "\n");

                boolean isRunned = false;

                while (!isRunned) {
                    if (isPlayerHit) {
                        int[] data = makeHit(monster, player, rand);
                        System.out.print("\n\n" + player.playerName + " наносит" + (data[2] == 1 ? " сокрушительный" : "") + " удар ");
                        System.out.println(monster.ruType + " в " + data[1] + " едениц");
                        if (data[3] == 1) {
                            System.out.println("И одерживает победу в этом бою\n");
                            winBattle(player, monster);
                            isFighting = isFindNextFight();
                            if (!isFighting) break;
                            monster = getRandomMonster(player.getLevel());
                        } else {
                            printHealths(player,monster);
                            System.out.println("\n\n\n");
                            isPlayerHit = !isPlayerHit;
                        }
                    } else {
                        int[] data = makeHit(player, monster, rand);
                        System.out.print(monster.ruType + " наносит" + (data[2] == 1 ? " сокрушительный" : "") + " удар ");
                        System.out.println(player.playerName + " в " + data[1] + " едениц\n");
                        if (data[3] == 1) {
                            System.out.println(player.playerName + " не сумел устоять перед ударом но ему удалось скрыться\n\n\n\n\n");
                            isFighting = false;
                            player.lostInventory();
                            isWin = false;
                            break;
                        } else {
                            printHealths(player,monster);
                            boolean flag = true;
                            while (flag) {
                                System.out.println("\n\n1. Продолжить бой");
                                System.out.println("2. Выпить зелье и продолжить бой");
                                System.out.println("3. Сбежать");
                                int input = readUserInput(1, 3);
                                System.out.println("\n\n\n");
                                switch (input) {
                                    case 1 -> {
                                        isPlayerHit = !isPlayerHit;
                                        flag = false;
                                        break;
                                    }
                                    case 2 -> {
                                        AtomicInteger f = new AtomicInteger(1);
                                        HashSet<ITEM_TYPE> itemSet = player.inventory.getAvailablePotions();
                                        itemSet.stream().forEach(item -> {
                                            System.out.println(f.getAndIncrement() + ". " + item.ruName);
                                        });
                                        System.out.println((itemSet.size() + 1) + ". Назад");
                                        int input2 = readUserInput(1, itemSet.size() + 1);
                                        if (input2 == itemSet.size() + 1) {
                                            break;
                                        }
                                        player.usePotion((ITEM_TYPE) itemSet.toArray()[input2 - 1]);
                                        flag = false;
                                        isPlayerHit = !isPlayerHit;
                                        break;
                                    }
                                    case 3 -> {
                                        System.out.println(player.playerName + " решил сбежать, но может это и к лучшему...");
                                        player.lostInventory();
                                        isFighting = false;
                                        flag = false;
                                        isWin = false;
                                        isRunned = true;
                                    }
                                }
                            }
                        }
                    }
                }   //----------------------------------------------------------------

                isFighting = false;
            } else {
                System.out.println(player.playerName + " так бежал что из карманов всё повылетало...");
                player.lostInventory();
                isFighting = false;
            }
        }
        player.setDexterity(playerSavedDexterity);
    }

    protected int[] makeHit(Entity defender, Entity attacker, Random random) {
        int[] damage = attacker.attack(random, attacker);
        if (defender.getHealth() <= damage[0]) {
            return new int[]{damage[0] == 0 ? 0 : 1,damage[0], damage[1], 1};
        } else {
            defender.setHealth(defender.getHealth() - damage[0]);
            return new int[]{damage[0] == 0 ? 0 : 1,damage[0], damage[1], 0};
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
                type.hp + (type.hpPerLvl + random.nextInt(-1, 2)) * playerLevel + playerLevel,
                type.strength + (type.strengthPerLvl + random.nextInt(-1, 2)) * playerLevel + playerLevel,
                type.dexterity,
                type.playerDexterityChange,
                type.exp + (type.expPerLvl + random.nextInt(-1, 2)) * playerLevel,
                type.dropItemType,
                type.itemCount + random.nextInt(-1, 2),
                playerLevel
        );
    }

    private boolean isFindNextFight() {
        System.out.println("Теперь перед ним выбор\n1. Вернутся в город\n2. Найти нового противника\n3. Зайти к целительнице\n");
        int input = readUserInput(1, 3);

        switch (input) {
            case 1 -> {
                System.out.println("Возвращаемся в город...");
                return false;
            }
            case 2 -> {
                System.out.println("Продолжаем бой...");
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
    private static void printStats(Player player,Monster monster) {
        int border = 30;
        printHealths(player, monster);
        System.out.print("Сила      -  ");
        printComparisonWithBorder(player.getStrength(), border, monster.getStrength());
        System.out.println();
    }
    private static void printHealths(Player player,Monster monster) {
        int border = 30;
        System.out.print("Участник  -  ");
        printComparisonWithBorder(player.playerName, border,monster.ruType);
        System.out.print("\nЗдоровье  -  ");
        printComparisonWithBorder(player.getHealth(), border, monster.getHealth());
        System.out.println();
    }
}
