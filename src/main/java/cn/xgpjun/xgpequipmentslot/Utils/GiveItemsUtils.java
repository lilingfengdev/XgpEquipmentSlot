package cn.xgpjun.xgpequipmentslot.Utils;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class GiveItemsUtils {
    public static void giveItem(Player player , ItemStack itemStack){
        Map<Integer, ItemStack> map = player.getInventory().addItem(itemStack);
        if(map.isEmpty())
            return;
        for(ItemStack item : map.values()){
            player.getWorld().dropItemNaturally(player.getLocation(),item);
        }
    }
}
