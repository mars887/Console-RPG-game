package Game.Entity;

import Game.Items.ITEM_TYPE;
import Game.Items.Inventory;
import org.w3c.dom.ls.LSOutput;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Player extends Entity {

    protected int money;           // money       x > 0
    public String playerName;

    public final Inventory inventory;

    // -------------------------------------------------------------------------------------    Constructors

    public Player(int maxHealth, int health, int strength, float dexterity, int level, int exp, int money) {
        super(maxHealth, health, strength, dexterity, exp);
        setLevel(level);
        expToLevel(false);
        setMoney(money);
        inventory = new Inventory();
    }

    // -------------------------------------------------------------------------------------    Setters and Getters

    public void setPlayerName(String name) {
        playerName = name;
    }

    public void setLevel(int level) {
        this.level = Math.max(level, 0);
    }

    public void setMoney(int money) {
        this.money = Math.max(money, 0);
    }

    public int getLevel() {
        return level;
    }

    public int getMoney() {
        return money;
    }

    public int getXpForNextLevel() {
        return (100 + this.level * 15) - exp;
    }

    // -------------------------------------------------------------------------------------    Functions

    private void expToLevel(boolean print) {
        while (exp > 0) {
            if (100 + this.level * 15 <= exp) {
                level++;
                exp -= 100 + this.level * 15;
                if (print) System.out.println("Level Up to " + level + " !");
            } else {
                return;
            }
        }
    }

    public void addExp(int quantity) {
        if (quantity > 0) {
            exp += quantity;
            expToLevel(true);
        }
    }


    public void lostInventory() {
        HashMap<ITEM_TYPE, Integer> map = inventory.getHashMap();
        for (Map.Entry<ITEM_TYPE, Integer> entry : map.entrySet()) {
            if (entry.getValue() > 2) {
                inventory.remove(entry.getKey(), (int) (entry.getValue() * new Random().nextFloat(0.2f, 0.4f)));
            }
        }
    }

    public void usePotion(ITEM_TYPE item) {
        switch (item) {
            case DEXTERITY_POTION -> {
                new Thread(() -> {
                    float lastVal = dexterity;
                    dexterity = (float) Math.sqrt(dexterity);
                    try {
                        Thread.sleep(60000);
                    } catch (InterruptedException e) {
                    }
                    dexterity = lastVal;
                }).start();
                System.out.println("Зелье будет действовать ещё 1 минуту");
            }
            case HEALING_POTION_15HP -> System.out.println("Терерь у " + playerName + (health = Math.min(health + 15,maxHealth)) + " здоровья");
            case HEALING_POTION_40HP -> System.out.println("Терерь у " + playerName + (health = Math.min(health + 40,maxHealth)) + " здоровья");
        }
    }

    public void restoreHp() {
        health = maxHealth;
    }
}
