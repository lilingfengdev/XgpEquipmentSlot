package cn.xgpjun.xgpequipmentslot.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class MyItemBuilder {
    private ItemStack item;
    private final ItemMeta itemMeta;

    public MyItemBuilder(Material material, int amount, byte damage){
        item = new ItemStack(material,amount,damage);
        itemMeta = item.getItemMeta();
    }

    public MyItemBuilder(Material material){
        item = new ItemStack(material);
        itemMeta = item.getItemMeta();
    }
    public MyItemBuilder(@Nullable ItemStack item){
        if(item==null){
            item = getMissingItem();
        }
        this.item = item.clone();
        itemMeta = item.getItemMeta();
    }

    public MyItemBuilder setDisplayName(String DisplayName){
        itemMeta.setDisplayName(DisplayName);
        return this;
    }
    public String getDisplayName(){
        return itemMeta.getDisplayName();
    }

    public MyItemBuilder setLore(String... lore){
        itemMeta.setLore(Arrays.asList(lore));
        return this;
    }

    public MyItemBuilder setLore(List<String> lore){
        itemMeta.setLore(lore);
        return this;
    }

    public MyItemBuilder insertLore(int index, String... lore){
        List<String> loreList = getLoreList();

        if(loreList.isEmpty()){
            itemMeta.setLore(Arrays.asList(lore));
        }else {
            if(index<0){
                index = loreList.size()+index;
            }
            loreList.addAll(index, Arrays.asList(lore));
            itemMeta.setLore(loreList);
        }
        return this;
    }

    public MyItemBuilder insertLore(int index, List<String> lore){
        List<String> loreList = getLoreList();

        if(loreList.isEmpty()){
            itemMeta.setLore(lore);
        }else {
            if(index<0){
                index = loreList.size()+index;
            }
            loreList.addAll(index, lore);
            itemMeta.setLore(loreList);
        }
        return this;
    }

    public List<String> getLoreList() {
        List<String> rawLore = itemMeta.getLore();
        return (rawLore != null) ? new ArrayList<>(rawLore) : new ArrayList<>();
    }

    public MyItemBuilder addLore(String... lore){
        return insertLore(getLoreList().size(),lore);
    }

    public MyItemBuilder addLore(List<String> lore){
        return insertLore(getLoreList().size(),lore);
    }

    public MyItemBuilder setAmount(int amount){
        item.setAmount(amount);
        return this;
    }

    public ItemStack getItem() {
        item.setItemMeta(itemMeta);
        return item;
    }
    public ItemStack getItem(Player player){
        List<String> lore=itemMeta.getLore();
        List<String> newLore=new ArrayList<>();
        if(lore!=null&& Bukkit.getPluginManager().getPlugin("PlaceholderAPI")!=null){
            for (String str:lore){
                newLore.add(PlaceholderAPI.setPlaceholders(player,str));
            }
            itemMeta.setLore(newLore);
        }
        item.setItemMeta(itemMeta);
        return item;
    }

    public static ItemStack getMissingItem(){
        return new MyItemBuilder(Material.STONE).setDisplayName(ChatColor.RED+ "Missing Item!").setLore(ChatColor.AQUA+"If you see this line of lore").addLore(ChatColor.AQUA+ "means the item has missed enchantments/material.").getItem();
    }

}