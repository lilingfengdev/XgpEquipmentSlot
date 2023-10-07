package cn.xgpjun.xgpequipmentslot.gui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

public abstract class XESHolder implements InventoryHolder {
    public abstract void handleClick(InventoryClickEvent e);
    public abstract int getSize();
}
