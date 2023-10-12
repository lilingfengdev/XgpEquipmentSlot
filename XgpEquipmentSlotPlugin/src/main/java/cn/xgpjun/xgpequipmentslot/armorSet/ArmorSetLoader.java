package cn.xgpjun.xgpequipmentslot.armorSet;

import cn.xgpjun.xgpequipmentslot.XgpEquipmentSlot;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArmorSetLoader {
    public static void loadArmorSet(){
        Bukkit.getScheduler().runTaskAsynchronously(XgpEquipmentSlot.getInstance(),ArmorSetLoader::load);
    }
    private static void load(){
        ArmorSet.armorSets.clear();
        File armorSetFolder = new File(XgpEquipmentSlot.getInstance().getDataFolder(), "ArmorSet");
        if (!armorSetFolder.exists() || !armorSetFolder.isDirectory()) {
            return;
        }
        File[] yamlFiles = armorSetFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".yml"));
        if (yamlFiles == null) {
            XgpEquipmentSlot.warning("No YAML files found in ArmorSet folder.");
            return;
        }
        for(File yamlFile:yamlFiles){
            YamlConfiguration yaml = YamlConfiguration.loadConfiguration(yamlFile);
            String name = yaml.getString("name");
            String lore = yaml.getString("decision.lore");
            String type = yaml.getString("decision.type");
            String regex = yaml.getString("decision.regex");
            if(name==null||(lore==null&&type==null)||ArmorSet.armorSets.containsKey(name)){
                continue;
            }
            List<String> display = yaml.getStringList("display");
            Map<Integer,List<String>> attributes = new HashMap<>();
            ConfigurationSection attribute = yaml.getConfigurationSection("bonus.attribute");
            if (attribute != null) {
                for (String amount: attribute.getKeys(false)){
                    attributes.put(Integer.valueOf(amount),attribute.getStringList(amount));
                }
            }
            Map<Integer,List<PotionEffect>> potions = new HashMap<>();
            ConfigurationSection potion = yaml.getConfigurationSection("bonus.potion");
            if (potion != null) {
                for (String amount: potion.getKeys(false)){
                    potions.put(Integer.valueOf(amount),convert(potion.getStringList(amount)));
                }
            }
            ConfigurationSection l = yaml.getConfigurationSection("lore");
            List<String> loreName = new ArrayList<>();
            Map<Integer,List<String>> loreActive = new HashMap<>();
            Map<Integer,List<String>> loreInActive = new HashMap<>();
            if (l != null) {
                loreName = l.getStringList("name");
                loreName = color(loreName);
                ConfigurationSection la = l.getConfigurationSection("active");
                if(la != null){
                    for (String amount:la.getKeys(false)){
                        loreActive.put(Integer.valueOf(amount),color(la.getStringList(amount)));
                    }
                }
                ConfigurationSection lia = l.getConfigurationSection("inactive");
                if(lia != null){
                    for (String amount:lia.getKeys(false)){
                        loreInActive.put(Integer.valueOf(amount),color(lia.getStringList(amount)));
                    }
                }
            }
            new ArmorSet(name,lore,type,regex,display,attributes,potions,loreName,loreActive,loreInActive).register();
            XgpEquipmentSlot.log("成功读取套装效果:"+yamlFile.getName()+" | name="+name);
        }
    }

    private static List<PotionEffect> convert(List<String> list){
        List<PotionEffect> result = new ArrayList<>();
        for (String s:list){
            String[] strings = s.split(":");
            String type = strings[0];
            int level = Integer.parseInt(strings[1])-1;
            if(PotionEffectType.getByName(type)!=null){
                result.add(new PotionEffect(PotionEffectType.getByName(type),100,level));
            }
        }
        return result;
    }

    private static List<String> color(List<String> l){
        List<String> result = new ArrayList<>();
        l.forEach((s -> result.add(ChatColor.translateAlternateColorCodes('&',s))));
        return result;
    }
}
