package cn.xgpjun.xgpequipmentslot.APIWrapper.AP;

import cn.xgpjun.xgpequipmentslot.APIWrapper.AttributeAPIWrapper;
import cn.xgpjun.xgpequipmentslot.ArmorSet.ArmorSet;
import cn.xgpjun.xgpequipmentslot.Database.DataManager;
import cn.xgpjun.xgpequipmentslot.EquipmentSlot.EquipmentSlot;
import cn.xgpjun.xgpequipmentslot.EquipmentSlot.PlayerSlotInfo;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.serverct.ersha.api.AttributeAPI;
import org.serverct.ersha.attribute.data.AttributeSource;

import java.util.ArrayList;

public class APAttributeAPIWrapper implements AttributeAPIWrapper {

    @Override
    public boolean isUse(LivingEntity entity, ItemStack item) {
        return true;
    }

    @Override
    public void updateAttribute(Player player) {
        for(String name: EquipmentSlot.equipmentSlots.keySet()){
            PlayerSlotInfo playerSlotInfo = DataManager.loadPlayerSlotInfo(player.getUniqueId(),name);
            AttributeSource attribute = AttributeAPI.getAttributeSource(new ArrayList<>(playerSlotInfo.getEquipments().values()),player);
            attribute.merge(AttributeAPI.getAttributeSource(ArmorSet.getAttributes(playerSlotInfo)));
            AttributeAPI.addSourceAttribute(AttributeAPI.getAttrData(player),playerSlotInfo.getName(),attribute);
        }
    }
}
