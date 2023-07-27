package cn.xgpjun.xgpequipmentslot.Utils;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class NMSUtils {

    private static Class<?> nbtTagCompound;
    private static Class<?> itemStack;
    private static Class<?> MojangsonParser;
    private static Class<?> CraftItemStack;
    private static Method parse;
    private static Method a; //构造
    private static Method asCraftMirror;
    private static Method asNMSCopy;
    private static Method save;
    private static Method toString;
    private static Method getTag;
    private static Method setTag;
    private static Method setString;
    private static Method remove;
    private static Method getString;

    private static String NMS_PACKAGE = "";
    private static String OBC_PACKAGE = "";
    private final static String version;
    public static int versionToInt;

    static
    {
        String packet = Bukkit.getServer().getClass().getPackage().getName();
        version = packet.substring(packet.lastIndexOf('.') + 1);
        String nmsBaseHead = "net.minecraft.server.";
        versionToInt = Integer.parseInt(version.split("_")[1]);
        try {
            if(versionToInt <17)
                Class.forName(nmsBaseHead + version +".ItemStack");
            else
                Class.forName("net.minecraft.world.item.ItemStack");
            NMS_PACKAGE = nmsBaseHead + version;
            OBC_PACKAGE = "org.bukkit.craftbukkit." + version;
        } catch (ClassNotFoundException ignored){
        }

        try {
            CraftItemStack = Class.forName(OBC_PACKAGE+".inventory.CraftItemStack");
            if(versionToInt<17){
                nbtTagCompound = Class.forName(NMS_PACKAGE+ ".NBTTagCompound");
                itemStack = Class.forName(NMS_PACKAGE+".ItemStack");
                MojangsonParser = Class.forName(NMS_PACKAGE+".MojangsonParser");
                parse = MojangsonParser.getMethod("parse",String.class);
                save = itemStack.getMethod("save",nbtTagCompound);
                getTag = itemStack.getMethod("getTag");
                setTag = itemStack.getMethod("setTag", nbtTagCompound);
                setString = nbtTagCompound.getMethod("setString", String.class, String.class);
                getString = nbtTagCompound.getMethod("getString",String.class);
                remove = nbtTagCompound.getMethod("remove",String.class);
            }else {
                nbtTagCompound = Class.forName("net.minecraft.nbt.NBTTagCompound");
                itemStack = Class.forName("net.minecraft.world.item.ItemStack");
                MojangsonParser = Class.forName("net.minecraft.nbt.MojangsonParser");
                parse = MojangsonParser.getMethod("a", String.class);
                save = itemStack.getMethod("b", nbtTagCompound);
                switch (versionToInt){
                    case 20:
                        getTag = itemStack.getMethod("w");break;
                    case 19:
                    case 18:
                        getTag = itemStack.getMethod("u");break;
                    case 17:
                        getTag = itemStack.getMethod("getOrCreateTag");break;
                }
                if(versionToInt==17)
                    setTag = itemStack.getMethod("setTag",nbtTagCompound);
                else
                    setTag = itemStack.getMethod("c", nbtTagCompound);
                setString = nbtTagCompound.getMethod("a", String.class, String.class);
                getString = nbtTagCompound.getMethod("l", String.class);
                remove = nbtTagCompound.getMethod("r",String.class);
            }
            if(versionToInt>12)
                a = itemStack.getMethod("a",nbtTagCompound);
            asCraftMirror = CraftItemStack.getMethod("asCraftMirror",itemStack);
            asNMSCopy = CraftItemStack.getMethod("asNMSCopy", ItemStack.class);
            toString = nbtTagCompound.getMethod("toString");

        } catch (ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public static ItemStack toItem(String NBTString){
        Object nbt; //(nbtTagCompound)
        ItemStack item = null;
        Object nmsItemStack;
        try {
            nbt = parse.invoke(MojangsonParser,NBTString);
            if(versionToInt>12){
                nmsItemStack = a.invoke(itemStack,nbt);
            }else {
                if(versionToInt>9) {
                    Constructor<?> constructor = itemStack.getConstructor(nbtTagCompound);
                    nmsItemStack = constructor.newInstance(nbt);
                }
                else {
                    Method createStack = itemStack.getMethod("createStack", nbtTagCompound);
                    nmsItemStack = createStack.invoke(itemStack,nbt);
                }

            }
            item = (ItemStack) asCraftMirror.invoke(CraftItemStack,nmsItemStack);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(item==null||item.getItemMeta()==null)
            item = MyItem.getMissingItem();
        return item;
    }
    public static String toNBTString(ItemStack itemStack){

        Object nbt;
        Object str = null;
        try {
            Object nmsItemStack = asNMSCopy.invoke(CraftItemStack,itemStack);
            Constructor<?> constructor = nbtTagCompound.getConstructor();
            nbt = constructor.newInstance();
            save.invoke(nmsItemStack,nbt);
            str = toString.invoke(nbt);
        }catch (Exception e){
            e.printStackTrace();
        }
        return (String)str;
    }

    public static ItemStack addTag(ItemStack item,String tag){
        try {
            Object nmsItemStack = asNMSCopy.invoke(CraftItemStack,item);
            Object tags = getTag.invoke(nmsItemStack);
            if(tags==null){
                tags = nbtTagCompound.newInstance();
            }
            String key = "XgpES";
            setString.invoke(tags,key,tag);
            setTag.invoke(nmsItemStack, tags);
            return (ItemStack) asCraftMirror.invoke(CraftItemStack,nmsItemStack);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public static ItemStack removeTag(ItemStack item){
        try {
            Object nmsItemStack = asNMSCopy.invoke(CraftItemStack,item);
            Object tags = getTag.invoke(nmsItemStack);
            if(tags==null){
                tags = nbtTagCompound.newInstance();
            }
            String key = "XgpES";
            remove.invoke(tags,key);
            setTag.invoke(nmsItemStack, tags);
            return (ItemStack) asCraftMirror.invoke(CraftItemStack,nmsItemStack);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static boolean checkTag(ItemStack item,String tag) {
        if(tag==null)
            return false;
        try {
            Object nmsItemStack = asNMSCopy.invoke(CraftItemStack,item);
            Object tags = getTag.invoke(nmsItemStack);
            if(tags==null)
                return false;
            String key = "XgpES";
            Object str = getString.invoke(tags,key);
            return tag.equals(str);
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

}