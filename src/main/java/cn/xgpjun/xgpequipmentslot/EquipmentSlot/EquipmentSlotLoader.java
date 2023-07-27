package cn.xgpjun.xgpequipmentslot.EquipmentSlot;

import cn.xgpjun.xgpequipmentslot.Utils.Message;
import cn.xgpjun.xgpequipmentslot.Utils.MyItem;
import cn.xgpjun.xgpequipmentslot.Utils.NMSUtils;
import cn.xgpjun.xgpequipmentslot.XgpEquipmentSlot;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EquipmentSlotLoader {
    //读取EquipmentSlot文件夹中的文件信息。
    public static void loadEquipmentSlot(){
        Bukkit.getScheduler().runTaskAsynchronously(XgpEquipmentSlot.getInstance(),EquipmentSlotLoader::load);
    }

    private static void load(){
        EquipmentSlot.equipmentSlots.clear();
        // 子文件夹EquipmentSlot
        File equipmentSlotFolder = new File(XgpEquipmentSlot.getInstance().getDataFolder(), "EquipmentSlot");
        // 检查子文件夹是否存在
        if (!equipmentSlotFolder.exists() || !equipmentSlotFolder.isDirectory()) {
            return;
        }
        File[] yamlFiles = equipmentSlotFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".yml"));
        if (yamlFiles == null) {
            XgpEquipmentSlot.warning("No YAML files found in EquipmentSlot folder.");
            return;
        }
        for(File yamlFile:yamlFiles){
            YamlConfiguration yaml = YamlConfiguration.loadConfiguration(yamlFile);
            String name = yaml.getString("name");
            boolean permission = yaml.getBoolean("permission",false);
            String title = ChatColor.translateAlternateColorCodes('&',yaml.getString("GUI.title", Message.defaultTitle));
            List<String> layout = yaml.getStringList("GUI.layout");
            ConfigurationSection items = yaml.getConfigurationSection("items");
            if(items==null||name==null||layout.isEmpty())
                continue;
            Collection<String> keys = items.getKeys(false);
            Map<String, ItemStack> guiItems = new HashMap<>();
            Map<String, Slot> equipmentSlot = new HashMap<>();
            Map<Integer,Slot> slotInfo = new HashMap<>();
            for (String key:keys){
                ConfigurationSection item = items.getConfigurationSection(key);
                if (item == null)
                    continue;
                MyItem guiItem;
                String nbt = item.getString("nbt");
                if(nbt!=null){
                    guiItem = new MyItem(NMSUtils.toItem(nbt));
                } else{
                    Material material = Material.getMaterial(item.getString("material","STONE"));
                    if(material==null)
                        material = Material.STONE;
                    guiItem = new MyItem(material,item.getInt("amount",1), (byte) item.getInt("damage",0));
                }
                String displayName = item.getString("displayName");
                if(displayName!=null){
                    guiItem.setDisplayName(ChatColor.translateAlternateColorCodes('&',displayName));
                }
                List<String> lore = item.getStringList("lore");
                if(!lore.isEmpty()){
                    lore.replaceAll(textToTranslate -> ChatColor.translateAlternateColorCodes('&', textToTranslate));
                    guiItem.setLore(lore);
                }
                guiItems.put(key,guiItem.getItem());
                if(item.get("equipmentSlot")!=null){
                    String type = item.getString("equipmentSlot.type");
                    String lore2 = item.getString("equipmentSlot.lore");
                    equipmentSlot.put(key,new Slot(type,lore2));
                }
            }
            for(int i=0;i<layout.size()*9;i++){
                int row = i/9;
                int col = i%9;
                String c = String.valueOf(layout.get(row).charAt(col));
                if(equipmentSlot.containsKey(c)){
                    slotInfo.put(i,equipmentSlot.get(c));
                }
            }
            new EquipmentSlot(name,permission,title,layout,guiItems,slotInfo).addToGlobal();
            XgpEquipmentSlot.log("成功读取配置:"+yamlFile.getName()+" | name="+name);

        }
    }
}
