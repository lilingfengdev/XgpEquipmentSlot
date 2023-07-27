package cn.xgpjun.xgpequipmentslot.EquipmentSlot;

import cn.xgpjun.xgpequipmentslot.APIWrapper.AP.APAttributeAPIWrapper;
import cn.xgpjun.xgpequipmentslot.APIWrapper.AttributeAPIWrapper;
import cn.xgpjun.xgpequipmentslot.APIWrapper.SX.SXAttributeAPIWrapperV2;
import cn.xgpjun.xgpequipmentslot.APIWrapper.SX.SXAttributeAPIWrapperV3;
import cn.xgpjun.xgpequipmentslot.Utils.ConfigSetting;
import cn.xgpjun.xgpequipmentslot.XgpEquipmentSlot;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AttributeManager {
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
        }

    }
    public static void addAttribute(Player player){
        Bukkit.getScheduler().runTaskAsynchronously(XgpEquipmentSlot.getInstance(),()->attributeAPI.updateAttribute(player));
    }
    public static boolean isUse(Player player, ItemStack itemStack){
        return attributeAPI.isUse(player,itemStack);
    }

}
