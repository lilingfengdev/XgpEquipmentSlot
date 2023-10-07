package cn.xgpjun.xgpequipmentslot.listener;

import cn.xgpjun.xgpequipmentslot.gui.XESHolder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InventoryListener implements Listener {
    public static final Map<UUID,Long> map = new HashMap<>();
    @EventHandler
    public void handleClick(InventoryClickEvent e){
        if(e.getInventory().getHolder()==null||!(e.getInventory().getHolder() instanceof XESHolder))
            return;
        long nowTime = System.currentTimeMillis();
        long lastTime = map.get(e.getWhoClicked().getUniqueId())==null? nowTime-114514 : map.get(e.getWhoClicked().getUniqueId());
        //限制点击频率
        XESHolder xesHolder = ((XESHolder) e.getInventory().getHolder());
        if(nowTime-lastTime<50*4&&(e.getRawSlot()>=0&&e.getRawSlot()<=xesHolder.getSize())){
            e.setCancelled(true);
            return;
        }else {
            map.put(e.getWhoClicked().getUniqueId(),  nowTime);
        }

        if(e.getAction().equals(InventoryAction.DROP_ONE_CURSOR)||e.getAction().equals(InventoryAction.DROP_ALL_CURSOR)||e.getClick().equals(ClickType.DOUBLE_CLICK)||e.getClick().equals(ClickType.DROP)||e.getClick().equals(ClickType.WINDOW_BORDER_LEFT)||e.getClick().equals(ClickType.WINDOW_BORDER_RIGHT)){
            e.setCancelled(true);
            return;
        }
        if((e.getRawSlot()>=0&&e.getRawSlot()<xesHolder.getSize()))
            e.setCancelled(true);
        xesHolder.handleClick(e);
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
