package cn.xgpjun.xgpequipmentslot.utils;

import cn.xgpjun.xgpequipmentslot.XgpEquipmentSlot;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class RawEquipmentSlot {
    public static void startEquipmentCheckTask() throws Throwable{
        // 创建一个定时任务，每隔一段时间检查玩家的装备栏
        Bukkit.getScheduler().runTaskTimerAsynchronously(XgpEquipmentSlot.getInstance(),()->{
            for(Player player: Bukkit.getOnlinePlayers()){
                if(player.hasPermission("XgpES.bypass"))
                    continue;
                ItemStack[] equipment = player.getInventory().getArmorContents();
                boolean save = false;
                for (int i=0;i<equipment.length;i++){
                    ItemStack item = equipment[i];
                    if(item!=null){
                        Bukkit.getScheduler().runTask(XgpEquipmentSlot.getInstance(),()->GiveItemsUtils.giveItem(player,item));
                        player.sendMessage(Message.prefix+Message.disableRawEquipmentSlot);
                        equipment[i]=null;
                        save = true;
                    }
                }
                if (save)
                    Bukkit.getScheduler().runTask(XgpEquipmentSlot.getInstance(),()->player.getInventory().setArmorContents(equipment));
            }
        },20L,20L);
    }
}
