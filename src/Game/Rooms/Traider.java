package Game.Rooms;

import Game.Items.ITEM_TYPE;
import Game.Entity.Player;
import Game.MESSAGES;

import java.util.HashMap;
import java.util.Map;

import static Game.Game.countPrint;
import static Game.Game.readUserInput;

public class Traider extends Thread {

    public Player player;

    public Traider(Player player) {
        this.player = player;
    }


    @Override
    public void run() {
        boolean isTraiding = true;
        System.out.println("Идём к торговцу...");
        while (isTraiding) {
            System.out.println(MESSAGES.ENTERS_5 + "\n\n\n");
            System.out.println(MESSAGES.TRAIDER_SELL_BUY);

            int input = readUserInput(1, 3);
            switch (input) {
                case 1 -> traiderSelling();
                case 2 -> traiderBuying();
                case 3 -> {
                    System.out.println(MESSAGES.ENTERS_10);
                    System.out.println("Выходим из лавки торговца\n" + MESSAGES.ENTERS_5);
                    isTraiding = false;
                }
            }
        }
    }

    private void traiderSelling() {
        boolean isSelling = true;
        while (isSelling) {
            System.out.println(MESSAGES.ENTERS_10);
            System.out.println(MESSAGES.TRAIDER_SELLING_LIST);
            int option = readUserInput(1, 4);

            ITEM_TYPE item = null;
            if ((item = getItemById(option + 3)) == null) {
                System.out.println(MESSAGES.ENTERS_10);
                return;
            }

            int playerMoney = player.getMoney();
            int oneCost = item.costSell;
            int maxCount = Math.min(playerMoney / oneCost, player.inventory.getFreeSlotsFor(item));

            if (maxCount == 0) {
                if (playerMoney / oneCost == 0) {
                    System.out.println("Недостаточно монет!");
                } else {
                    System.out.println("Нет места в инвентаре");
                }
                return;
            }

            System.out.print("Можно купить от " + 1 + " до " + maxCount + " едениц товара\nСколько берём - ");
            int quantity = readUserInput(1, maxCount);
            System.out.println("Покупаем...\n");
            player.inventory.add(item, quantity);
            player.setMoney(playerMoney - quantity * oneCost);
            player.addExp(quantity);
        }
    }

    private void traiderBuying() {
        HashMap<ITEM_TYPE, Integer> types = new HashMap();
        for (int i = 1; i <= 3; i++) {
            if (player.inventory.contains(getItemById(i)) > 0)
                types.put(getItemById(i), player.inventory.contains(getItemById(i)));
        }
        if (types.size() == 0) {
            System.out.println("Нечего продавать...");
            return;
        }
        int f = 1;
        System.out.println(MESSAGES.ENTERS_10);
        System.out.println("Торговец говорит:\n  Сегодня я скупаю только эти товары" + MESSAGES.ENTERS_5 + "\n\n\n");
        for (Map.Entry<ITEM_TYPE, Integer> item : types.entrySet()) {
            System.out.print((f++) + ". " + item.getKey().ruName);
            countPrint(" ", 30 - item.getKey().ruName.length());
            System.out.println("имеется - " + item.getValue() + "   (" + (item.getKey().costSell * item.getValue()) + " монет)");
        }
        System.out.println((types.size() + 1) + ". Назад");
        int option = readUserInput(1, types.size() + 1);
        if(option == types.size() + 1) {
            System.out.println(MESSAGES.ENTERS_10);
            return;
        }

        Map.Entry<ITEM_TYPE, Integer> itemEntry = (Map.Entry<ITEM_TYPE, Integer>) types.entrySet().toArray()[option - 1];
        ITEM_TYPE item = itemEntry.getKey();
        int inInventory = itemEntry.getValue();

        System.out.print("\nВы можете продать от " + 1 + " до " + inInventory + " штук(и)\nСколько продаём? - ");
        int quantity = readUserInput(1, inInventory);
        System.out.println(MESSAGES.ENTERS_5);
        System.out.println("\n\n\nПродаём...");

        player.inventory.remove(item, quantity);
        player.setMoney(player.getMoney() + (quantity * item.costSell));
        player.addExp(quantity);
    }

    private ITEM_TYPE getItemById(int id) {
        switch (id) {
            case 1 -> {
                return ITEM_TYPE.BONE;
            }
            case 2 -> {
                return ITEM_TYPE.FABRIC;
            }
            case 3 -> {
                return ITEM_TYPE.DIAMOND;
            }
            case 4 -> {
                return ITEM_TYPE.DEXTERITY_POTION;
            }
            case 5 -> {
                return ITEM_TYPE.HEALING_POTION_15HP;
            }
            case 6 -> {
                return ITEM_TYPE.HEALING_POTION_40HP;
            }
        }
        return null;
    }
}
