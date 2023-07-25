package cn.xgpjun.xgpequipmentslot.Pojo;

import cn.xgpjun.xgpequipmentslot.Gui.Impl.EquipmentSlotInventory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class EquipmentSlot {
    @Getter
    public static Map<String,EquipmentSlot> equipmentSlots = new HashMap<>();
    private String name;
    private boolean permission;
    //GUI
    private String title;
    private List<String> layout;
    private Map<String, ItemStack> guiItems;
    private Map<Integer,Slot> slotInfo;

    public void addToGlobal(){
        equipmentSlots.put(this.name,this);
    }

    public Inventory getGui(){
        return new EquipmentSlotInventory(this).getInventory();
    }

}
