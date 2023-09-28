package Game.Entity.Items;

public enum ITEM_TYPE {
    BONE("кость", 5, 10, 64),
    FABRIC("ткань", 7, 14, 64),
    DIAMOND("алмаз", 100, 160, 16),
    HEALING_POTION_15HP("зелье востановления 15НР", 30, 50, 3),
    HEALING_POTION_40HP("зелье востановления 40НР", 80, 240, 2),
    DEXTERITY_POTION("зелье ловкости", 24, 40, 3);

    public final String ruName;
    public final int costSell;
    public final int costBuy;
    public final int maxStackSize;

    ITEM_TYPE(String ruName, int costSell, int costBuy, int maxStackSize) {
        this.ruName = ruName;
        this.costSell = costSell;
        this.costBuy = costBuy;
        this.maxStackSize = maxStackSize;
    }

    public String getRuName() {
        return ruName;
    }

    public int getCostSell() {
        return costSell;
    }

    public int getCostBuy() {
        return costBuy;
    }

    public int getMaxStackSize() {
        return maxStackSize;
    }

    public static String getTradeMessage(ITEM_TYPE type) {
        return type.ruName + " - " + type.costSell + " за штуку";
    }
}
