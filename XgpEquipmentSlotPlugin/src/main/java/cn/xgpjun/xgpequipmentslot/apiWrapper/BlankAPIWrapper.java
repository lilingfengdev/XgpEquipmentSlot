package cn.xgpjun.xgpequipmentslot.apiWrapper;

import cn.xgpjun.xgpequipmentslot.api.AttributeAPIWrapper;
import cn.xgpjun.xgpequipmentslot.equipmentSlot.AttributeManager;
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

    @Override
    public void register() {
        AttributeManager.setAttributeAPI(new BlankAPIWrapper());
    }

    @Override
    public String getAuthor() {
        return "xgpjun";
    }

    @Override
    public String getName() {
        return "None";
    }

    @Override
    public String getVersion() {
        return "?.?";
    }
}
