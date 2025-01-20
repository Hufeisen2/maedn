package dev.hufeisen.maedn.utils;

public class DiceUtils {

    private static int nextDiceValue;

    public static int rollDice() {
        if(nextDiceValue > 0) {
            return nextDiceValue;
        }
        return (int) (Math.random() * 6 + 1);
    }

    public static void setNextDiceValue(int value) {
        nextDiceValue = value;
    }
}
