package cn.xgpjun.xgpequipmentslot.listener;

import cn.xgpjun.xgpequipmentslot.XgpEquipmentSlot;
import cn.xgpjun.xgpequipmentslot.armorSet.bonus.Potions;
import cn.xgpjun.xgpequipmentslot.database.DataManager;
import cn.xgpjun.xgpequipmentslot.equipmentSlot.AttributeManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void playerJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        DataManager.loadPlayerTemp(e.getPlayer().getUniqueId());
        Bukkit.getScheduler().runTaskLater(XgpEquipmentSlot.getInstance(),()-> AttributeManager.addAttribute(player),20L);
    }

    @EventHandler
    public void playerQuit(PlayerQuitEvent e){
        Player player = e.getPlayer();
        Potions.remove(player);
        DataManager.savePlayerTemp(e.getPlayer().getUniqueId());
        InventoryListener.map.remove(player.getUniqueId());
    }

}
