package cn.xgpjun.xgpequipmentslot.api;

import cn.xgpjun.xgpequipmentslot.armorSet.ArmorSet;
import cn.xgpjun.xgpequipmentslot.database.DataManager;
import cn.xgpjun.xgpequipmentslot.equipmentSlot.AttributeManager;
import cn.xgpjun.xgpequipmentslot.equipmentSlot.EquipmentSlot;
import cn.xgpjun.xgpequipmentslot.equipmentSlot.PlayerSlotInfo;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class XESAPI {
    @Getter
    private static Map<String,Map<UUID, List<ItemStack>>> tempItems = new HashMap<>();
    /**
     * 获得指定的物品栏 建议放在异步代码内
     * PlayerSlotInfo 实例的equipments字段 为玩家装备的物品。 键为槽位，值为物品。
     * @param playerUUID 玩家UUID
     * @param equipmentSlotName 物品栏名称
     * @return 物品栏数据
     */
    public @NotNull PlayerSlotInfo getSlotInfo(UUID playerUUID, String equipmentSlotName){
        return DataManager.loadPlayerSlotInfo(playerUUID,equipmentSlotName);
    }

    /**
     * 获取所有注册的物品栏名称
     * @return 所有注册的物品栏名称
     */
    public @NotNull Set<String> getEquipmentSlotNames(){
        return EquipmentSlot.equipmentSlots.keySet();
    }

    public @NotNull Set<String> getArmorSetNames(){return ArmorSet.armorSets.keySet();
    }
    //建议异步
    public @NotNull Set<UUID> getAllPlayer(){
        return DataManager.getAllPlayer();
    }

    //建议异步
    public void save(@NotNull PlayerSlotInfo playerSlotInfo){
        DataManager.savePlayerSlotInfo(playerSlotInfo);
    }

    public void setAttributeAPI(@NotNull AttributeAPIWrapper attributeAPI){
        AttributeManager.setAttributeAPI(attributeAPI);
    }

    public void setTempItems(String key,UUID uuid, List<ItemStack> items){
        Map<UUID, List<ItemStack>> map = getTempItems().get(key);
        if (map==null){
            map = new HashMap<>();
        }
        map.put(uuid,items);
        getTempItems().put(key,map);
    }

}
