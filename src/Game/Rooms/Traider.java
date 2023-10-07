package Game.Rooms;

import Game.Items.ITEM_TYPE;
import Game.Entity.Player;
import Game.MESSAGES;

import java.util.HashMap;
import java.util.Map;

import static Game.Game.readUserInput;
import static Game.TextWriter.*;

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
            System.out.println(MESSAGES.ENTERS_5 + "\n\n");
            System.out.println(MESSAGES.TRAIDER_SELL_BUY);

            int input = readUserInput(1, 4, 0);
            switch (input) {
                case 1 -> traiderSelling();
                case 2 -> traiderBuying();
                case 3 -> traiderWorking();
                case 0 -> {
                    System.out.println(MESSAGES.ENTERS_10);
                    System.out.println("Выходим из лавки торговца\n" + MESSAGES.ENTERS_5);
                    isTraiding = false;
                }
            }
        }
    }

    private void traiderWorking() {
        System.out.println(MESSAGES.ENTERS_10);
        System.out.println("у меня пока нет работы для тебя\n");
    }

    private void traiderSelling() {
        boolean printTraiderText = true;

        while (true) {
            System.out.println(MESSAGES.ENTERS_5 + "\n\n");
            if (printTraiderText) {
                System.out.println("Продавец разложил товар и дал вам такой выбор...\n\n\n\n\n\n\n\n");
            } else printTraiderText = true;

            System.out.println(MESSAGES.TRAIDER_SELLING_LIST);
            int option = readUserInput(1, 4, 0);
            if(option == 0) return;

            ITEM_TYPE item = getItemById(option + 3);

            int playerMoney = player.getMoney();
            int oneCost = item.costBuy;
            int maxCount = Math.min(playerMoney / oneCost, player.inventory.getFreeSlotsFor(item));

            if (maxCount == 0) {
                System.out.println(MESSAGES.ENTERS_5 + "\n\n");
                if (playerMoney / oneCost == 0) {
                    System.out.println("Недостаточно монет!");
                } else {
                    System.out.println("Нет места в инвентаре");
                }
                printTraiderText = false;
                continue;
            }

            System.out.print("Можно купить от " + 1 + " до " + maxCount + " едениц товара\nСколько берём - ");
            int quantity = readUserInput(1, maxCount, 0);
            if(quantity == 0) continue;

            System.out.println("Покупаем...\n");

            player.inventory.add(item, quantity);
            player.setMoney(playerMoney - quantity * oneCost);
            player.addExp(quantity);
        }
    }

    private void traiderBuying() {
        boolean printTraiderText = true;
        while (true) {
            HashMap<ITEM_TYPE, Integer> types = player.inventory.getHashMap(false);

            if (types.size() == 0) {
                System.out.println("Нечего продавать...");
                return;
            }

            if(printTraiderText) {
                System.out.println(MESSAGES.ENTERS_10);
                System.out.println("Торговец говорит:\n  Сегодня я скупаю только эти товары" + MESSAGES.ENTERS_5 + "\n\n\n");
            } else printTraiderText = true;

            int f = 1;
            for (Map.Entry<ITEM_TYPE, Integer> item : types.entrySet()) {
                printWithRightBorder(f++ + ". " + item.getKey().ruName, 24);
                printComparisonWithBorder("количество - " + item.getValue(), 36,"(" + item.getKey().costSell * item.getValue() + " монет)");
                System.out.println();
            }
            System.out.println(f++ + ". Продать всё");
            System.out.println(f + ". Продать половину");

            int option = readUserInput(1, types.size() + 2, 0);
            if (option == types.size() + 1) {
                traiderSell(false);
                return;
            } else if (option == types.size() + 2) {
                printTraiderText = false;
                traiderSell(true);
                continue;
            }
            if (option == 0) {
                System.out.println(MESSAGES.ENTERS_10);
                return;
            }

            Map.Entry<ITEM_TYPE, Integer> itemEntry = (Map.Entry<ITEM_TYPE, Integer>) types.entrySet().toArray()[option - 1];
            ITEM_TYPE item = itemEntry.getKey();
            int inInventory = itemEntry.getValue();

            System.out.print("\nВы можете продать от " + 1 + " до " + inInventory + " штук(и)\nСколько продаём? - ");
            int quantity = readUserInput(1, inInventory, 0);
            if(quantity == 0) continue;
            System.out.println(MESSAGES.ENTERS_5);
            System.out.println("\n\n\nПродаём...");
            printTraiderText = false;
            System.out.println(MESSAGES.ENTERS_5 + "\n\n\n");

            player.inventory.remove(item, quantity);
            player.setMoney(player.getMoney() + (quantity * item.costSell));
            player.addExp(quantity);
        }
    }

    private void traiderSell(boolean isHalf) {
        System.out.println(MESSAGES.ENTERS_10);
        int allQuantity = 0;
        int allCost = 0;
        for(Map.Entry<ITEM_TYPE,Integer> item : player.inventory.getHashMap(false).entrySet()) {
            int quantity = (isHalf ? item.getValue() / 2 + 1 : item.getValue());
            int cost = item.getKey().costSell * quantity;
            player.inventory.remove(item.getKey(),quantity);
            player.setMoney(player.getMoney() + cost);
            allQuantity += quantity;
            allCost += cost;
        }
        player.addExp(allQuantity);
        System.out.println("Было продано    - " + allQuantity);
        System.out.println("Монет получено  - " + allCost);
        System.out.println(MESSAGES.ENTERS_5);
    }

    private ITEM_TYPE getItemById(int id) {
        return ITEM_TYPE.values()[id - 1];
    }
}
