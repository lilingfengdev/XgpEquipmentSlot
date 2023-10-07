package cn.xgpjun.xgpequipmentslot.api;

import cn.xgpjun.xgpequipmentslot.armorSet.ArmorSet;
import cn.xgpjun.xgpequipmentslot.database.DataManager;
import cn.xgpjun.xgpequipmentslot.equipmentSlot.AttributeManager;
import cn.xgpjun.xgpequipmentslot.equipmentSlot.EquipmentSlot;
import cn.xgpjun.xgpequipmentslot.equipmentSlot.PlayerSlotInfo;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.UUID;

public class XESAPI {

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

}
