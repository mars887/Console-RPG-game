package Game.Entity;

import Game.Items.ITEM_TYPE;

public enum MONSTER_TYPE {
    ZOMBIE("зомби", 100, 13, 0.75f, 1, ITEM_TYPE.FABRIC, 8, 1, 20, 3),
    SKELETON("скелет", 70, 11, 0.9f, 0.9f, ITEM_TYPE.BONE, 8, 1.2f, 25, 4),
    GOBLIN("гоблин", 120, 10, 0.8f, 0.95f, ITEM_TYPE.DIAMOND, 3, 1.5f, 30, 5);

    public final String ruType;               // russian name
    public final int health;                  // preFunction health
    public final int healthFunc;              // Function health const
    public final double dexterity;            // standard dexterity
    public final float playerDexterityChange; // on start fight:  player.dexterity *= this
    public final ITEM_TYPE dropItemType;      // type of drop
    public final int itemCount;               // number of drops items
    public final float expConst;              // pre expFunc value
    public final int strength;                // preFunction strength
    public final int strengthFunc;            // Function strength const

    MONSTER_TYPE(String ruType, int health, int healthFunc, double dexterity, float playerDexterityChange
            , ITEM_TYPE dropItemType, int itemCount, float expConst, int strength, int strengthFunc) {
        this.ruType = ruType;
        this.health = health;
        this.healthFunc = healthFunc;
        this.dexterity = dexterity;
        this.playerDexterityChange = playerDexterityChange;
        this.dropItemType = dropItemType;
        this.itemCount = itemCount;
        this.strength = strength;
        this.strengthFunc = strengthFunc;
        this.expConst = expConst;
    }
}
