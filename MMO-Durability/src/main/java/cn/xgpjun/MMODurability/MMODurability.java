package cn.xgpjun.MMODurability;

import cn.xgpjun.xgpequipmentslot.XgpEquipmentSlot;
import cn.xgpjun.xgpequipmentslot.api.XESExpansion;
import cn.xgpjun.xgpequipmentslot.database.DataManager;
import cn.xgpjun.xgpequipmentslot.equipmentSlot.PlayerSlotInfo;
import io.lumine.mythic.lib.api.item.NBTItem;
import net.Indyuce.mmoitems.api.interaction.util.DurabilityItem;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class MMODurability implements XESExpansion, Listener {
    private static final List<EntityDamageEvent.DamageCause> IGNORED_CAUSES
            = Arrays.asList(EntityDamageEvent.DamageCause.DROWNING,
            EntityDamageEvent.DamageCause.SUICIDE,
            EntityDamageEvent.DamageCause.FALL,
            EntityDamageEvent.DamageCause.VOID,
            EntityDamageEvent.DamageCause.FIRE_TICK,
            EntityDamageEvent.DamageCause.SUFFOCATION,
            EntityDamageEvent.DamageCause.POISON,
            EntityDamageEvent.DamageCause.WITHER,
            EntityDamageEvent.DamageCause.STARVATION,
            EntityDamageEvent.DamageCause.MAGIC);

    @Override
    public void register() {
        XgpEquipmentSlot.log("已加载MMOItems-耐久度扩展");
    }
    @EventHandler(ignoreCancelled = true)
    public void playerDamage(EntityDamageEvent e){
        if (e.getEntityType() != EntityType.PLAYER ||IGNORED_CAUSES.contains(e.getCause())){
            return;
        }
        Player player = (Player)e.getEntity();
        int i = Math.max((int)e.getDamage() / 4, 1);
        XgpEquipmentSlot.getApi().getEquipmentSlotNames().forEach(s -> {
            PlayerSlotInfo playerSlotInfo = DataManager.loadPlayerSlotInfo(player.getUniqueId(),s);
            Map<Integer,ItemStack> temp = new HashMap<>(playerSlotInfo.getEquipments());
            playerSlotInfo.getEquipments().forEach((integer, itemStack) -> {
                ItemStack newItem = handleDamage(itemStack,player,i);
                if (newItem==null||newItem.getItemMeta()==null){
                    temp.remove(integer);
                }else {
                    temp.put(integer,newItem);
                }
            });
            playerSlotInfo.setEquipments(temp);
            DataManager.savePlayerSlotInfo(playerSlotInfo);
        });

    }

    private @Nullable ItemStack handleDamage(ItemStack itemStack, Player player, int damage){
        if(player==null){
            return itemStack;
        }
        if(!NBTItem.get(itemStack).hasType()){
            return itemStack;
        }
        DurabilityItem durabilityItem = new DurabilityItem(Objects.requireNonNull(player,"Player is null"),itemStack);
        if (!durabilityItem.isValid()){
            return itemStack;
        }
        durabilityItem.decreaseDurability(damage);
        return durabilityItem.toItem();
    }

    @Override
    public String getAuthor() {
        return "xgpjun";
    }

    @Override
    public String getName() {
        return "MMO-Durability";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }
}
