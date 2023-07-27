package cn.xgpjun.xgpequipmentslot.Command.Sub;

import cn.xgpjun.xgpequipmentslot.Utils.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Help implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender.isOp()){
            sender.sendMessage(Message.title1);
            sender.sendMessage(Message.nbt1);
            sender.sendMessage(Message.nbt2);
            sender.sendMessage(Message.openInv1);
            sender.sendMessage(Message.openInv2);
            sender.sendMessage(Message.openInv3);
            sender.sendMessage(Message.openInv4);
            sender.sendMessage(Message.openInv5);
            sender.sendMessage(Message.openInv6);
            sender.sendMessage(Message.reload1);
            sender.sendMessage(Message.reload2);
            sender.sendMessage(Message.tag1);
            sender.sendMessage(Message.tag2);
            sender.sendMessage(Message.tag3);
            sender.sendMessage(Message.tag4);
        }else {
            sender.sendMessage(Message.title1);
            sender.sendMessage(Message.openInv1);
            sender.sendMessage(Message.openInv2);
            sender.sendMessage(Message.openInv3);
            sender.sendMessage(Message.openInv4);
            sender.sendMessage(Message.openInv5);
            sender.sendMessage(Message.openInv6);
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return new ArrayList<>();
    }
}
