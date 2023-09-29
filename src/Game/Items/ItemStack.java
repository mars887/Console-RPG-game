package Game.Items;

public class ItemStack {
    public final int MAX_STACK_SIZE;
    public final ITEM_TYPE itemType;
    private int quantity;

    public ItemStack(int quantity, ITEM_TYPE itemType) {
        this.itemType = itemType;
        this.MAX_STACK_SIZE = itemType.maxStackSize;
        this.quantity = Math.max(1, Math.min(quantity, MAX_STACK_SIZE));
    }

    public int addQuantity(int i) {
        int freeSpace = MAX_STACK_SIZE - quantity;
        if (freeSpace <= 0) return 0;
        if (i > freeSpace) {
            quantity = MAX_STACK_SIZE;
            return freeSpace;
        } else {
            quantity += i;
            return i;
        }
    }

    public int getQuantity() {
        return quantity;
    }

    public int setQuantity(int quantity) {
        this.quantity = Math.max(1, Math.min(quantity, MAX_STACK_SIZE));
        return this.quantity;
    }

    public void setQuantityWithoutCheck(int quantity) {
        this.quantity = quantity;
    }

    public boolean equalsItem(ItemStack item) {
        return this.itemType == item.itemType;
    }

    public boolean equalsType(ITEM_TYPE type) {
        return this.itemType == type;
    }
}
