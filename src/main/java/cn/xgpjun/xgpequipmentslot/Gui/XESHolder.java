package cn.xgpjun.xgpequipmentslot.Gui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

public abstract class XESHolder implements InventoryHolder {
    public abstract void handleClick(InventoryClickEvent e);
}
