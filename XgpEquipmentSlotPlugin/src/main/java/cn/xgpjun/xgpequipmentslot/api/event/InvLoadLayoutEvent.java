package cn.xgpjun.xgpequipmentslot.api.event;

import cn.xgpjun.xgpequipmentslot.gui.impl.EquipmentSlotInventory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class InvLoadLayoutEvent extends Event {

    private static final HandlerList handlerList = new HandlerList();
    private UUID target;

    private EquipmentSlotInventory inventory;
    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
    public String getEquipmentSlotName(){
        return inventory.getEquipmentSlot().getName();
    }
}
