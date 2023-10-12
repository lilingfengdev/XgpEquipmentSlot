package cn.xgpjun.xgpequipmentslot.bStats;

import cn.xgpjun.xgpequipmentslot.XgpEquipmentSlot;

public class Metrics {
    public static void enable() {
        int pluginId = 19171;
        org.bstats.bukkit.Metrics metrics = new org.bstats.bukkit.Metrics(XgpEquipmentSlot.getInstance(), pluginId);
        metrics.addCustomChart(new org.bstats.bukkit.Metrics.SimplePie("chart_id", () -> "My value"));
    }
}
