package cn.xgpjun.xgpequipmentslot.Utils;

import cn.xgpjun.xgpequipmentslot.Database.DataManager;
import cn.xgpjun.xgpequipmentslot.XgpEquipmentSlot;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;

public class ConfigSetting {
    public static String version;
    public static String depend;
    public static boolean mysql;
    public static String sqlite;
    public static boolean redis;
    public static boolean checkNewVersion;
    public static boolean disableRawEquipmentSlot;
    public static String defaultEquipmentSlot;
    public static void load(Configuration config){
        version=config.getString("version","1.0.0");
        depend= config.getString("depend","SX");
        mysql = config.getBoolean("mysql",false);
        sqlite = config.getString("sqlite","");
        redis = config.getBoolean("redis",false);
        checkNewVersion = config.getBoolean("checkNewVersion",true);
        disableRawEquipmentSlot = config.getBoolean("disableRawEquipmentSlot",false);
        defaultEquipmentSlot = config.getString("defaultEquipmentSlot","default");
        if(checkNewVersion){
            Bukkit.getScheduler().runTaskAsynchronously(XgpEquipmentSlot.getInstance(), NewVersionUtils::checkNewVersion);
        }
    }
    public static void checkDepend(){
        if(!depend.equals("SX") &&!depend.equals("AP") ||(Bukkit.getPluginManager().getPlugin("SX-Attribute")==null&&Bukkit.getPluginManager().getPlugin("AttributePlus")==null)) {
            XgpEquipmentSlot.warning("depend填写错误或未检测到对应插件!");
            Bukkit.getPluginManager().disablePlugin(XgpEquipmentSlot.getInstance());
        }
        if(Bukkit.getPluginManager().getPlugin("SX-Attribute")!=null)
            depend = "SX";
        if(Bukkit.getPluginManager().getPlugin("AttributePlus")!=null)
            depend = "AP";
        XgpEquipmentSlot.getInstance().getConfig().set("depend",depend);
        XgpEquipmentSlot.getInstance().saveConfig();
    }
    public static void update(){
        if(getVersionToInt(version)==100){
            DataManager.dropPrimaryKey();
        }
        version = XgpEquipmentSlot.getInstance().getDescription().getVersion();
        XgpEquipmentSlot.getInstance().getConfig().set("version",version);
        XgpEquipmentSlot.getInstance().saveConfig();
    }
    static int getVersionToInt(String version){
        return Integer.parseInt(version.split("\\.")[0])*100+Integer.parseInt(version.split("\\.")[1])*10+Integer.parseInt(version.split("\\.")[2]);
    }

}
