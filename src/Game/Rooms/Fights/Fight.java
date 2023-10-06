package Game.Rooms.Fights;

import Game.Entity.DataSupplier;
import Game.Entity.MONSTER_TYPE;
import Game.Entity.Monster;
import Game.Entity.Player;
import Game.*;

import java.util.Random;

import static Game.Game.readUserInput;

public class Fight {

    public static Monster getRandomMonster(int level) {
        Random random = new Random();
        MONSTER_TYPE type = MONSTER_TYPE.values()[random.nextInt(0, 3)];
        return new Monster(
                type.ruType,
                DataSupplier.HealthFunction.apply(level,type.healthFunc) + (type.health - 100),
                DataSupplier.StrengthFunction.apply(level,type.strengthFunc) + (type.strength - 20),
                type.dexterity,
                type.playerDexterityChange,
                DataSupplier.ExpToDropFunction.apply(level,type.expConst),
                type.dropItemType,
                type.itemCount + random.nextInt(-1, 2),
                level
        );
    }

    public static boolean isFindNextFight(Player player) {
        System.out.println("Теперь перед ним выбор\n1. Вернутся в город\n2. Найти нового противника\n3. Зайти к целительнице\n");
        int input = readUserInput(1, 3, 0);

        switch (input) {
            case 0,1 -> {
                System.out.println(MESSAGES.ENTERS_10);
                System.out.println("Возвращаемся в город...\n\n\n\n\n\n");
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
                return isFindNextFight(player);
            }
        }
        return false;
    }

    static void printStats(Player player, Monster monster) {
        int border = 30;
        printHealths(player, monster);
        System.out.print("Сила      -  ");
        TextWriter.printComparisonWithBorder(player.getStrength(), border, monster.getStrength());
        System.out.println();
    }

    static void printHealths(Player player, Monster monster) {
        int border = 30;
        System.out.print("\nУчастник  -  ");
        TextWriter.printComparisonWithBorder(player.playerName, border, monster.ruType);
        System.out.print("\nЗдоровье  -  ");
        TextWriter.printComparisonWithBorder(player.getHealth(), border, monster.getHealth());
        System.out.println();
    }
}
