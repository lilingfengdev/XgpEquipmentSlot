package cn.xgpjun.xgpequipmentslot.api;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface AttributeAPIWrapper {
    boolean isUse(LivingEntity entity, ItemStack item);
    void updateAttribute(Player player);
}
