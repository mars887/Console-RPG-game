package Entity;

public class Player extends Entity implements Fighter {

    protected int level;           // level       x > 0
    protected int money;           // money       x > 0

    // -------------------------------------------------------------------------------------    Constructors
    public Player(int maxHealth, int health, int strength, float dexterity, int level, int exp, int money) {
        super(maxHealth, health, strength, dexterity, exp);
        setLevel(level);
        expToLevel(false);
        setMoney(money);
    }

    // -------------------------------------------------------------------------------------    Setters

    public void setLevel(int level) {
        this.level = Math.max(level, 0);
    }

    public void setMoney(int money) {
        this.money = Math.max(money, 0);
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

    @Override
    public boolean attack(Entity entity) {
        return false;
    }
}
