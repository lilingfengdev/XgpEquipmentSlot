package cn.xgpjun.xgpequipmentslot.armorSet.bonus;

import cn.xgpjun.xgpequipmentslot.XgpEquipmentSlot;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class Potions {
    static Map<UUID, List<PotionEffect>> map = new HashMap<>();
    static BukkitTask task;

    public static void init(){
        map.clear();
        task = Bukkit.getScheduler().runTaskTimerAsynchronously(XgpEquipmentSlot.getInstance(),()->{
            //防止遍历时修改
            Set<UUID> uuids = map.keySet();
            for (UUID uuid:uuids){
                List<PotionEffect> effects = map.get(uuid);
                if(effects!=null){
                    for (PotionEffect effect:effects){
                        if(Bukkit.getPlayer(uuid)!=null) {
                            Bukkit.getScheduler().runTask(XgpEquipmentSlot.getInstance(),()->effect.apply(Objects.requireNonNull(Bukkit.getPlayer(uuid))));
                        }
                    }
                }
            }
        },0L,100L);
    }

    public static void add(Player player,List<PotionEffect> potionEffects){
        map.put(player.getUniqueId(),potionEffects);
    }
    public static void remove(Player player){
        map.remove(player.getUniqueId());
    }
    public static void cancel(){
        map.clear();
        if(task!=null)
            task.cancel();
    }
}
