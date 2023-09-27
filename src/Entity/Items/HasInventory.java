package Entity.Items;

public interface HasInventory {
    int add(ItemStack item);
    int contains(ITEM_TYPE type);
    boolean remove(ItemStack item);
    boolean getAndRemove(ItemStack item);
}
