package Game.Rooms;

import Game.Entity.Items.ITEM_TYPE;
import Game.Entity.Player;
import Game.MESSAGES;

import static Game.Game.readUserInput;

public class Traider extends Thread {

    public Player player;

    public Traider(Player player) {
        this.player = player;
    }


    @Override
    public void run() {
        boolean isTraiding = true;
        System.out.println("\nИдём к торговцу...");
        while (isTraiding) {
            System.out.println(MESSAGES.TRAIDER_SELL_BUY);

            int input = readUserInput(1, 3);
            switch (input) {
                case 1 -> traiderSelling();
                case 2 -> traiderBuying();
                case 3 -> isTraiding = false;
            }
        }
    }

    private void traiderSelling() {
        boolean isSelling = true;
        while (isSelling) {
            System.out.println(MESSAGES.TRAIDER_SELLING_LIST);
            System.out.println("Что покупаем?");
            int option = readUserInput(1, 4);

            ITEM_TYPE item = null;
            switch (option) {
                case 1 -> item = ITEM_TYPE.DEXTERITY_POTION;
                case 2 -> item = ITEM_TYPE.HEALING_POTION_15HP;
                case 3 -> item = ITEM_TYPE.HEALING_POTION_40HP;
                case 4 -> {
                    return;
                }
            }
            int playerMoney = player.getMoney();
            int oneCost = item.costSell;
            int maxCount = Math.min(playerMoney / oneCost, player.inventory.getFreeSlotsFor(item));
            if(maxCount == 0) {
                if(playerMoney / oneCost == 0) {
                    System.out.println("Недостаточно монет!");
                } else {
                    System.out.println("Нет места в инвентаре");
                }
            }

            System.out.print("Можно купить от " + 1 + " до " + maxCount + " едениц товара\nСколько берём - ");
            int quantity = readUserInput(1,maxCount);
            System.out.println("Покупаем...\n");
            player.inventory.add(item,quantity);
            player.setMoney(playerMoney - quantity * oneCost);
        }
    }

    private void traiderBuying() {

    }
}
