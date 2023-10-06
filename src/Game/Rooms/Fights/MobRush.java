package Game.Rooms.Fights;

import Game.Entity.Monster;
import Game.Entity.Player;
import Game.Items.ITEM_TYPE;
import Game.MESSAGES;
import Game.TextWriter;

import java.util.HashMap;
import java.util.Map;

import static Game.Game.readUserInput;

public class MobRush extends Thread {
    private final Player player;

    private FightSession session;
    private int level = 0;
    private HashMap<ITEM_TYPE,Integer[]> collectedItems = new HashMap<>();
    private int collectedExp = 0;

    public MobRush(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        System.out.println("1. Начать\n2. Назад\n");
        if(readUserInput(1,2,0) == 1) {
            startRush();
        }
    }

    private void startRush() {

        while(true) {
            Monster monster = Fight.getRandomMonster(level);
            System.out.println("Ваш " + (level + 1) + " противник...");
            Fight.printStats(player,monster);
            System.out.println("1. Начать бой\n2. Уйти\n");
            if(readUserInput(1,2,0) == 1) {
                if(startFight(player,monster)) {
                    collectDropFrom(monster);
                    player.restoreHp();
                    level++;
                    continue;
                } else {
                    return;
                }
            } else {
                System.out.println(MESSAGES.ENTERS_5);
                collectAllDrops(player);
                System.out.println("\n\n");
                return;
            }
        }
    }

    private void collectDropFrom(Monster monster) {
        if(collectedItems.containsKey(monster.dropItemType)) {
            collectedItems.get(monster.dropItemType)[0] += monster.itemCount;
        } else {
            collectedItems.put(monster.dropItemType,new Integer[]{monster.itemCount});
        }
        collectedExp += monster.getExp();
    }

    private boolean startFight(Player player, Monster monster) {
        session = new FightSession(player,monster);
        session.startQuestion = false;
        session.printStartStats = false;
        session.giveDrop = false;
        session.start();
        try {
            session.join();
        } catch (InterruptedException e) {

        }
        return session.isWin;
    }

    private void collectAllDrops(Player player) {
        System.out.println(player.playerName + " смог унести с собой");
        for(Map.Entry<ITEM_TYPE, Integer[]> entry : collectedItems.entrySet()) {
            TextWriter.printWithRightBorder(entry.getKey().ruName,16);
            System.out.println(" | " + player.inventory.add(entry.getKey(), entry.getValue()[0]));
        }
        System.out.println("И " + collectedExp + " опыта");
        player.addExp(collectedExp);
    }
}
