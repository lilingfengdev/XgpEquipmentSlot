package cn.xgpjun.xgpequipmentslot.command.sub;

import cn.xgpjun.xgpequipmentslot.command.MainCommand;
import cn.xgpjun.xgpequipmentslot.utils.Message;
import cn.xgpjun.xgpequipmentslot.utils.NMSUtils;
import cn.xgpjun.xgpequipmentslot.utils.VersionAdapterUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Tag implements TabExecutor {
    /*
    xes tag addEquip <tag>
    xes tag addSet <tag>
    xes tag remove
     */

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!sender.isOp())
            return false;
        if(!(sender instanceof Player)){
            sender.sendMessage(Message.prefix+Message.playerOnly);
            return true;
        }

        if((args.length!=2&&args.length!=3)||(!args[1].equalsIgnoreCase("addSet")&&!args[1].equalsIgnoreCase("addEquip")&&!args[1].equalsIgnoreCase("remove"))){
            sender.sendMessage(Message.prefix+Message.incorrectInput);
            sender.sendMessage(Message.tag1);
            sender.sendMessage(Message.tag2);
            sender.sendMessage(Message.tag3);
            sender.sendMessage(Message.tag4);
            return true;
        }
        Player player = (Player) sender;
        ItemStack itemStack = VersionAdapterUtils.getItemInMainHand(player);
        if(itemStack==null||itemStack.getItemMeta()==null){
            return true;
        }
        if(args[1].equalsIgnoreCase("addEquip")&&args.length==3){
            VersionAdapterUtils.setItemInMainHand(player,NMSUtils.addTag(itemStack,"XgpES",args[2]));
            return true;
        }
        if(args[1].equalsIgnoreCase("addSet")&&args.length==3){
            VersionAdapterUtils.setItemInMainHand(player,NMSUtils.addTag(itemStack,"XgpES_Set",args[2]));
            return true;
        }
        if(args[1].equalsIgnoreCase("remove")){
            VersionAdapterUtils.setItemInMainHand(player,NMSUtils.removeTag(itemStack,"XgpES"));
            VersionAdapterUtils.setItemInMainHand(player,NMSUtils.removeTag(itemStack,"XgpES_Set"));
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if(args.length == 2){
            return MainCommand.filter(new ArrayList<>(Arrays.asList("addEquip","addSet", "remove")), args);
        }
        if(args.length == 3&&(args[1].equals("addEquip")||args[1].equals("addSet"))){
            return Collections.singletonList("<tags>");
        }
        return new ArrayList<>();
    }
}
