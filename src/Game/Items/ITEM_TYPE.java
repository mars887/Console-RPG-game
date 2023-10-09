package Game.Items;

public enum ITEM_TYPE {
    BONE("кость", 5, 10, 64,false),
    FABRIC("ткань", 6, 14, 64,false),
    DIAMOND("алмаз", 40, 90, 16,false),
    HEALING_POTION_30PE("зелье востановления +30%", 30, 50, 5,true),
    HEALING_POTION_70PE("зелье востановления +70%", 40, 90, 3,true),
    DEXTERITY_POTION("зелье ловкости", 35, 60, 3,true),
    PROTECTION_POTION("зелье защиты",45,100,2,true);

    public final String ruName;
    public final int costSell;
    public final int costBuy;
    public final int maxStackSize;
    public final boolean isPotion;

    ITEM_TYPE(String ruName, int costSell, int costBuy, int maxStackSize,boolean isPotion) {
        this.ruName = ruName;
        this.costSell = costSell;
        this.costBuy = costBuy;
        this.maxStackSize = maxStackSize;
        this.isPotion = isPotion;
    }

    public static String getTradeMessage(ITEM_TYPE type) {
        return type.ruName + " - " + type.costBuy + " за штуку";
    }
}
