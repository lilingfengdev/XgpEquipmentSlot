package cn.xgpjun.xgpequipmentslot.equipmentSlot;

import cn.xgpjun.xgpequipmentslot.database.DataManager;
import cn.xgpjun.xgpequipmentslot.gui.impl.EquipmentSlotInventory;
import cn.xgpjun.xgpequipmentslot.utils.NMSUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private Map<String, GuiItem> guiItems;
    //槽位 槽位信息
    private Map<Integer,Slot> slotInfo;


    public void addToGlobal(){
        equipmentSlots.put(this.name,this);
    }

    public EquipmentSlotInventory getGui(OfflinePlayer player){
        return new EquipmentSlotInventory(this, DataManager.loadPlayerSlotInfo(player.getUniqueId(),getName()));
    }
    public boolean canWear(int index, Player player, ItemStack equipment){
        if (player==null)
            return false;
        Slot slot = slotInfo.get(index);
        //非装备槽
        if(slot==null){
            return false;
        }
        return check(player,equipment,slot);
    }
    public Future<Integer> autoMatch(Player player, ItemStack equipment, PlayerSlotInfo playerSlotInfo){
        CompletableFuture<Integer> result = new CompletableFuture<>();
        for (int index:slotInfo.keySet()){
            if(playerSlotInfo.getEquipments().get(index)==null||playerSlotInfo.getEquipments().get(index).getItemMeta()==null){
                if(check(player,equipment,slotInfo.get(index))){
                    result.complete(index);
                }
            }
        }
        result.complete(-1);
        return result;
    }

    private boolean check(Player player,ItemStack equipment,Slot slot){
        if(equipment==null||equipment.getItemMeta()==null){
            return false;
        }
        //插件是否可用
        try{
            if(!AttributeManager.isUse(player,equipment)){
                return false;
            }
        }catch (Exception ignored){
        }
        //建议额外配置
        if(!slot.getOther().isEmpty()){
            for (Map.Entry<String,String> e:slot.getOther().entrySet()){
                if (NMSUtils.checkTag(equipment,e.getKey(),e.getValue())){
                    return true;
                }
            }
        }
        //检验tag
        if(slot.getType()!=null){
            if(NMSUtils.checkTag(equipment,"XgpES",slot.getType())){
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
        //检验regex
        if(slot.getRegex()!=null&&equipment.getItemMeta().getLore()!=null){
            Pattern pattern = Pattern.compile(slot.getRegex());
            for (String lore:equipment.getItemMeta().getLore()){
                Matcher matcher = pattern.matcher(lore);
                if(matcher.find()){
                    return true;
                }
            }
        }
        return false;
    }
}
