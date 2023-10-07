package cn.xgpjun.xgpequipmentslot.apiWrapper.oa;

import ac.github.oa.api.OriginAttributeAPI;
import ac.github.oa.internal.core.attribute.AttributeData;
import cn.xgpjun.xgpequipmentslot.XgpEquipmentSlot;
import cn.xgpjun.xgpequipmentslot.api.AttributeAPIWrapper;
import cn.xgpjun.xgpequipmentslot.armorSet.ArmorSet;
import cn.xgpjun.xgpequipmentslot.database.DataManager;
import cn.xgpjun.xgpequipmentslot.equipmentSlot.EquipmentSlot;
import cn.xgpjun.xgpequipmentslot.equipmentSlot.PlayerSlotInfo;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OriginAttributeAPIWrapper implements AttributeAPIWrapper {
    private final OriginAttributeAPI oaAPI;
    public OriginAttributeAPIWrapper(){
        oaAPI = OriginAttributeAPI.INSTANCE;
    }
    @Override
    public boolean isUse(LivingEntity entity, ItemStack item) {
        return true;
    }

    @Override
    public void updateAttribute(Player player) {
        removeEntityAPIData(player.getUniqueId());
        AttributeData data = new AttributeData();
        for(String name: EquipmentSlot.equipmentSlots.keySet()){
            data.merge(getAttribute(player,name));
        }
        setEntityAPIData(player.getUniqueId(),data);
        attributeUpdate(player);
        ArmorSet.addPotionEffect(player);
    }

    void removeEntityAPIData(UUID playerUUID) {
        oaAPI.removeExtra(playerUUID, XgpEquipmentSlot.class.getName());
    }

    void setEntityAPIData(UUID playerUUID, AttributeData data) {
        oaAPI.setExtra(playerUUID,XgpEquipmentSlot.class.getName(),data);
    }

    AttributeData loadItemData(Player player, ItemStack item) {
        List<String> lore;
        if(item.getItemMeta()!=null&&item.getItemMeta().getLore()!=null){
            lore=item.getItemMeta().getLore();
        }else {
            lore = new ArrayList<>();
        }
        return loadListData(player,lore);
    }

    AttributeData loadListData(Player player, List<String> list) {
        return oaAPI.loadList(player,list);
    }

    void attributeUpdate(Player player) {
        oaAPI.callUpdate(player);
    }
    protected AttributeData getAttribute(Player player, String name){
        PlayerSlotInfo playerSlotInfo = DataManager.loadPlayerSlotInfo(player.getUniqueId(),name);
        AttributeData data = new AttributeData();
        for (ItemStack itemStack: playerSlotInfo.getEquipments().values()){
            try{
                if(!isUse(player,itemStack))
                    continue;
            }catch (Exception ignore){
            }
            data.merge(loadItemData(player,itemStack));
        }
        data.merge(loadListData(player,ArmorSet.getAttributes(playerSlotInfo)));
        return data;
    }
}
