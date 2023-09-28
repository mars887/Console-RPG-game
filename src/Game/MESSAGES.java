package Game;

import Game.Entity.Items.ITEM_TYPE;

public abstract class MESSAGES {
    public static final String MENU_MESSAGE = """
            
            Куда вы хотите пойти?
                                
            1. К торговцу        
            2. В тёмный лес     
            3. Заглянуть в инвентарь, узнать уровень
            4. На выход""";

    public static final String TRAIDER_NOT_AVAILABLE =
            "Рабочий день торговца ещё не начался, загляните в другое время...\n\n\n";

    public static final String DANGEON_NOT_AVAILABLE =
            "В лесу ремонт, загляните в другое время...\n\n\n";


    public static final String TRAIDER_SELL_BUY = """
            1. Хочу что-нибуть купить
            2. Хочу продать
            3. Так, где тут выход
            """;

    public static final String TRAIDER_SELLING_LIST = "Список товаров: " +
            "\n1. " + ITEM_TYPE.getTradeMessage(ITEM_TYPE.DEXTERITY_POTION) +
            "\n2. " + ITEM_TYPE.getTradeMessage(ITEM_TYPE.HEALING_POTION_15HP) +
            "\n3. " + ITEM_TYPE.getTradeMessage(ITEM_TYPE.HEALING_POTION_40HP) +
            "\n4. Вернуться назад";
}