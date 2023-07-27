package cn.xgpjun.xgpequipmentslot.Listener;

import cn.xgpjun.xgpequipmentslot.EquipmentSlot.AttributeManager;
import cn.xgpjun.xgpequipmentslot.XgpEquipmentSlot;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void playerJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        Bukkit.getScheduler().runTaskLater(XgpEquipmentSlot.getInstance(),()-> AttributeManager.addAttribute(player),20L);
    }

}
