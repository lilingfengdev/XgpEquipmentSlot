package cn.xgpjun.xgpequipmentslot.gui.impl;

import cn.xgpjun.xgpequipmentslot.XgpEquipmentSlot;
import cn.xgpjun.xgpequipmentslot.api.event.InvLoadLayoutEvent;
import cn.xgpjun.xgpequipmentslot.api.event.PlayerChangeEquipmentSlotEvent;
import cn.xgpjun.xgpequipmentslot.armorSet.ArmorSet;
import cn.xgpjun.xgpequipmentslot.database.DataManager;
import cn.xgpjun.xgpequipmentslot.equipmentSlot.AttributeManager;
import cn.xgpjun.xgpequipmentslot.equipmentSlot.EquipmentSlot;
import cn.xgpjun.xgpequipmentslot.equipmentSlot.GuiItem;
import cn.xgpjun.xgpequipmentslot.equipmentSlot.PlayerSlotInfo;
import cn.xgpjun.xgpequipmentslot.gui.XESHolder;
import cn.xgpjun.xgpequipmentslot.utils.*;
import lombok.Getter;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class EquipmentSlotInventory extends XESHolder {
    private Inventory inv;
    @Getter
    private final EquipmentSlot equipmentSlot;
    @Getter
    private final PlayerSlotInfo playerSlotInfo;

    public EquipmentSlotInventory(EquipmentSlot equipmentSlot, PlayerSlotInfo playerSlotInfo) {
        this.equipmentSlot = equipmentSlot;
        this.playerSlotInfo = playerSlotInfo;
        loadLayout();
    }

    @Override
    public int getSize(){
        return inv.getSize();
    }
    @NotNull
    @Override
    public Inventory getInventory() {
        return inv;
    }

    private void loadLayout(){
        String title = equipmentSlot.getTitle();
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI")!=null){
            title = PlaceholderAPI.setPlaceholders(Bukkit.getOfflinePlayer(playerSlotInfo.getPlayer()).getPlayer(),title);
        }
        inv = Bukkit.createInventory(this,9*equipmentSlot.getLayout().size(),title);
        for(int i=0;i<equipmentSlot.getLayout().size()*9;i++){
            int row = i/9;
            int col = i%9;
            String c = String.valueOf(equipmentSlot.getLayout().get(row).charAt(col));
            if(equipmentSlot.getGuiItems().containsKey(c)){
                inv.setItem(i,new MyItemBuilder(equipmentSlot.getGuiItems().get(c).getItemStack()).getItem(Bukkit.getPlayer(playerSlotInfo.getPlayer())));
            }
        }
        //显示玩家的物品
        if(playerSlotInfo.getEquipments()==null)
            playerSlotInfo.setEquipments(new HashMap<>());
        //获得玩家激活套装
        Map<String ,Integer> resultMap = ArmorSet.getArmorSetAmount(playerSlotInfo);
        for(int slot:playerSlotInfo.getEquipments().keySet()){
            ItemStack item = playerSlotInfo.getEquipments().get(slot);
            if(item==null||item.getItemMeta()==null)
                continue;
            //添加套装lore
            item = ArmorSet.addArmorSetLore(item,resultMap);
            inv.setItem(slot,item);
        }
        InvLoadLayoutEvent event = new InvLoadLayoutEvent(playerSlotInfo.getPlayer(),this);
        Bukkit.getScheduler().runTask(XgpEquipmentSlot.getInstance(),()->Bukkit.getPluginManager().callEvent(event));
    }

    @Override
    public void handleClick(InventoryClickEvent e) {
        int slot = e.getRawSlot();
        //检验磁盘空间
        if(!Memory.checkFreeSpace()){
            e.getWhoClicked().sendMessage(Message.noFreeSpace);
            Bukkit.getScheduler().runTask(XgpEquipmentSlot.getInstance(),()-> e.getWhoClicked().closeInventory());
            return;
        }
        Player player = (Player) e.getWhoClicked();
        OfflinePlayer owner = Bukkit.getOfflinePlayer(playerSlotInfo.getPlayer());
        if(owner.getPlayer()!=null&&owner.getPlayer().getUniqueId()!=player.getUniqueId()&&!player.isOp()){
            return;
        }
        if(slot<inv.getSize()&&slot>=0){
            int row = slot/9;
            int col = slot%9;
            String c = String.valueOf(equipmentSlot.getLayout().get(row).charAt(col));
            GuiItem guiItem = equipmentSlot.getGuiItems().get(c);
            if(guiItem==null){
                return;
            }
            //检测是否有权限
            if(guiItem.getPermission()!=null){
                String permission = "XgpEquipmentSlot."+equipmentSlot.getName()+"."+guiItem.getPermission();
                if (!player.hasPermission(permission)){
                    player.sendMessage(Message.noPermission.replace("{permission}",permission));
                    return;
                }
            }
            //检测执行命令
            List<String> commandList = new ArrayList<>();
            guiItem.getCommandList().forEach(s -> {
                commandList.add(PlaceholderAPI.setPlaceholders(player,s));
            });
            if(!commandList.isEmpty()){
                commandList.forEach(s -> {
                    Bukkit.dispatchCommand(player,s);
                });
                return;
            }
        }
        //shift快速操作
        if(e.isShiftClick()&&e.isRightClick()){
            e.setCancelled(true);
            return;
        }
        if(e.isLeftClick()&&e.isShiftClick()){
            e.setCancelled(true);
            ItemStack clickItem = e.getCurrentItem();
            if(clickItem==null){
                return;
            }
            //自动脱下
            if((e.getRawSlot()>=0&&e.getRawSlot()<getSize())){
                if (checkEmptySlot(player)){
                    return;
                }
                ItemStack rawItem = playerSlotInfo.getEquipments().get(slot);
                if(rawItem!=null){
                    playerSlotInfo.getEquipments().remove(slot);
                    GiveItemsUtils.giveItem(player,rawItem);
                    loadLayout();
                    Bukkit.getScheduler().runTask(XgpEquipmentSlot.getInstance(),()-> player.openInventory(getInventory()));
                    DataManager.savePlayerSlotInfo(playerSlotInfo);
                    AttributeManager.addAttribute(owner.getPlayer());

                    Event event = new PlayerChangeEquipmentSlotEvent(player.getUniqueId(),equipmentSlot.getName(),slot);
                    Bukkit.getScheduler().runTask(XgpEquipmentSlot.getInstance(),()->Bukkit.getPluginManager().callEvent(event));

                }
            }else {
                try {
                    int index = equipmentSlot.autoMatch(player,clickItem,playerSlotInfo).get(15, TimeUnit.SECONDS);
                    if(index!=-1){
                        int row = index/9;
                        int col = index%9;
                        String c = String.valueOf(equipmentSlot.getLayout().get(row).charAt(col));
                        GuiItem guiItem = equipmentSlot.getGuiItems().get(c);
                        if(guiItem==null){
                            return;
                        }
                        //检测是否有权限
                        if(guiItem.getPermission()!=null){
                            String permission = "XgpEquipmentSlot."+equipmentSlot.getName()+"."+guiItem.getPermission();
                            if (!player.hasPermission(permission)){
                                player.sendMessage(Message.noPermission.replace("{permission}",permission));
                                return;
                            }
                        }
                        playerSlotInfo.getEquipments().put(index,clickItem);
                        e.setCurrentItem(null);
                        loadLayout();
                        Bukkit.getScheduler().runTask(XgpEquipmentSlot.getInstance(),()-> player.openInventory(getInventory()));
                        Event event = new PlayerChangeEquipmentSlotEvent(player.getUniqueId(),equipmentSlot.getName(),slot);
                        Bukkit.getScheduler().runTask(XgpEquipmentSlot.getInstance(),()->Bukkit.getPluginManager().callEvent(event));
                        DataManager.savePlayerSlotInfo(playerSlotInfo);
                        AttributeManager.addAttribute(owner.getPlayer());
                    }
                } catch (InterruptedException | ExecutionException | TimeoutException ex) {
                    throw new RuntimeException(ex);
                }
            }
            return;
        }

        //手动拖物品装备
        if((e.getRawSlot()>=0&&e.getRawSlot()<getSize())){
            if (checkEmptySlot(player)){
                return;
            }
            //没拿物品
            ItemStack cursorItem = e.getCursor();
            if(cursorItem==null||cursorItem.getItemMeta()==null){
                //返还之前装备的物品
                ItemStack rawItem = playerSlotInfo.getEquipments().get(slot);
                if(rawItem!=null){
                    playerSlotInfo.getEquipments().remove(slot);
                    GiveItemsUtils.giveItem(player,rawItem);
                    loadLayout();
                    Bukkit.getScheduler().runTask(XgpEquipmentSlot.getInstance(),()-> player.openInventory(getInventory()));
                    Event event = new PlayerChangeEquipmentSlotEvent(player.getUniqueId(),equipmentSlot.getName(),slot);
                    Bukkit.getScheduler().runTask(XgpEquipmentSlot.getInstance(),()->Bukkit.getPluginManager().callEvent(event));

                    DataManager.savePlayerSlotInfo(playerSlotInfo);
                    AttributeManager.addAttribute(owner.getPlayer());
                }
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
                    loadLayout();
                    Bukkit.getScheduler().runTask(XgpEquipmentSlot.getInstance(),()-> player.openInventory(getInventory()));
                    Event event = new PlayerChangeEquipmentSlotEvent(player.getUniqueId(),equipmentSlot.getName(),slot);
                    Bukkit.getScheduler().runTask(XgpEquipmentSlot.getInstance(),()->Bukkit.getPluginManager().callEvent(event));
                    DataManager.savePlayerSlotInfo(playerSlotInfo);
                    AttributeManager.addAttribute(owner.getPlayer());
                }else {
                    player.setItemOnCursor(null);
                    GiveItemsUtils.giveItem(player,cursorItem);
                }
            }
        }
    }

    private boolean checkEmptySlot(Player player){
        return  (VersionAdapterUtils.getPlayerEmptySlot(player)<2);
    }

}
