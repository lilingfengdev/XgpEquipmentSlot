package cn.xgpjun.xgpequipmentslot.Command.Sub;

import cn.xgpjun.xgpequipmentslot.Command.MainCommand;
import cn.xgpjun.xgpequipmentslot.Utils.Message;
import cn.xgpjun.xgpequipmentslot.Utils.NMSUtils;
import cn.xgpjun.xgpequipmentslot.Utils.VersionAdapterUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class Tag implements TabExecutor {
    /*
    xes tag add <tag>
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

        if((args.length!=2&&args.length!=3)||(!args[1].equalsIgnoreCase("add")&&!args[1].equalsIgnoreCase("remove"))){
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
        if(args[1].equalsIgnoreCase("add")&&args.length==3){
            VersionAdapterUtils.setItemInMainHand(player,NMSUtils.addTag(itemStack,args[2]));
            return true;
        }
        if(args[1].equalsIgnoreCase("remove")){
            VersionAdapterUtils.setItemInMainHand(player,NMSUtils.removeTag(itemStack));
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if(args.length == 2){
            return MainCommand.filter(new ArrayList<>(Arrays.asList("add", "remove")), args);
        }
        if(args.length == 3&&args[1].equals("add")){
            return Collections.singletonList("<tags>");
        }
        return new ArrayList<>();
    }
}
