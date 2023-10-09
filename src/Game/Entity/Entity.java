package Game.Entity;

public abstract class Entity implements Fighter {

    protected int maxHealth;        // max health  x > 0
    protected int health;           // health      x > 0 <= maxHealth
    protected int strength;         // strength    x > 0
    protected int protection = 0;   // protection  0 <= x < 100 (int percent)
    protected Double dexterity;     // dexterity   0.0 < x < 1.0
    protected int level;            // level       x >= 0
    protected int exp;              // exp         x > 0

    // -------------------------------------------------------------------------------------    Constructors
    public Entity(int maxHealth, int health, int strength, double dexterity, int exp) {
        this.maxHealth = Math.max(maxHealth, 1);
        setHealth(health);
        setStrength(strength);
        setDexterity(dexterity);
        setExp(exp);
    }

    public Entity(int maxHealth, int strength, double dexterity, int exp) {
        this(maxHealth, maxHealth, strength, dexterity, exp);
    }

    // -------------------------------------------------------------------------------------    Setters

    public void setHealth(int health) {
        this.health = Math.min(health, maxHealth) < 0 ? 0 : health;
    }

    public void setStrength(int strength) {
        this.strength = Math.max(strength, 1);
    }

    public void setDexterity(double dexterity) {
        this.dexterity = Math.max(Math.min(dexterity, 1), 0);
    }

    public void setExp(int exp) {
        this.exp = Math.max(exp, 0);
    }

    public void setProtection(int protection) {
        this.protection = protection;
    }

    public boolean setDamage(int damage) {
        damage = (int) (damage / 100.0 * (100 - protection));

        if (health - damage > 0) {
            health -= damage;
            return false;
        } else return true;
    }

    public int getDamageWithProtection(int damage) {
        return (int) (damage / 100.0 * (100 - protection));
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getHealth() {
        return health;
    }

    public int getStrength() {
        return (strength);
    }

    public double getDexterity() {
        return dexterity;
    }

    public int getProtection() {
        return protection;
    }

    public int getExp() {
        return exp;
    }

}
