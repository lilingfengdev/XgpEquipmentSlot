package cn.xgpjun.xgpequipmentslot.equipmentSlot;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Data
@AllArgsConstructor
public class GuiItem {
    private ItemStack itemStack;
    private List<String> commandList;
    private String permission;
}
