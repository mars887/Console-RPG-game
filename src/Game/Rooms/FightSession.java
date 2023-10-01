package Game.Rooms;

import Game.Entity.Entity;
import Game.Entity.MONSTER_TYPE;
import Game.Entity.Monster;
import Game.Entity.Player;
import Game.Items.ITEM_TYPE;
import org.w3c.dom.ls.LSOutput;

import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import static Game.Game.readUserInput;

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
            System.out.println("Ваш враг - " + monster.ruType);
            System.out.println(" здоровье - " + monster.getHealth());
            System.out.println(" сила -  " + monster.getStrength());
            System.out.println("\n1. Начать бой\n2. Сбежать");
            if (readUserInput(1, 2) == 1) { //---------------------------------
                Random rand = new Random();

                boolean isPlayerHit = rand.nextBoolean();
                System.out.println("\n\n\nПервый удар наносит " + (isPlayerHit ? player.playerName : monster.ruType));

                while (true) {
                    System.out.println(monster.getHealth() + "      " + player.getHealth());
                    if (isPlayerHit) {
                        int[] data = makeHit(monster, player, rand);
                        System.out.print("Отважный " + player.playerName + " наносит" + (data[1] == 1 ? " сокрушительный" : "") + " удар ");
                        System.out.println(monster.ruType + " в " + data[0] + " едениц");
                        if (data[2] == 1) {
                            System.out.println("Одерживая победу в этом бою");
                            winBattle(player, monster);
                            isFighting = isFindNextFight();
                            if (!isFighting) break;
                            monster = getRandomMonster(player.getLevel());
                        } else {
                            System.out.println("\nНо " + monster.ruType + " устоял на ногах");
                            System.out.println("И теперь удар наносит уже он...\n\n\n");
                            isPlayerHit = !isPlayerHit;
                            continue;
                        }
                    } else {
                        int[] data = makeHit(player, monster, rand);
                        System.out.print(monster.ruType + " наносит" + (data[1] == 1 ? " сокрушительный" : "") + " удар ");
                        System.out.println(player.playerName + " в " + data[0] + " едениц\n");
                        if (data[2] == 1) {
                            System.out.println(player.playerName + " не сумел устоять перед ударом но ему удалось скрыться\n\n\n\n\n");
                            isFighting = false;
                            player.lostInventory();
                            isWin = false;
                            break;
                        } else {
                            System.out.println("\n\nНо " + player.playerName + " смог устоять на ногах");
                            System.out.println("И вдруг у него возникли некоторые мысли...");
                            boolean flag = true;
                            while (flag) {
                                System.out.println("1. Продолжить бой");
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
            return new int[]{damage[0], damage[1], 1};
        } else {
            defender.setHealth(defender.getHealth() - damage[0]);
            return new int[]{damage[0], damage[1], 0};
        }
    }

    private void winBattle(Player player, Monster monster) {
        player.addExp(monster.getExp());
        player.inventory.add(monster.dropItemType, monster.itemCount);
        System.out.println("Вы получили:\n  Опыт - " + monster.getExp() + "\n  " + monster.dropItemType.ruName + " -  " + monster.itemCount);
    }

    public static Monster getRandomMonster(int playerLevel) {
        Random random = new Random();
        MONSTER_TYPE type = MONSTER_TYPE.values()[random.nextInt(0, 3)];
        return new Monster(
                type.ruType,
                type.hp + (type.hpPerLvl + random.nextInt(-1, 2)) * playerLevel,
                type.strength + (type.strengthPerLvl + random.nextInt(-1, 2)) * playerLevel,
                type.dexterity,
                type.playerDexterityChange,
                type.exp + (type.expPerLvl + random.nextInt(-1, 2)) * playerLevel,
                type.dropItemType,
                type.itemCount + random.nextInt(-1, 2),
                playerLevel
        );
    }

    private boolean isFindNextFight() {
        System.out.println("Теперь перед ним выбор\n1. Вернутся в город\n2. Найти нового противника\n");
        int input = readUserInput(1, 2);
        if (input == 1) {
            System.out.println("Возвращаемся в город...");
            return false;
        } else {
            System.out.println("Продолжаем бой...");
            return true;
        }
    }
}
