package cn.xgpjun.xgpequipmentslot.armorSet;

import cn.xgpjun.xgpequipmentslot.armorSet.bonus.Potions;
import cn.xgpjun.xgpequipmentslot.database.DataManager;
import cn.xgpjun.xgpequipmentslot.equipmentSlot.EquipmentSlot;
import cn.xgpjun.xgpequipmentslot.equipmentSlot.PlayerSlotInfo;
import cn.xgpjun.xgpequipmentslot.utils.MyItemBuilder;
import cn.xgpjun.xgpequipmentslot.utils.NMSUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
@AllArgsConstructor
public class ArmorSet {
    public static Map<String,ArmorSet> armorSets = new HashMap<>();
    private String name;
    private String lore;
    private String type;
    private String regex;
    private List<String> display;
    private Map<Integer,List<String>> attributes;
    private Map<Integer,List<PotionEffect>> potions;
    private List<String> loreName;
    private Map<Integer,List<String>> loreActive;
    private Map<Integer,List<String>> loreInactive;

    public void register(){
        armorSets.put(name,this);
    }
    public static List<String> getAttributes(PlayerSlotInfo playerSlotInfo){
        List<String> result = new ArrayList<>();
        Map<String,Integer> map = getArmorSetAmount(playerSlotInfo);
        map.forEach((String name,Integer count)->{
            if(count>0){
                ArmorSet armorSet = armorSets.get(name);
                for (int i=count;i>0;i--){
                    if(armorSet.attributes.containsKey(i)){
                        result.addAll(armorSet.attributes.get(i));
                    }
                }
            }
        });
        return result;
    }

    public static Map<String,Integer> getArmorSetAmount(PlayerSlotInfo playerSlotInfo){
        Map<String,Integer> map = new HashMap<>();
        for (ArmorSet armorSet :armorSets.values()){
            map.put(armorSet.name, 0);
        }
        for(ItemStack item:playerSlotInfo.getEquipments().values()){
            for (String name: map.keySet()){
                if(containLore(armorSets.get(name).lore,item)|| NMSUtils.checkTag(item,"XgpES_Set",armorSets.get(name).type)){
                    map.put(name,map.get(name)+1);
                }
            }
        }
        return map;
    }
    public static void addPotionEffect(Player player){
        if(!player.isOnline()){
            return;
        }
        List<PotionEffect> effects = new ArrayList<>();
        for(String equipName: EquipmentSlot.equipmentSlots.keySet()){
            PlayerSlotInfo playerSlotInfo = DataManager.loadPlayerSlotInfo(player.getUniqueId(),equipName);

            Map<String,Integer> map = new HashMap<>();
            for (ArmorSet armorSet :armorSets.values()){
                map.put(armorSet.name, 0);
            }
            for(ItemStack item:playerSlotInfo.getEquipments().values()){
                if (item.getItemMeta()==null||!item.getItemMeta().hasLore())
                    continue;
                for (String name: map.keySet()){
                    if(containLore(armorSets.get(name).lore,item)|| NMSUtils.checkTag(item,"XgpES_Set",armorSets.get(name).type)||regexLore(armorSets.get(name).regex,item)){
                        map.put(name,map.get(name)+1);
                    }
                }
            }
            map.forEach((String name,Integer count)->{
                if(count>0){
                    ArmorSet armorSet = armorSets.get(name);
                    for (int i=count;i>0;i--){
                        if(armorSet.potions.containsKey(i)){
                            effects.addAll(armorSet.getPotions().get(i));
                        }
                    }
                }
            });
        }
        Potions.add(player,effects);
    }
    private static boolean containLore(String target,ItemStack item){
        if(item.getItemMeta().getLore()==null){
            return false;
        }
        for (String lore : item.getItemMeta().getLore()){
            if (lore.contains(target)){
                return true;
            }
        }
        return false;
    }
    private static boolean regexLore(String regex,ItemStack item){
        if(regex==null){
            return false;
        }
        Pattern pattern = Pattern.compile(regex);
        if(item.getItemMeta().getLore()==null){
            return false;
        }
        for (String lore:item.getItemMeta().getLore()){
            Matcher matcher = pattern.matcher(lore);
            if(matcher.find()){
                return true;
            }
        }
        return false;
    }

    public static ItemStack addArmorSetLore(ItemStack raw,Map<String,Integer> resultMap){
        for (String name:resultMap.keySet()){
            ArmorSet armorSet = armorSets.get(name);
            if(containLore(armorSet.lore,raw)||regexLore(armorSet.regex,raw)||NMSUtils.checkTag(raw,"XgpES_Set",armorSets.get(name).type)){
                MyItemBuilder item = new MyItemBuilder(raw);
                List<String> newLore = new ArrayList<>();
                int amount = resultMap.get(name);
                armorSet.loreName.forEach(s -> newLore.add(s.replace("{n}",String.valueOf(amount))));
                for(int i:armorSet.loreActive.keySet()){
                    if(i<=amount){
                        newLore.addAll(armorSet.loreActive.get(i));
                    }
                }
                for(int i:armorSet.loreInactive.keySet()){
                    if(i>amount){
                        newLore.addAll(armorSet.loreInactive.get(i));
                    }
                }
                item.addLore(newLore);
                return item.getItem();
            }
        }
        return raw;
    }
}
