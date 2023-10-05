package Game.Items;

import Game.Entity.Player;
import Game.TextWriter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static Game.TextWriter.printWithRightBorder;

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
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).equalsType(item)) count += items.get(i).MAX_STACK_SIZE - items.get(i).getQuantity();
        }
        if (INVENTORY_SIZE - items.size() > 0) count += item.maxStackSize * (INVENTORY_SIZE - items.size());
        return count;
    }

    public Stream<ItemStack> getStream() {
        return items.stream();
    }

    public HashMap<ITEM_TYPE, Integer> getHashMap(boolean countPotions) {
        HashMap<ITEM_TYPE, Integer> map = new HashMap<>();
        for (ItemStack item : items) {
            if (!countPotions && item.itemType.isPotion) continue;

            if (map.containsKey(item.itemType)) {
                map.put(item.itemType, map.get(item.itemType) + item.getQuantity());
            } else {
                map.put(item.itemType, item.getQuantity());
            }
        }
        return map;
    }

    public HashSet<ITEM_TYPE> getAvailablePotions() {
        HashSet<ITEM_TYPE> set = new HashSet<>();
        for (ItemStack item : items) {
            if (item.itemType.isPotion) {
                set.add(item.itemType);
            }
        }
        return set;
    }

    public int getFreeSlots() {
        return INVENTORY_SIZE - items.size();
    }

    public static void printInventory(Player player) {
        System.out.println("В инвентаре " + player.playerName + " нашёл следующее...");
        Stream<ItemStack> list = player.inventory.getStream();
        AtomicInteger fullCost = new AtomicInteger();

        list.peek(x -> fullCost.addAndGet(x.itemType.isPotion ? 0 : x.getQuantity() * x.itemType.costSell))
                .forEach(x -> {
                    printWithRightBorder(String.valueOf(x.getQuantity()), 4);
                    System.out.println(" | " + x.itemType.ruName);
                });
        TextWriter.countPrint("     |\n", player.inventory.getFreeSlots());

        System.out.println("-------------------------------------------------" + " Общая стоимость - " + fullCost);

        printWithRightBorder(String.valueOf(player.getMoney()), 4);
        System.out.println(" | Монет имеется");
        printWithRightBorder(String.valueOf(player.getLevel()), 4);
        System.out.print(" | Уровень");
        System.out.print("       Опыт - " + player.getExp());
        System.out.println("       Нужно опыта до следующего уровня - " + player.getXpForNextLevel());
        printWithRightBorder(String.valueOf(player.getHealth()), 4);
        System.out.println(" | Здоровье  (Max - " + player.getMaxHealth() + ")");
        System.out.println("\n\n\n\n");
    }
}
