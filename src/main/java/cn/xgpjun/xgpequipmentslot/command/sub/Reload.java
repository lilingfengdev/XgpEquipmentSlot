package cn.xgpjun.xgpequipmentslot.command.sub;

import cn.xgpjun.xgpequipmentslot.XgpEquipmentSlot;
import cn.xgpjun.xgpequipmentslot.utils.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Reload implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!sender.isOp())
            return false;
        if(args.length!=1){
            sender.sendMessage(Message.prefix+Message.incorrectInput);
            sender.sendMessage(Message.reload1);
            sender.sendMessage(Message.reload2);
            return true;
        }
        XgpEquipmentSlot.reload();
        sender.sendMessage(Message.prefix+Message.reloadSuccessfully);
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return new ArrayList<>();
    }
}
