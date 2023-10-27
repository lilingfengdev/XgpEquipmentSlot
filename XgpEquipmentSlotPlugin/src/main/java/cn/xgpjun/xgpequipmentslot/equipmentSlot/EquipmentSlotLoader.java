package cn.xgpjun.xgpequipmentslot.equipmentSlot;

import cn.xgpjun.xgpequipmentslot.XgpEquipmentSlot;
import cn.xgpjun.xgpequipmentslot.utils.Message;
import cn.xgpjun.xgpequipmentslot.utils.MyItemBuilder;
import dev.lone.itemsadder.api.CustomStack;
import io.lumine.mythic.api.MythicProvider;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import pers.neige.neigeitems.manager.ItemManager;

import java.io.File;
import java.util.*;

public class EquipmentSlotLoader {
    //物品库
    static List<String> depends = Arrays.asList("ia","ni","mm");
    static boolean ia;
    static boolean ni;
    static boolean mm;
    //读取EquipmentSlot文件夹中的文件信息。
    public static void loadEquipmentSlot(){
        Bukkit.getScheduler().runTaskLater(XgpEquipmentSlot.getInstance(),EquipmentSlotLoader::load,100L);
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
        ia = Bukkit.getPluginManager().getPlugin("ItemsAdder")!=null;
        ni = Bukkit.getPluginManager().getPlugin("NeigeItems")!=null;
        mm = Bukkit.getPluginManager().getPlugin("MythicMobs")!=null;
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
            Map<String, GuiItem> guiItems = new HashMap<>();
            Map<String, Slot> equipmentSlot = new HashMap<>();
            Map<Integer,Slot> slotInfo = new HashMap<>();
            for (String key:keys){
                ConfigurationSection item = items.getConfigurationSection(key);
                if (item == null)
                    continue;
                MyItemBuilder guiItem =null;
                String depend = getDepend(item);
                if(depend!=null){
                    switch (depend){
                        case "ia": {
                            if(ia){
                                CustomStack stack = CustomStack.getInstance(item.getString("ia"));
                                if(stack != null) {
                                    ItemStack itemStack = stack.getItemStack();
                                    guiItem = new MyItemBuilder(itemStack);
                                }
                            }
                            break;
                        }
                        case "ni":{
                            if(ni){
                                guiItem = new MyItemBuilder(ItemManager.INSTANCE.getItemStack(item.getString("ni")));
                            }
                            break;
                        }
                        case "mm":{
                            if(mm){
                                guiItem = new MyItemBuilder( MythicProvider.get().getItemManager().getItem(item.getString("mm")).get().getCachedBaseItem());
                            }
                            break;
                        }
                    }
                } else{
                    Material material = Material.getMaterial(item.getString("material","STONE"));
                    if(material==null)
                        material = Material.STONE;
                    guiItem = new MyItemBuilder(material,item.getInt("amount",1), (byte) item.getInt("damage",0));
                    if(guiItem.getItem().getType().equals(Material.AIR))
                        continue;
                    String displayName = item.getString("displayName");
                    if(displayName!=null){
                        guiItem.setDisplayName(ChatColor.translateAlternateColorCodes('&',displayName));
                    }
                    List<String> lore = item.getStringList("lore");
                    if(!lore.isEmpty()){
                        lore.replaceAll(textToTranslate -> ChatColor.translateAlternateColorCodes('&', textToTranslate));
                        guiItem.setLore(lore);
                    }
                }

                guiItems.put(key,new GuiItem(guiItem==null? MyItemBuilder.getMissingItem():guiItem.getItem(),item.getStringList("command"),item.getString("permission")));

                if(item.get("equipmentSlot")!=null){
                    String type = item.getString("equipmentSlot.type");
                    String lore2 = item.getString("equipmentSlot.lore");
                    String regex = item.getString("equipmentSlot.regex");
                    Map<String,String> other = new HashMap<>();
                    if (item.get("equipmentSlot.other")!=null){
                        for (String s:item.getKeys(false)){
                            other.put(s,item.getString("equipmentSlot.other."+s));
                        }
                    }
                    equipmentSlot.put(key,new Slot(type,lore2,regex,other));
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
    private static String getDepend(ConfigurationSection section){
        for (String depend:depends){
            if(section.contains(depend)){
                return depend;
            }
        }
        return null;
    }
}
