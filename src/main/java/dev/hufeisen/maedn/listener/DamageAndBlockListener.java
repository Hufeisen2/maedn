package dev.hufeisen.maedn.listener;

import dev.hufeisen.maedn.GameState;
import dev.hufeisen.maedn.MaednMain;
import io.papermc.paper.event.block.BlockBreakBlockEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DamageAndBlockListener implements Listener {

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if(MaednMain.getGameState() != GameState.SETUP) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if(MaednMain.getGameState() != GameState.SETUP) {
            event.setCancelled(true);
        }
    }

}
