package Game.Entity;

public abstract class Entity implements Fighter {

    protected final int maxHealth; // max health  x > 0
    protected int health;          // health      x > 0 <= maxHealth
    protected int strength;        // strength    x > 0
    protected float dexterity;     // dexterity   0.0 > x < 1.0
    protected int level;           // level       x >= 0
    protected int exp;             // exp         x > 0

    // -------------------------------------------------------------------------------------    Constructors
    public Entity(int maxHealth, int health, int strength, float dexterity, int exp) {
        this.maxHealth = Math.max(maxHealth, 1);
        setHealth(health);
        setStrength(strength);
        setDexterity(dexterity);
        setExp(exp);
    }

    public Entity(int maxHealth, int strength, float dexterity, int exp) {
        this(maxHealth, maxHealth, strength, dexterity, 0);
    }

    // -------------------------------------------------------------------------------------    Setters

    public void setHealth(int health) {
        this.health = Math.min(health, maxHealth) < 0 ? 0 : health;
    }

    public void setStrength(int strength) {
        this.strength = Math.max(strength, 1);
    }

    public void setDexterity(float dexterity) {
        this.dexterity = Math.max(Math.min(dexterity, 1), 0);
    }

    public void setExp(int exp) {
        this.exp = Math.max(exp, 0);
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getHealth() {
        return health;
    }

    public int getStrength() {
        return strength;
    }

    public float getDexterity() {
        return dexterity;
    }

    public int getExp() {
        return exp;
    }

}
