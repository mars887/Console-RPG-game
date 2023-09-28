package Game.Entity;

import Game.Entity.Items.Inventory;

public class Player extends Entity implements Fighter {

    protected int level;           // level       x > 0
    protected int money;           // money       x > 0

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

    public int getXpForNextLevel() { return (100 + this.level * 15) - exp; }

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

    @Override
    public int attack(Entity entity) {
        return 0;
    }
}
