package cn.xgpjun.xgpequipmentslot.equipmentSlot;

import cn.xgpjun.xgpequipmentslot.XgpEquipmentSlot;
import cn.xgpjun.xgpequipmentslot.api.AttributeAPIWrapper;
import cn.xgpjun.xgpequipmentslot.api.event.UpdateAttributeEvent;
import cn.xgpjun.xgpequipmentslot.apiWrapper.BlankAPIWrapper;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

public class AttributeManager {
    @Setter
    @Getter
    private static AttributeAPIWrapper attributeAPI;
    static {
        attributeAPI = new BlankAPIWrapper();

    }
    public static void addAttribute(Player player){
        if(player==null||!player.isOnline()){
            return;
        }
        Event event = new UpdateAttributeEvent(player.getUniqueId());
        Bukkit.getScheduler().runTask(XgpEquipmentSlot.getInstance(),()->Bukkit.getPluginManager().callEvent(event));

        Bukkit.getScheduler().runTaskAsynchronously(XgpEquipmentSlot.getInstance(),()->attributeAPI.updateAttribute(player));
    }
    public static boolean isUse(Player player, ItemStack itemStack){
        return attributeAPI.isUse(player,itemStack);
    }

}
