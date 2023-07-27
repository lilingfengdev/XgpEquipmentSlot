package cn.xgpjun.xgpequipmentslot.Command.Sub;

import cn.xgpjun.xgpequipmentslot.Command.MainCommand;
import cn.xgpjun.xgpequipmentslot.EquipmentSlot.EquipmentSlot;
import cn.xgpjun.xgpequipmentslot.Utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
        if(args.length!=2&&args.length!=3){
            sender.sendMessage(Message.prefix+Message.incorrectInput);
            sender.sendMessage(Message.openInv1);
            sender.sendMessage(Message.openInv2);
            sender.sendMessage(Message.openInv3);
            sender.sendMessage(Message.openInv4);
            return true;
        }
        String name = args[1];
        if(!EquipmentSlot.getEquipmentSlots().containsKey(name)){
            sender.sendMessage(Message.prefix+Message.notFound);
            return true;
        }
        Player player = (Player) sender;
        EquipmentSlot equipmentSlot = EquipmentSlot.getEquipmentSlots().get(name);
        if(args.length==3){
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[2]);
            player.openInventory(equipmentSlot.getGui(offlinePlayer));
            return true;
        }

        if(equipmentSlot.isPermission()&&!player.hasPermission("XgpEquipmentSlot.open."+equipmentSlot.getName())){
            sender.sendMessage(Message.prefix+Message.noPermission.replace("{permission}","XgpEquipmentSlot.open."+equipmentSlot.getName()));
            return true;
        }
        player.openInventory(equipmentSlot.getGui(player));
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
}
