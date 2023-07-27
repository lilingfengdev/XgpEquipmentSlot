package cn.xgpjun.xgpequipmentslot.APIWrapper.SX;

import cn.xgpjun.xgpequipmentslot.APIWrapper.AttributeAPIWrapper;
import cn.xgpjun.xgpequipmentslot.ArmorSet.ArmorSet;
import cn.xgpjun.xgpequipmentslot.Database.DataManager;
import cn.xgpjun.xgpequipmentslot.EquipmentSlot.PlayerSlotInfo;
import github.saukiya.sxattribute.data.attribute.SXAttributeData;
import github.saukiya.zfrunes.data.StatsDataManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public abstract class SXAttributeAPIWrapper implements AttributeAPIWrapper {
    protected boolean ZF_Ruins;
    abstract void removeEntityAPIData(UUID playerUUID);
    abstract void setEntityAPIData(UUID playerUUID, SXAttributeData data);
    abstract SXAttributeData loadItemData(Player player, ItemStack item);
    abstract SXAttributeData loadListData(Player player, List<String> list);
    abstract void attributeUpdate(Player player);
    protected SXAttributeData getAttribute(Player player,String name){
        PlayerSlotInfo playerSlotInfo = DataManager.loadPlayerSlotInfo(player.getUniqueId(),name);
        SXAttributeData data = new SXAttributeData();
        for (ItemStack itemStack: playerSlotInfo.getEquipments().values()){
            try{
                if(!isUse(player,itemStack))
                    continue;
            }catch (Exception ignore){
            }
            data.add(loadItemData(player,itemStack));
            if(ZF_Ruins){
                data.add(StatsDataManager.getItemData(itemStack));
            }
        }
        data.add(loadListData(player,ArmorSet.getAttributes(playerSlotInfo)));
        return data;
    }
}
