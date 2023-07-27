package cn.xgpjun.xgpequipmentslot.ArmorSet;

import cn.xgpjun.xgpequipmentslot.XgpEquipmentSlot;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
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
            if(name==null||(lore==null&&type==null)||ArmorSet.armorSets.containsKey(name)){
                continue;
            }
            List<String> display = yaml.getStringList("display");
            Map<Integer,List<String>> attributes = new HashMap<>();
            ConfigurationSection attribute = yaml.getConfigurationSection("bonus.attribute");
            for (String amount: attribute.getKeys(false)){
                attributes.put(Integer.valueOf(amount),attribute.getStringList(amount));
            }
            new ArmorSet(name,lore,type,display,attributes).add();
            XgpEquipmentSlot.log("成功读取套装效果:"+yamlFile.getName()+" | name="+name);
        }
    }
}
