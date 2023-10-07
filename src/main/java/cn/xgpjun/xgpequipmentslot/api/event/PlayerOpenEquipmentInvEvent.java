package cn.xgpjun.xgpequipmentslot.api.event;

import cn.xgpjun.xgpequipmentslot.gui.impl.EquipmentSlotInventory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class PlayerOpenEquipmentInvEvent extends Event implements Cancellable {
    private static final HandlerList handlerList = new HandlerList();
    private boolean cancelled;

    private Player player;
    private UUID target;

    private EquipmentSlotInventory inventory;
    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }
    public String getEquipmentSlotName(){
        return inventory.getEquipmentSlot().getName();
    }
}
