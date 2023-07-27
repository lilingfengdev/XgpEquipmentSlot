package cn.xgpjun.xgpequipmentslot.Utils;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class VersionAdapterUtils {
    public static boolean ifMainHand(PlayerInteractEvent e){
        if(NMSUtils.versionToInt<9)
            return true;
        else
            return EquipmentSlot.HAND.equals(e.getHand());
    }
    public static void setItemInMainHand(Player player, ItemStack itemStack){
        if(NMSUtils.versionToInt<9){
            player.setItemInHand(itemStack);
        }else {
            player.getInventory().setItemInMainHand(itemStack);
        }
    }

    public static ItemStack getItemInMainHand(Player player){
        if(NMSUtils.versionToInt<9)
            return player.getItemInHand();
        else
            return player.getInventory().getItemInMainHand();
    }
}
