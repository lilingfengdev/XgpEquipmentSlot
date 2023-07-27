package cn.xgpjun.xgpequipmentslot.ArmorSet;

import cn.xgpjun.xgpequipmentslot.EquipmentSlot.PlayerSlotInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class ArmorSet {
    public static Map<String,ArmorSet> armorSets = new HashMap<>();
    private String name;
    private String lore;
    private String type;
    private List<String> display;
    private Map<Integer,List<String>> attributes;
    public void add(){
        armorSets.put(name,this);
    }
    public static List<String> getAttributes(PlayerSlotInfo playerSlotInfo){
        List<String> result = new ArrayList<>();
        Map<String,Integer> map = new HashMap<>();
        for (ArmorSet armorSet :armorSets.values()){
            map.put(armorSet.name, 0);
        }
        for(ItemStack item:playerSlotInfo.getEquipments().values()){
            if (item.getItemMeta()==null||!item.getItemMeta().hasLore())
                continue;
            for (String name: map.keySet()){
                for (String lore : item.getItemMeta().getLore()){
                    if (lore.contains(armorSets.get(name).lore)){
                        map.put(name,map.get(name)+1);
                        break;
                    }
                }
            }
        }
        map.forEach((String name,Integer count)->{
            if(count>0){
                ArmorSet armorSet = armorSets.get(name);
                for (int i=count;i>0;i--){
                    if(armorSet.attributes.containsKey(i)){
                        result.addAll(armorSet.attributes.get(i));
                        break;
                    }
                }
            }
        });
        return result;
    }
}
