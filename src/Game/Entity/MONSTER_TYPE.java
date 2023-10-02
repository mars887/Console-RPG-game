package Game.Entity;

import Game.Items.ITEM_TYPE;

public enum MONSTER_TYPE {
    ZOMBIE("зомби", 100, 10, 0.7f, 1, ITEM_TYPE.FABRIC, 8, 10, 5, 20, 5),
    SKELETON("скелет", 70, 10, 0.9f, 0.9f, ITEM_TYPE.BONE, 8, 15, 5, 25, 5),
    GOBLIN("гоблин", 120, 10, 0.8f, 0.95f, ITEM_TYPE.DIAMOND, 3, 25, 7, 30, 5);

    public final String ruType;
    public final int hp;
    public final int hpPerLvl;
    public final float dexterity;
    public final float playerDexterityChange;
    public final ITEM_TYPE dropItemType;
    public final int itemCount;
    public final int exp;
    public final int expPerLvl;
    public final int strength;
    public final int strengthPerLvl;

    MONSTER_TYPE(String ruType, int hp, int hpPerLvl, float dexterity, float playerDexterityChange
            , ITEM_TYPE dropItemType, int itemCount, int exp, int expPerLvl, int strength, int strengthPerLvl) {
        this.ruType = ruType;
        this.hp = hp;
        this.hpPerLvl = hpPerLvl;
        this.dexterity = dexterity;
        this.playerDexterityChange = playerDexterityChange;
        this.dropItemType = dropItemType;
        this.itemCount = itemCount;
        this.exp = exp;
        this.expPerLvl = expPerLvl;
        this.strength = strength;
        this.strengthPerLvl = strengthPerLvl;
    }
}
