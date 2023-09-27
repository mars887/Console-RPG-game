package Entity.Items;

import java.util.ArrayList;

public class Inventory implements HasInventory {
    public static final int INVENTORY_SIZE = 7;
    private ArrayList<ItemStack> items = new ArrayList<>();

    @Override
    public int add(ItemStack item) {
        int count = item.getQuantity();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).equalsItem(item)) {
                count -= items.get(i).addQuantity(count);
            }
            if (count == 0) {
                return item.getQuantity();
            }
        }
        if (count > 0 && items.size() < INVENTORY_SIZE) {
            while (items.size() < INVENTORY_SIZE && count > 0) {
                items.add(new ItemStack(count, item.itemType));
                count -= items.get(items.size() - 1).getQuantity();
            }
        }
        if (count == 0) return item.getQuantity();
        else return item.getQuantity() - count;
    }

    @Override
    public int contains(ITEM_TYPE type) {
        int count = 0;
        for(ItemStack item : items) {
            if(item.itemType == type) count += item.getQuantity();
        }
        return count;
    }

    @Override
    public boolean remove(ItemStack item) {
        return false;
    }

    @Override
    public boolean getAndRemove(ItemStack item) {
        return false;
    }
}
