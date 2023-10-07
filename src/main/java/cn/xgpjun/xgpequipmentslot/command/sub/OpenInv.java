package cn.xgpjun.xgpequipmentslot.command.sub;

import cn.xgpjun.xgpequipmentslot.XgpEquipmentSlot;
import cn.xgpjun.xgpequipmentslot.api.event.PlayerOpenEquipmentInvEvent;
import cn.xgpjun.xgpequipmentslot.command.MainCommand;
import cn.xgpjun.xgpequipmentslot.equipmentSlot.EquipmentSlot;
import cn.xgpjun.xgpequipmentslot.gui.impl.EquipmentSlotInventory;
import cn.xgpjun.xgpequipmentslot.utils.ConfigSetting;
import cn.xgpjun.xgpequipmentslot.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class OpenInv implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage(Message.prefix+Message.playerOnly);
            return true;
        }
        if(args.length!=1&&args.length!=2&&args.length!=3){
            sender.sendMessage(Message.prefix+Message.incorrectInput);
            sender.sendMessage(Message.openInv1);
            sender.sendMessage(Message.openInv2);
            sender.sendMessage(Message.openInv3);
            sender.sendMessage(Message.openInv4);
            sender.sendMessage(Message.openInv5);
            sender.sendMessage(Message.openInv6);
            return true;
        }
        Player player = (Player) sender;
        if(args.length==1){
            String def = ConfigSetting.defaultEquipmentSlot;
            if(!EquipmentSlot.getEquipmentSlots().containsKey(def)){
                sender.sendMessage(Message.prefix+Message.notFound);
                return true;
            }
            EquipmentSlot equipmentSlot = EquipmentSlot.getEquipmentSlots().get(def);
            openGui(player,player,equipmentSlot);
            return true;
        }
        String name = args[1];
        if(!EquipmentSlot.getEquipmentSlots().containsKey(name)){
            sender.sendMessage(Message.prefix+Message.notFound);
            return true;
        }
        EquipmentSlot equipmentSlot = EquipmentSlot.getEquipmentSlots().get(name);
        if(args.length==3){
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[2]);
            openGui(player,offlinePlayer,equipmentSlot);
            return true;
        }

        if(equipmentSlot.isPermission()&&!player.hasPermission("XgpEquipmentSlot.open."+equipmentSlot.getName())){
            sender.sendMessage(Message.prefix+Message.noPermission.replace("{permission}","XgpEquipmentSlot.open."+equipmentSlot.getName()));
            return true;
        }
        openGui(player,player,equipmentSlot);
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if(args.length == 2){
            return MainCommand.filter(new ArrayList<>(EquipmentSlot.getEquipmentSlots().keySet()),args);
        }
        if (args.length == 3){
            return null;
        }
        return new ArrayList<>();
    }

    private static void openGui(Player player,OfflinePlayer target,EquipmentSlot equipmentSlot){
        Bukkit.getScheduler().runTaskAsynchronously(XgpEquipmentSlot.getInstance(),()->{
            EquipmentSlotInventory inventory = equipmentSlot.getGui(target);
            PlayerOpenEquipmentInvEvent event = new PlayerOpenEquipmentInvEvent(false,player,target.getUniqueId(),inventory);
            Bukkit.getScheduler().runTask(XgpEquipmentSlot.getInstance(),()->Bukkit.getPluginManager().callEvent(event));
            if(!event.isCancelled()){
                Bukkit.getScheduler().runTask(XgpEquipmentSlot.getInstance(),()->player.openInventory(inventory.getInventory()));
            }
        });
    }
}
