package cn.xgpjun.xgpequipmentslot.Command.Sub;

import cn.xgpjun.xgpequipmentslot.Utils.Message;
import cn.xgpjun.xgpequipmentslot.Utils.NMSUtils;
import cn.xgpjun.xgpequipmentslot.Utils.VersionAdapterUtils;
import cn.xgpjun.xgpequipmentslot.XgpEquipmentSlot;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Nbt implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage(Message.prefix+Message.playerOnly);
            return true;
        }
        if(!sender.isOp())
            return false;
        if(args.length!=1){
            sender.sendMessage(Message.prefix+Message.incorrectInput);
            sender.sendMessage(Message.nbt1);
            sender.sendMessage(Message.nbt2);
            return true;
        }
        Player player = (Player) sender;
        ItemStack itemStack = VersionAdapterUtils.getItemInMainHand(player);
        if(itemStack==null||itemStack.getItemMeta()==null){
            return true;
        }
        String nbt = NMSUtils.toNBTString(itemStack);
        TextComponent prefix = new TextComponent(Message.prefix);
        TextComponent text = new TextComponent(Message.nbtCopy);
        text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new BaseComponent[]{new TextComponent(nbt)}));
        try{
            text.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD,nbt));
            prefix.addExtra(text);
            player.spigot().sendMessage(prefix);
        }catch (NoSuchFieldError e){
            File log = new File(XgpEquipmentSlot.getInstance().getDataFolder(), "nbt.txt");
            if (!log.exists()) {
                try {
                    log.createNewFile();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(log, true))) {
                writer.newLine(); //回车
                writer.write(getName(itemStack));
                writer.newLine();
                writer.write(nbt);
                player.sendMessage(Message.prefix+Message.nbtLog);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return new ArrayList<>();
    }
    public static String getName(ItemStack itemStack){
        if(Objects.requireNonNull(itemStack.getItemMeta()).hasDisplayName())
            return itemStack.getItemMeta().getDisplayName();

        return itemStack.getType().name();
    }
}
