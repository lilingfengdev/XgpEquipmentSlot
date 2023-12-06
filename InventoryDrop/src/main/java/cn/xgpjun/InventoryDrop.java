package cn.xgpjun;

import cn.xgpjun.xgpequipmentslot.XgpEquipmentSlot;
import cn.xgpjun.xgpequipmentslot.api.XESExpansion;
import cn.xgpjun.xgpequipmentslot.equipmentSlot.PlayerSlotInfo;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class InventoryDrop implements Listener,XESExpansion {
    static String permission = null;
    @EventHandler
    public void death(PlayerDeathEvent e){
        if (permission!=null&&e.getEntity().hasPermission(permission)){
            return;
        }
        if (!e.getKeepInventory()){
            //掉落
            XgpEquipmentSlot.getApi().getEquipmentSlotNames().forEach(s -> {
                PlayerSlotInfo slotInfo = XgpEquipmentSlot.getApi().getSlotInfo(e.getEntity().getUniqueId(),s);
                if (slotInfo!=null){
                    e.getDrops().addAll(slotInfo.getEquipments().values());
                    slotInfo.getEquipments().clear();
                    XgpEquipmentSlot.getApi().save(slotInfo);
                }
            });
        }
    }

    @Override
    public void register() {
        XgpEquipmentSlot.log("load "+getName());
        permission = XgpEquipmentSlot.getInstance().getConfig().getString("keepInventory");
    }

    @Override
    public String getAuthor() {
        return "xgpjun";
    }

    @Override
    public String getName() {
        return "InventoryDrop";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }
}