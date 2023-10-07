package cn.xgpjun.xgpequipmentslot.apiWrapper;

import cn.xgpjun.xgpequipmentslot.api.AttributeAPIWrapper;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BlankAPIWrapper implements AttributeAPIWrapper {
    @Override
    public boolean isUse(LivingEntity entity, ItemStack item) {
        return false;
    }

    @Override
    public void updateAttribute(Player player) {

    }
}
