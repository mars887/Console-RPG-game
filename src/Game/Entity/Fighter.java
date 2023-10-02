package Game.Entity;

import java.util.Random;

public interface Fighter {
    default int[] attack(Random random, Entity entity) {
        if (random.nextFloat() > entity.dexterity) return new int[]{0, 0};
        boolean isKrit = random.nextInt(10) > 7;
        return new int[] {(int) (entity.strength * (isKrit ? 1.5 : 1)), isKrit ? 1 : 0};
    }
}
