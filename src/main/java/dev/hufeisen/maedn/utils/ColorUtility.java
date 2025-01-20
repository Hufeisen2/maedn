package dev.hufeisen.maedn.utils;

import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Color;

public class ColorUtility {

    public static TextColor colorToTextColor(Color color) {
        return TextColor.color(color.asRGB());
    }

}
