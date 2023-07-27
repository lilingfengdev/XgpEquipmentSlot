package cn.xgpjun.xgpequipmentslot.APIWrapper.SX;

import cn.xgpjun.xgpequipmentslot.EquipmentSlot.EquipmentSlot;
import cn.xgpjun.xgpequipmentslot.XgpEquipmentSlot;
import github.saukiya.sxattribute.SXAttribute;
import github.saukiya.sxattribute.api.SXAPI;
import github.saukiya.sxattribute.data.PreLoadItem;
import github.saukiya.sxattribute.data.attribute.SXAttributeData;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public class SXAttributeAPIWrapperV3 extends SXAttributeAPIWrapper {
    private final SXAPI sxapi;
    public SXAttributeAPIWrapperV3() {
        sxapi = SXAttribute.getApi();
        ZF_Ruins = Bukkit.getPluginManager().getPlugin("ZF-Runes")!=null;
    }

    @Override
    public void removeEntityAPIData(UUID playerUUID) {
        sxapi.removeEntityAPIData(XgpEquipmentSlot.class, playerUUID);
    }

    @Override
    public void setEntityAPIData(UUID playerUUID, SXAttributeData data) {
        sxapi.setEntityAPIData(XgpEquipmentSlot.class,playerUUID,data);
    }

    @Override
    public void attributeUpdate(Player player) {
        sxapi.attributeUpdate(player);
    }

    @Override
    public boolean isUse(LivingEntity entity, ItemStack item){
        return sxapi.isUse(entity,new PreLoadItem(item),item.getItemMeta().getLore());
    }

    @Override
    public SXAttributeData loadItemData(Player player, ItemStack item) {
        return sxapi.loadItemData(player,new PreLoadItem(item));
    }
    @Override
    public SXAttributeData loadListData(Player player, List<String> list) {
        return sxapi.loadListData(list);
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
