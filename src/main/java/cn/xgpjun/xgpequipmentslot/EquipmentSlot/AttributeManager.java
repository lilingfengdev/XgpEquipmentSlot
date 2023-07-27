package cn.xgpjun.xgpequipmentslot.EquipmentSlot;

import cn.xgpjun.xgpequipmentslot.Database.DataManager;
import cn.xgpjun.xgpequipmentslot.XgpEquipmentSlot;
import github.saukiya.sxattribute.api.SXAPI;
import github.saukiya.sxattribute.data.PreLoadItem;
import github.saukiya.sxattribute.data.attribute.SXAttributeData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.serverct.ersha.api.AttributeAPI;
import org.serverct.ersha.attribute.data.AttributeSource;

import java.util.ArrayList;

public class AttributeManager {
    public static SXAPI sxapi;
    static {
        if(Bukkit.getPluginManager().getPlugin("SX-Attribute")!=null)
            sxapi = new SXAPI();
    }
    public static void addAttribute(Player player,String name){
        PlayerSlotInfo playerSlotInfo = DataManager.loadPlayerSlotInfo(player.getUniqueId(),name);
        addAttribute(player,playerSlotInfo);
    }
    public static void addAttribute(Player player){
        for(String name:EquipmentSlot.equipmentSlots.keySet()){
            addAttribute(player,name);
        }
    }
    public static void addAttribute(@NotNull Player player, PlayerSlotInfo playerSlotInfo){
        if(player==null)
            return;
        if(sxapi!=null){
            sxapi.removeEntityAPIData(XgpEquipmentSlot.class,player.getUniqueId());
            SXAttributeData data = new SXAttributeData();
            for (ItemStack itemStack: playerSlotInfo.getEquipments().values()){
                try{
                    if(!sxapi.isUse(player,new PreLoadItem(itemStack),itemStack.getItemMeta().getLore()))
                        continue;
                }catch (Exception ignore){
                }
                data.add(sxapi.loadItemData(player,new PreLoadItem(itemStack)));
            }
            sxapi.setEntityAPIData(XgpEquipmentSlot.class,player.getUniqueId(),data);
            sxapi.attributeUpdate(player);
        }else {
            AttributeSource attribute = AttributeAPI.getAttributeSource(new ArrayList<>(playerSlotInfo.getEquipments().values()),player);
            AttributeAPI.addSourceAttribute(AttributeAPI.getAttrData(player),"XES",attribute);
        }
    }
}
