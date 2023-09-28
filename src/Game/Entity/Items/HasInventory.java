package Game.Entity.Items;

public interface HasInventory {
    int add(ITEM_TYPE type, int quantity);
    int contains(ITEM_TYPE type);
    int remove(ITEM_TYPE type, int quantity);
}
