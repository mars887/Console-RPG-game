package Game.Entity;

public class Monster extends Entity implements Fighter {

    public Monster(int maxHealth, int strength, float dexterity, int exp) {
        super(maxHealth, strength, dexterity, exp);
    }

    @Override
    public int attack(Entity entity) {
        return 0;
    }
}
