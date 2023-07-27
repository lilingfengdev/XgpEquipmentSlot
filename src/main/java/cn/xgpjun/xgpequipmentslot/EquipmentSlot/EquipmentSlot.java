package cn.xgpjun.xgpequipmentslot.EquipmentSlot;

import cn.xgpjun.xgpequipmentslot.Database.DataManager;
import cn.xgpjun.xgpequipmentslot.Gui.Impl.EquipmentSlotInventory;
import cn.xgpjun.xgpequipmentslot.Utils.ConfigSetting;
import cn.xgpjun.xgpequipmentslot.Utils.NMSUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class EquipmentSlot {
    @Getter
    public static Map<String,EquipmentSlot> equipmentSlots = new HashMap<>();
    private String name;
    private boolean permission;
    //GUI
    private String title;
    private List<String> layout;
    private Map<String, ItemStack> guiItems;
    //槽位 槽位信息
    private Map<Integer,Slot> slotInfo;

    public void addToGlobal(){
        equipmentSlots.put(this.name,this);
    }

    public Inventory getGui(OfflinePlayer player){
        return new EquipmentSlotInventory(this, DataManager.loadPlayerSlotInfo(player.getUniqueId(),getName())).getInventory();
    }
    public boolean canWear(int index, Player player, ItemStack equipment){
        if (player==null)
            return false;
        Slot slot = slotInfo.get(index);
        //非装备槽
        if(slot==null){
            return false;
        }
        if(ConfigSetting.depend.equals("SX")){
            if(equipment.getItemMeta()==null){
                return false;
            }
            //SX判断
            try{
                if(!AttributeManager.isUse(player,equipment)){
                    return false;
                }
            }catch (Exception ignored){
            }

            //检验tag
            if(slot.getType()!=null){
                if(NMSUtils.checkTag(equipment,slot.getType())){
                    return true;
                }
            }
            //检验lore
            if(slot.getLore()!=null&&equipment.getItemMeta().getLore()!=null){
                for (String lore:equipment.getItemMeta().getLore()){
                    if(lore.contains(slot.getLore())){
                        return true;
                    }
                }
            }
        }
        if(ConfigSetting.depend.equals("AP")){
            if(equipment.getItemMeta()==null){
                return false;
            }
            //检验tag
            if(slot.getType()!=null){
                if(NMSUtils.checkTag(equipment,slot.getType())){
                    return true;
                }
            }
            //检验lore
            if(slot.getLore()!=null&&equipment.getItemMeta().getLore()!=null){
                for (String lore:equipment.getItemMeta().getLore()){
                    if(lore.contains(slot.getLore())){
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
