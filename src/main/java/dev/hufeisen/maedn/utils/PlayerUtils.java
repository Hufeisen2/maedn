package dev.hufeisen.maedn.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class PlayerUtils {

    public static ItemStack getSkull(Player player) {

        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);

        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        skullMeta.setOwningPlayer(player);

        skull.setItemMeta(skullMeta);

        return skull;
    }
}
