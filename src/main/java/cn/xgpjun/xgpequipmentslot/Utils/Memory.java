package cn.xgpjun.xgpequipmentslot.Utils;

import cn.xgpjun.xgpequipmentslot.XgpEquipmentSlot;

import java.io.File;

public class Memory {
    public static boolean checkFreeSpace(){
        File file = new File(XgpEquipmentSlot.getInstance().getDataFolder().toURI());
        if (file.exists()) {
            long freeSpace = file.getFreeSpace()/(1024 * 1024);
            return freeSpace > 300;
        }
        return false;
    }
}
