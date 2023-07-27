package cn.xgpjun.xgpequipmentslot.APIWrapper.SX;

import cn.xgpjun.xgpequipmentslot.EquipmentSlot.EquipmentSlot;
import cn.xgpjun.xgpequipmentslot.XgpEquipmentSlot;
import github.saukiya.sxattribute.api.SXAttributeAPI;
import github.saukiya.sxattribute.data.attribute.SXAttributeData;
import github.saukiya.sxattribute.data.condition.SXConditionType;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;

public class SXAttributeAPIWrapperV2 extends SXAttributeAPIWrapper {
    private final SXAttributeAPI sxapi;
    public SXAttributeAPIWrapperV2() {
        try {
            Class<?> clazz = Class.forName("github.saukiya.sxattribute.SXAttribute");
            Method method = clazz.getMethod("getApi");
            sxapi = (SXAttributeAPI) method.invoke(clazz);
            ZF_Ruins = Bukkit.getPluginManager().getPlugin("ZF-Runes")!=null;
        } catch (Exception e) {
            Bukkit.getPluginManager().disablePlugin(XgpEquipmentSlot.getInstance());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeEntityAPIData(UUID playerUUID) {
        sxapi.removeEntityAPIData(XgpEquipmentSlot.class, playerUUID);
    }

    @Override
    public void setEntityAPIData(UUID playerUUID, SXAttributeData data) {
        sxapi.setEntityAPIData(XgpEquipmentSlot.class, playerUUID, data);
    }

    @Override
    public void attributeUpdate(Player player) {
        sxapi.updateStats(player);
    }

    @Override
    public boolean isUse(LivingEntity entity, ItemStack item){
        return sxapi.isUse(entity,SXConditionType.ALL,item);
    }

    @Override
    public SXAttributeData loadItemData(Player player, ItemStack itemStack){
        return sxapi.getItemData(player,SXConditionType.ALL,itemStack);
    }
    @Override
    public SXAttributeData loadListData(Player player, List<String> list) {
        return sxapi.getLoreData(player,SXConditionType.ALL,list);
    }

    @Override
    public void updateAttribute(Player player) {
        removeEntityAPIData(player.getUniqueId());
        SXAttributeData data = new SXAttributeData();
        for(String name: EquipmentSlot.equipmentSlots.keySet()){
            data.add(getAttribute(player,name));
        }
        setEntityAPIData(player.getUniqueId(),data);
        attributeUpdate(player);
    }
}
