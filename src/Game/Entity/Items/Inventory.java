package Game.Entity.Items;

import java.util.ArrayList;
import java.util.stream.Stream;

public class Inventory implements HasInventory {
    public static final int INVENTORY_SIZE = 7;                 // Inventory size
    private ArrayList<ItemStack> items = new ArrayList<>();     // List of items in inventory

    @Override
    public int add(ITEM_TYPE type, int quantity) {
        int added = 0;
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).equalsType(type)) {
                added += items.get(i).addQuantity(quantity);
            }
            if (added == quantity) return quantity;
        }
        while (INVENTORY_SIZE - items.size() > 0 && added < quantity) {
            items.add(new ItemStack(quantity - added, type));
            added += items.get(items.size() - 1).getQuantity();
            if (added == quantity) return quantity;
        }
        return added;
    }

    @Override
    public int contains(ITEM_TYPE type) {
        int count = 0;
        for (ItemStack item : items) {
            if (item.itemType == type) count += item.getQuantity();
        }
        return count;
    }

    @Override
    public int remove(ITEM_TYPE type, int quantity) {
        int count = quantity;
        for (int i = items.size() - 1; i >= 0; i--) {
            ItemStack item = items.get(i);
            if (item.itemType == type) {
                if (item.getQuantity() > quantity) {
                    item.setQuantity(item.getQuantity() - quantity);
                    return quantity;
                } else if (item.getQuantity() == quantity) {
                    items.remove(i);
                    return quantity;
                } else {
                    quantity -= item.getQuantity();
                    items.remove(i);
                    if (quantity == 0) return quantity;
                }
            }
        }
        return count - quantity;
    }
    public int getFreeSlotsFor(ITEM_TYPE item) {
        int count = 0;
        for(int i = 0;i < items.size();i++) {
            if(items.get(i).equalsType(item)) count += items.get(i).MAX_STACK_SIZE - items.get(i).getQuantity();
        }
        if(INVENTORY_SIZE - items.size() > 0) count += item.maxStackSize * (INVENTORY_SIZE - items.size());
        return count;
    }

    public Stream<ItemStack> getStream() {
        return items.stream();
    }
}
