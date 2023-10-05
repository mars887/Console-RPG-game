package Game.Entity;

import java.util.function.BiFunction;

public class DataSupplier {
    public static BiFunction<Integer, Integer, Integer> StrengthFunction = (level, ident) -> 20 + level * ident;
    public static BiFunction<Integer, Integer, Integer> HealthFunction = (level, ident) -> 100 + level * ident;
    public static BiFunction<Integer, Float, Double> DexterityFunction =
            (level, ident) -> 1 - ((Math.cbrt(Math.pow(ident, Math.sqrt(level)))) / 4);
    /*
    if(ident == 0.7)    // forPlayer
        level 1 = 0.778
        level 3 = 0.796
        level 8 = 0.821
        level 15 = 0.842
        level 40 = 0.882
        level 100 = 0.924
        level 300 = 0.968
     */
    public static BiFunction<Integer, Integer, Integer> ExpToNextLevelFunction = (level, ident) -> (int) (100 + Math.pow(level,3));
    public static BiFunction<Integer, Float, Integer> ExpToDropFunction = (level,ident) -> (int) (10 + level * level * ident);
}