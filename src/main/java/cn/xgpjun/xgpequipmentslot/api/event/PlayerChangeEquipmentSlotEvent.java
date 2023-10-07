package cn.xgpjun.xgpequipmentslot.api.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class PlayerChangeEquipmentSlotEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private UUID playerUUID;
    private String equipmentSlotName;
    private int slot;


    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}
