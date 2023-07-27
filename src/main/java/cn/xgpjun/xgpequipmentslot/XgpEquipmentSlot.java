package cn.xgpjun.xgpequipmentslot;

import cn.xgpjun.xgpequipmentslot.ArmorSet.ArmorSetLoader;
import cn.xgpjun.xgpequipmentslot.Command.MainCommand;
import cn.xgpjun.xgpequipmentslot.Database.DataManager;
import cn.xgpjun.xgpequipmentslot.EquipmentSlot.EquipmentSlotLoader;
import cn.xgpjun.xgpequipmentslot.Listener.InventoryListener;
import cn.xgpjun.xgpequipmentslot.Listener.PlayerListener;
import cn.xgpjun.xgpequipmentslot.Utils.ConfigSetting;
import cn.xgpjun.xgpequipmentslot.Utils.Message;
import cn.xgpjun.xgpequipmentslot.Utils.RawEquipmentSlot;
import cn.xgpjun.xgpequipmentslot.bStats.Metrics;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;

public final class XgpEquipmentSlot extends JavaPlugin {

    @Getter
    private static JavaPlugin instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        Metrics.enable();
        if (!new File(getDataFolder(), "EquipmentSlot").exists()) {
            saveResource("EquipmentSlot/default.yml", false);
        }
        if (!new File(getDataFolder(), "ArmorSet").exists()) {
            saveResource("ArmorSet/default.yml", false);
        }
        if (!new File(getDataFolder(), "database.yml").exists()) {
            saveResource("database.yml", false);
        }
        if (!new File(getDataFolder(), "message.yml").exists()) {
            saveResource("message.yml", false);
        }
        if (!new File(getDataFolder(), "config.yml").exists()) {
            saveResource("config.yml", false);
        }
        log("§c§m====§e§m====§a§m====");
        log("author: xgpjun");
        log("QQ:357004885");
        log("dont forget me");
        log("§c§m====§e§m====§a§m====");
        Message.init();
        ConfigSetting.load(getConfig());
        DataManager.init();
        ConfigSetting.update();
        Objects.requireNonNull(Bukkit.getPluginCommand("XgpEquipmentSlot")).setExecutor(new MainCommand());
        Bukkit.getPluginManager().registerEvents(new InventoryListener(),this);
        Bukkit.getPluginManager().registerEvents(new PlayerListener(),this);
        EquipmentSlotLoader.loadEquipmentSlot();
        ArmorSetLoader.loadArmorSet();
        ConfigSetting.checkDepend();
        if(ConfigSetting.disableRawEquipmentSlot) {
            try {
                RawEquipmentSlot.startEquipmentCheckTask();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
        Objects.requireNonNull(Bukkit.getPluginCommand("XgpEquipmentSlot")).setExecutor(null);
        Bukkit.getScheduler().cancelTasks(this);
        DataManager.close();
        // Plugin shutdown logic
    }
    public static void reload(){
        DataManager.close();
        if (!new File(getInstance().getDataFolder(), "EquipmentSlot").exists()) {
            getInstance().saveResource("EquipmentSlot/default.yml", false);
        }
        if (!new File(getInstance().getDataFolder(), "database.yml").exists()) {
            getInstance().saveResource("database.yml", false);
        }
        Message.init();
        ConfigSetting.load(getInstance().getConfig());
        DataManager.init();
        EquipmentSlotLoader.loadEquipmentSlot();
        ArmorSetLoader.loadArmorSet();
        log(Message.reloadSuccessfully);
    }

    public static void log(String message) {
        Bukkit.getConsoleSender().sendMessage("§6[XgpEquipmentSlot] " +"§a"+ message);
    }

    public static void warning(String message) {
        Bukkit.getConsoleSender().sendMessage("§e[XgpEquipmentSlot] " +"§e"+ message);
    }

}
