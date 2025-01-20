package dev.hufeisen.maedn.utils;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Color;
import org.bukkit.boss.BarColor;

public class ColorUtils {

    public static TextColor colorToTextColor(Color color) {
        return TextColor.color(color.asRGB());
    }

    public static BossBar.Color colorToBarColor(Color color) {
        if (color.equals(Color.RED)) {
            return BossBar.Color.RED;
        } else if (color.equals(Color.GREEN)) {
            return BossBar.Color.GREEN;
        } else if (color.equals(Color.YELLOW)) {
            return BossBar.Color.YELLOW;
        } else if (color.equals(Color.BLUE)) {
            return BossBar.Color.BLUE;
        } else {
            return BossBar.Color.WHITE;
        }
    }

}
