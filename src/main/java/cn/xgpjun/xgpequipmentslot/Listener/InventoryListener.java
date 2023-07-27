package cn.xgpjun.xgpequipmentslot.Listener;

import cn.xgpjun.xgpequipmentslot.Gui.XESHolder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public class InventoryListener implements Listener {

    @EventHandler
    public void handleClick(InventoryClickEvent e){
        if(e.getInventory().getHolder()==null||!(e.getInventory().getHolder() instanceof XESHolder))
            return;
        if(e.isShiftClick()){
            e.setCancelled(true);
            return;
        }
        if((e.getRawSlot()>=0&&e.getRawSlot()<=53))
            e.setCancelled(true);
        ((XESHolder) e.getInventory().getHolder()).handleClick(e);
    }

    @EventHandler
    public void dragItem(InventoryDragEvent e){
        if(e.getInventory().getHolder() == null)
            return;
        if(e.getInventory().getHolder() instanceof XESHolder){
            e.setCancelled(true);
        }
    }

}
