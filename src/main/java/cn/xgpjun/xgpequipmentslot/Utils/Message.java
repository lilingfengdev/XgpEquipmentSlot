package cn.xgpjun.xgpequipmentslot.Utils;

import cn.xgpjun.xgpequipmentslot.XgpEquipmentSlot;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Message {
    public static String prefix;
    public static String noFreeSpace;
    public static String title1;
    public static String playerOnly;
    public static String incorrectInput;
    public static String notFound;
    public static String noPermission;
    public static String openInv1;
    public static String openInv2;
    public static String openInv3;
    public static String openInv4;
    public static String openInv5;
    public static String openInv6;
    public static String reload1;
    public static String reload2;
    public static String reloadSuccessfully;
    public static String tag1;
    public static String tag2;
    public static String tag3;
    public static String tag4;
    public static String help1;
    public static String nbt1;
    public static String nbt2;
    public static String nbtCopy;
    public static String nbtLog;
    public static String defaultTitle;
    public static String disableRawEquipmentSlot;

    private static FileConfiguration config;
    public static void init(){
        File LangConfigFile = new File(XgpEquipmentSlot.getInstance().getDataFolder(), "message.yml");
        config = YamlConfiguration.loadConfiguration(LangConfigFile);

        prefix = getString("prefix","&l&b[&6XES装备栏&b]&r");
        title1 = getString("command.title1","&c&m    &e&m    &a&m    &f&b&lXES&6命令提示&c&m    &e&m    &a&m    &f");
        noFreeSpace = getString("noFreeSpace","&c服务端磁盘不足300MB无法进行此操作！ 请联系管理员");
        playerOnly = getString("command.playerOnly","&c这条命令只有玩家可以使用!");
        incorrectInput = getString("command.incorrectInput","&c输入错误喵!");
        notFound = getString("command.notFound","&c未找到该装备栏! 你确定有配置正确吗?");
        noPermission = getString("command.noPermission","&c你没有权限这样做!缺少权限:{permission}");
        openInv1 = getString("command.openInv1","&b/xes openInv <装备栏名称>");
        openInv2 = getString("command.openInv2","&a打开指定名称(name)的装备栏。");
        openInv3 = getString("command.openInv3","&b/xes openInv <装备栏名称> <玩家名称>");
        openInv4 = getString("command.openInv4","&a查看指定玩家的装备栏,记得确认大小写!");
        openInv5 = getString("command.openInv5","&b/xes openInv");
        openInv6 = getString("command.openInv6","&a打开默认装备栏");
        reload1 = getString("command.reload1","&b/xes reload");
        reload2 = getString("command.reload2","&a重载插件~ 请不要使用plugman启用捏!");
        reloadSuccessfully = getString("command.reloadSuccessfully","&a重载成功~(如果后台没报错的话)");
        tag1 = getString("command.tag1","&b/xes tag add <tag>");
        tag2 = getString("command.tag2","&a手持物品,向手中的物品添加tag,用于配置中的检验.");
        tag3 = getString("command.tag3","&b/xes tag remove");
        tag4 = getString("command.tag4","&a手持物品,移除本插件添加的tag");
        help1 = getString("command.help1","&c/xes help 获得帮助!");
        nbt1 = getString("command.nbt1","&b/xes nbt");
        nbt2 = getString("command.nbt2","&a获得手中物品的nbt.");
        nbtCopy = getString("command.nbtCopy","&l&b[&6点击复制nbt信息&b]&r");
        nbtLog = getString("command.nbtLog","&6成功记录在插件文件夹中的nbt.txt中喵!");
        defaultTitle = getString("defaultTitle","&7无标题");
        disableRawEquipmentSlot = getString("disableRawEquipmentSlot","&c本服禁用了原版装备栏!");
    }

    static String getString(String path,String def){
        return ChatColor.translateAlternateColorCodes('&',config.getString(path,def));
    }


}
