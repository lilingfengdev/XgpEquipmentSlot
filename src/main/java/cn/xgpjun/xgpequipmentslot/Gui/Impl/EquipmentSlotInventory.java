package cn.xgpjun.xgpequipmentslot.Gui.Impl;

import cn.xgpjun.xgpequipmentslot.EquipmentSlot.AttributeManager;
import cn.xgpjun.xgpequipmentslot.EquipmentSlot.PlayerSlotInfo;
import cn.xgpjun.xgpequipmentslot.Gui.XESHolder;
import cn.xgpjun.xgpequipmentslot.EquipmentSlot.EquipmentSlot;
import cn.xgpjun.xgpequipmentslot.Database.DataManager;
import cn.xgpjun.xgpequipmentslot.Utils.GiveItemsUtils;
import cn.xgpjun.xgpequipmentslot.Utils.Memory;
import cn.xgpjun.xgpequipmentslot.Utils.Message;
import cn.xgpjun.xgpequipmentslot.Utils.MyItem;
import cn.xgpjun.xgpequipmentslot.XgpEquipmentSlot;
import lombok.AllArgsConstructor;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

@AllArgsConstructor
public class EquipmentSlotInventory extends XESHolder {

    private EquipmentSlot equipmentSlot;
    private PlayerSlotInfo playerSlotInfo;

    @NotNull
    @Override
    public Inventory getInventory() {
        String title = equipmentSlot.getTitle();
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI")!=null){
            title = PlaceholderAPI.setPlaceholders(Bukkit.getOfflinePlayer(playerSlotInfo.getPlayer()).getPlayer(),title);
        }
        Inventory inv = Bukkit.createInventory(this,9*equipmentSlot.getLayout().size(),title);
        loadLayout(inv);
        //显示玩家的物品
        if(playerSlotInfo.getEquipments()==null)
            playerSlotInfo.setEquipments(new HashMap<>());
        for(int slot:playerSlotInfo.getEquipments().keySet()){
            ItemStack item = playerSlotInfo.getEquipments().get(slot);
            if(item!=null)
                inv.setItem(slot,item);
        }

        return inv;
    }

    private void loadLayout(Inventory inv){
        for(int i=0;i<equipmentSlot.getLayout().size()*9;i++){
            int row = i/9;
            int col = i%9;
            String c = String.valueOf(equipmentSlot.getLayout().get(row).charAt(col));
            if(equipmentSlot.getGuiItems().containsKey(c)){
                inv.setItem(i,new MyItem(equipmentSlot.getGuiItems().get(c)).getItem(Bukkit.getPlayer(playerSlotInfo.getPlayer())));
            }
        }
    }

    @Override
    public void handleClick(InventoryClickEvent e) {
        //检验磁盘空间
        if(!Memory.checkFreeSpace()){
            e.getWhoClicked().sendMessage(Message.noFreeSpace);
            Bukkit.getScheduler().runTask(XgpEquipmentSlot.getInstance(),()-> e.getWhoClicked().closeInventory());
            return;
        }
        int slot = e.getRawSlot();
        Player player = (Player) e.getWhoClicked();
        if(equipmentSlot.getSlotInfo().get(slot)==null){
            return;
        }
        OfflinePlayer owner = Bukkit.getOfflinePlayer(playerSlotInfo.getPlayer());
        if(owner.getPlayer()!=null&&owner.getPlayer().getUniqueId()!=player.getUniqueId()&&!player.isOp()){
            return;
        }
        ItemStack cursorItem = e.getCursor();
        if(cursorItem==null||cursorItem.getItemMeta()==null){
            if (playerSlotInfo == null) {
                return;
            }

            //返还之前装备的物品
            ItemStack rawItem = playerSlotInfo.getEquipments().get(slot);
            if(rawItem!=null){
                playerSlotInfo.getEquipments().remove(slot);
                GiveItemsUtils.giveItem(player,rawItem);
            }
            Bukkit.getScheduler().runTask(XgpEquipmentSlot.getInstance(),()-> player.openInventory(getInventory()));

            DataManager.savePlayerSlotInfo(playerSlotInfo);
            AttributeManager.addAttribute(owner.getPlayer(), playerSlotInfo);
        }else {
            //可以穿上
            if(equipmentSlot.canWear(slot, owner.getPlayer() ,cursorItem)){
                player.setItemOnCursor(null);
                //返还之前装备的物品
                ItemStack rawItem = playerSlotInfo.getEquipments().get(slot);
                if(rawItem!=null){
                    GiveItemsUtils.giveItem(player,rawItem);
                }
                //穿上新的物品
                playerSlotInfo.getEquipments().put(slot,cursorItem);
                Bukkit.getScheduler().runTask(XgpEquipmentSlot.getInstance(),()-> player.openInventory(getInventory()));
                DataManager.savePlayerSlotInfo(playerSlotInfo);
                AttributeManager.addAttribute(owner.getPlayer(), playerSlotInfo);
            }
        }
    }
}
