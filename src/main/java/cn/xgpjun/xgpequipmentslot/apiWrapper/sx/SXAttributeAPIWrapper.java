package cn.xgpjun.xgpequipmentslot.apiWrapper.sx;

import cn.xgpjun.xgpequipmentslot.api.AttributeAPIWrapper;
import cn.xgpjun.xgpequipmentslot.api.XESAPI;
import cn.xgpjun.xgpequipmentslot.armorSet.ArmorSet;
import cn.xgpjun.xgpequipmentslot.database.DataManager;
import cn.xgpjun.xgpequipmentslot.equipmentSlot.PlayerSlotInfo;
import github.saukiya.sxattribute.data.attribute.SXAttributeData;
import github.saukiya.zfrunes.data.StatsDataManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
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
        for (ItemStack itemStack: playerSlotInfo.getAllItemStacks()){
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
    protected SXAttributeData getTempAttribute(Player player){
        List<ItemStack> tempItems = new ArrayList<>();
        XESAPI.getTempItems().forEach((s, uuidListMap) -> {
            if(uuidListMap!=null){
                uuidListMap.forEach((uuid, itemStacks) -> {
                    if (player.getUniqueId().equals(uuid)&&itemStacks!=null){
                        tempItems.addAll(itemStacks);
                    }
                });
            }
        });
        SXAttributeData data = new SXAttributeData();
        for (ItemStack itemStack: tempItems){
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
        return data;
    }
}
