package cn.xgpjun.xgpequipmentslot.Pojo;

import lombok.Data;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;

@Data
public class PlayerSlotInfo {
    private UUID player;
    private String name;
    private Map<Integer, ItemStack> equipments;

}
