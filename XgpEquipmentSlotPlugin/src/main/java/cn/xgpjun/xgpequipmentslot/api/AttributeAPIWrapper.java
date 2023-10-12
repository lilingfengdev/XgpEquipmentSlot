package cn.xgpjun.xgpequipmentslot.api;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("unused")
public interface AttributeAPIWrapper {
    boolean isUse(LivingEntity entity, ItemStack item);
    void updateAttribute(Player player);

    void register();
    String getAuthor();
    String getName();
    String getVersion();
}
