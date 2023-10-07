package Game;

import Game.Items.ITEM_TYPE;

public abstract class MESSAGES {

    public static final String ENTERS_15 = "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n";
    public static final String ENTERS_10 = "\n\n\n\n\n\n\n\n\n\n";
    public static final String ENTERS_5 = "\n\n\n\n\n";

    public static final String MENU_MESSAGE = """
            1. К торговцу
            2. Mob Rush
            3. В тёмный лес
            4. К целительнице (10 монет)
            5. Заглянуть в инвентарь, узнать уровень
             """;

    public static final String TRAIDER_NOT_AVAILABLE =
            "Рабочий день торговца ещё не начался, загляните в другое время...\n\n\n";

    public static final String DANGEON_NOT_AVAILABLE =
            "В лесу ремонт, загляните в другое время...\n\n\n";

    public static final String MOB_RUSH_NOT_AVAILABLE =
            "Пока не доступно, загляните в другое время";


    public static final String TRAIDER_SELL_BUY = """
            1. Хочу купить
            2. Хочу продать
            3. Хочу подзаработать""";

    public static final String TRAIDER_SELLING_LIST =
            "\n 1. " + ITEM_TYPE.getTradeMessage(ITEM_TYPE.HEALING_POTION_30PE) +
                    "\n 2. " + ITEM_TYPE.getTradeMessage(ITEM_TYPE.HEALING_POTION_70PE) +
                    "\n 3. " + ITEM_TYPE.getTradeMessage(ITEM_TYPE.DEXTERITY_POTION) +
                    "\n 4. " + ITEM_TYPE.getTradeMessage(ITEM_TYPE.PROTECTION_POTION);
}
