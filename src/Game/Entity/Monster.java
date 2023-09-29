package Game.Entity;

import Game.Items.ITEM_TYPE;

public class Monster extends Entity implements Fighter {

    public final String ruType;
    public final float playerDexterityChange;
    public final ITEM_TYPE dropItemType;
    public final int itemCount;


    public Monster(String ruType, int maxHealth, int strength, float dexterity,float playerDexterityChange, int exp,ITEM_TYPE dropItemType, int itemCount) {
        super(maxHealth, strength, dexterity, exp);
        this.itemCount = itemCount;
        this.dropItemType = dropItemType;
        this.ruType = ruType;
        this.playerDexterityChange = playerDexterityChange;
    }

    @Override
    public int attack(Entity entity) {
        return 0;
    }
}
