package cn.xgpjun.xgpequipmentslot.equipmentSlot;

import cn.xgpjun.xgpequipmentslot.XgpEquipmentSlot;
import cn.xgpjun.xgpequipmentslot.api.AttributeAPIWrapper;
import cn.xgpjun.xgpequipmentslot.api.event.UpdateAttributeEvent;
import cn.xgpjun.xgpequipmentslot.apiWrapper.BlankAPIWrapper;
import cn.xgpjun.xgpequipmentslot.apiWrapper.ap.APAttributeAPIWrapper;
import cn.xgpjun.xgpequipmentslot.apiWrapper.mmo.MMOAPIWrapper;
import cn.xgpjun.xgpequipmentslot.apiWrapper.oa.OriginAttributeAPIWrapper;
import cn.xgpjun.xgpequipmentslot.apiWrapper.sx.SXAttributeAPIWrapperV2;
import cn.xgpjun.xgpequipmentslot.apiWrapper.sx.SXAttributeAPIWrapperV3;
import cn.xgpjun.xgpequipmentslot.utils.ConfigSetting;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

public class AttributeManager {
    @Setter
    private static AttributeAPIWrapper attributeAPI;
    static {
        switch (ConfigSetting.depend){
            case "SX":{
                try {
                    Class.forName("github.saukiya.sxattribute.api.SXAPI");
                    attributeAPI = new SXAttributeAPIWrapperV3();
                }catch (Exception e){
                    attributeAPI = new SXAttributeAPIWrapperV2();
                }
                break;
            }
            case "AP":{
                attributeAPI = new APAttributeAPIWrapper();
                break;
            }
            case "MMO":{
                attributeAPI = new MMOAPIWrapper();
                break;
            }
            case "OA":{
                attributeAPI = new OriginAttributeAPIWrapper();
                break;
            }
            default:{
                attributeAPI = new BlankAPIWrapper();
                break;
            }
        }

    }
    public static void addAttribute(Player player){
        Event event = new UpdateAttributeEvent(player.getUniqueId());
        Bukkit.getScheduler().runTask(XgpEquipmentSlot.getInstance(),()->Bukkit.getPluginManager().callEvent(event));

        Bukkit.getScheduler().runTaskAsynchronously(XgpEquipmentSlot.getInstance(),()->attributeAPI.updateAttribute(player));
    }
    public static boolean isUse(Player player, ItemStack itemStack){
        return attributeAPI.isUse(player,itemStack);
    }

}
