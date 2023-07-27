package cn.xgpjun.xgpequipmentslot.Command;

import cn.xgpjun.xgpequipmentslot.Command.Sub.*;
import cn.xgpjun.xgpequipmentslot.Utils.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class MainCommand implements TabExecutor {

    private final Map<String, TabExecutor> subCommands = new HashMap<>();

    public MainCommand(){
        registerSubCommand("tag",new Tag());
        registerSubCommand("help",new Help());
        registerSubCommand("openInv",new OpenInv());
        registerSubCommand("reload",new Reload());
        registerSubCommand("nbt",new Nbt());
    }
    private void registerSubCommand(String subCommand, TabExecutor executor) {
        subCommands.put(subCommand.toLowerCase(), executor);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length > 0) {
            String subCommand = args[0].toLowerCase();
            TabExecutor executor = subCommands.get(subCommand);
            if (executor != null) {
                return executor.onCommand(sender, command, label, args);
            }
        }else
            return subCommands.get("help").onCommand(sender, command, label, args);

        sender.sendMessage(Message.prefix+Message.help1);
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> list = new ArrayList<>(subCommands.keySet());
        if(sender.isOp()){
            if(args.length==1){
                return filter(list,args);
            }else {
                String subCommand = args[0].toLowerCase();
                TabExecutor executor = subCommands.get(subCommand);
                if (executor != null) {
                    return executor.onTabComplete(sender, command, alias, args);
                }
            }
        }else {
            if(args.length==1){
                List<String> list2 = Arrays.asList("openInv","help");
                return filter(list2,args);
            }else {
                return new ArrayList<>();
            }
        }
        return null;
    }
    public static List<String> filter(List<String> list, String[] args) {
        String latest = null;
        if (args.length != 0) {
            latest = args[args.length - 1];
        }
        if (list.isEmpty() || latest == null)
            return list;
        String ll = latest.toLowerCase();
        List<String> filteredList = new ArrayList<>(list);
        filteredList.removeIf(k -> !k.toLowerCase().startsWith(ll));
        return filteredList;
    }
}
