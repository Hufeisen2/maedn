package dev.hufeisen.maedn.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class ArmorStandUtils {

    public static ArmorStand spawnAmorStand(Location location, Color color, String name) {

        ArmorStand armorStand = location.getWorld().spawn(location.add(0, 2, 0).toCenterLocation(), ArmorStand.class);
        armorStand.customName(Component.text(name, ColorUtility.colorToTextColor(color), TextDecoration.BOLD));
        armorStand.setCustomNameVisible(true);
        armorStand.getEquipment().setHelmet(generateArmor(Material.LEATHER_HELMET, color));
        armorStand.getEquipment().setChestplate(generateArmor(Material.LEATHER_CHESTPLATE, color));
        armorStand.getEquipment().setLeggings(generateArmor(Material.LEATHER_LEGGINGS, color));
        armorStand.getEquipment().setBoots(generateArmor(Material.LEATHER_BOOTS, color));
        return armorStand;
    }

    private static ItemStack generateArmor(Material material, Color color) {
        ItemStack leatherArmor = new ItemStack(material);
        LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) leatherArmor.getItemMeta();
        leatherArmorMeta.setColor(color);
        leatherArmor.setItemMeta(leatherArmorMeta);
        return leatherArmor;
    }

}
