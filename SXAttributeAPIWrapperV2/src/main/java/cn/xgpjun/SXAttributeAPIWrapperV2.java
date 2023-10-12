package cn.xgpjun;

import cn.xgpjun.xgpequipmentslot.XgpEquipmentSlot;
import cn.xgpjun.xgpequipmentslot.api.AttributeAPIWrapper;
import cn.xgpjun.xgpequipmentslot.api.XESAPI;
import cn.xgpjun.xgpequipmentslot.armorSet.ArmorSet;
import cn.xgpjun.xgpequipmentslot.database.DataManager;
import cn.xgpjun.xgpequipmentslot.equipmentSlot.EquipmentSlot;
import cn.xgpjun.xgpequipmentslot.equipmentSlot.PlayerSlotInfo;
import github.saukiya.sxattribute.api.SXAttributeAPI;
import github.saukiya.sxattribute.data.attribute.SXAttributeData;
import github.saukiya.sxattribute.data.condition.SXConditionType;
import github.saukiya.zfrunes.data.StatsDataManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class SXAttributeAPIWrapperV2 implements AttributeAPIWrapper {
    private final SXAttributeAPI sxapi;
    protected boolean ZF_Ruins;
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

    public void removeEntityAPIData(UUID playerUUID) {
        sxapi.removeEntityAPIData(XgpEquipmentSlot.class, playerUUID);
    }

    public void setEntityAPIData(UUID playerUUID, SXAttributeData data) {
        sxapi.setEntityAPIData(XgpEquipmentSlot.class, playerUUID, data);
    }

    public void attributeUpdate(Player player) {
        sxapi.updateStats(player);
    }

    @Override
    public boolean isUse(LivingEntity entity, ItemStack item){
        return sxapi.isUse(entity,SXConditionType.ALL,item);
    }

    public SXAttributeData loadItemData(Player player, ItemStack itemStack){
        return sxapi.getItemData(player,SXConditionType.ALL,itemStack);
    }
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
        data.add(getTempAttribute(player));
        setEntityAPIData(player.getUniqueId(),data);
        attributeUpdate(player);
        ArmorSet.addPotionEffect(player);
    }

    @Override
    public void register() {
        XgpEquipmentSlot.getApi().setAttributeAPI(this);
        XgpEquipmentSlot.log("设置SX2.0.3为前置！");
    }

    @Override
    public String getAuthor() {
        return "xgpjun";
    }

    @Override
    public String getName() {
        return "SX(2.X)";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    public static void m(){

    }
}
