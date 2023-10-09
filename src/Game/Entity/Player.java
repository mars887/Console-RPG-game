package Game.Entity;

import Game.Items.ITEM_TYPE;
import Game.Items.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Player extends Entity {

    protected int money;           // money       x > 0
    public String playerName;

    public final Inventory inventory;

    private boolean dexterityPotionAvailable = false;

    // -------------------------------------------------------------------------------------    Constructors

    public Player(int maxHealth, int health, int strength, Double dexterity, int level, int exp, int money) {
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
        return DataSupplier.ExpToNextLevelFunction.apply(level,1) - exp;
    }

    public double getDexterity() {
        return dexterityPotionAvailable ? Math.sqrt(dexterity) : dexterity;
    }


    // -------------------------------------------------------------------------------------    Functions

    private void expToLevel(boolean print) {
        while (exp > 0) {
            if (DataSupplier.ExpToNextLevelFunction.apply(level,1) <= exp) {
                exp -= DataSupplier.ExpToNextLevelFunction.apply(level,1);
                maxHealth = DataSupplier.HealthFunction.apply(level,13);
                strength = DataSupplier.StrengthFunction.apply(level,4);
                dexterity = DataSupplier.DexterityFunction.apply(level,0.7f);
                if (print) System.out.println("Level Up to " + ++level + " !");
                if(level % 10 == 0 && inventory.addInventorySlot()) System.out.println("Доступен новый слот ивентаря");
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


    public void lostInventory(float num) {
        HashMap<ITEM_TYPE, Integer> map = inventory.getHashMap(true);
        for (Map.Entry<ITEM_TYPE, Integer> entry : map.entrySet()) {
            if (entry.getValue() > 2) {
                inventory.remove(entry.getKey(), (int) (entry.getValue() * new Random().nextFloat(num - 0.1f, num + 0.1f)));
            }
        }
    }

    public void usePotion(ITEM_TYPE item) {
        System.out.println();
        switch (item) {
            case DEXTERITY_POTION -> {
                new Thread(() -> {
                    dexterityPotionAvailable = true;
                    try {
                        Thread.sleep(120000);
                    } catch (InterruptedException e) {
                    }
                    dexterityPotionAvailable = false;
                }).start();
                System.out.println("Зелье точности будет действовать ещё 2 минуты");
            }
            case PROTECTION_POTION -> {
                new Thread(() -> {
                    protection = 30;
                    try {
                        Thread.sleep(120000);
                    } catch (InterruptedException e) {
                    }
                    protection = 0;
                }).start();
                System.out.println("Зелье защиты будет действовать ещё 2 минуты");
            }
            case HEALING_POTION_30PE ->
                    System.out.println("Терерь у " + playerName + " " + (health = Math.min(health + (int)(maxHealth * 0.30), maxHealth)) + " здоровья");
            case HEALING_POTION_70PE ->
                    System.out.println("Терерь у " + playerName + " " + (health = Math.min(health + (int)(maxHealth * 0.70), maxHealth)) + " здоровья");
        }
        inventory.remove(item, 1);
    }

    public void restoreHp() {
        health = maxHealth;
    }
}
