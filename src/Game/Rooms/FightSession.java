package Game.Rooms;

import Game.Entity.MONSTER_TYPE;
import Game.Entity.Monster;
import Game.Entity.Player;

import java.util.Random;

import static Game.Game.readUserInput;

public class FightSession extends Thread {

    public final Player player;
    public final Monster monster;

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
            System.out.println(" его здоровье - " + monster.getHealth());
            System.out.println(" а удары забирают по " + monster.getStrength() + " едениц здоровья");
            System.out.println("1. Начать бой\n2. Сбежать");
            if (readUserInput(1, 2) == 1) {
                System.out.println("ты типа выграл поэтому всё топай отсюда");
                isFighting = false;
            } else {
                player.lostInventory();
                isFighting = false;
            }
        }
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
                type.itemCount + random.nextInt(-1, 2)
        );
    }
}
