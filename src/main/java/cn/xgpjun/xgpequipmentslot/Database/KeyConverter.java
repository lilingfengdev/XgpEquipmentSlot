package cn.xgpjun.xgpequipmentslot.Database;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor
@Data
public class KeyConverter {
    private UUID player;
    private String name;

    public String toKey(){
        return player +":"+name;
    }
    public static KeyConverter fromKey(String key){
        return new KeyConverter(getPlayerUUIDFromKey(key),getNameFromKey(key));
    }

    private static UUID getPlayerUUIDFromKey(String key) {
        String[] parts = key.split(":");
        if (parts.length >= 2) {
            return UUID.fromString(parts[0]);
        } else {
            return null;
        }
    }
    private static String getNameFromKey(String key) {
        String[] parts = key.split(":");
        if (parts.length >= 2) {
            // 合并第一个冒号之后的所有部分作为name
            StringBuilder nameBuilder = new StringBuilder();
            for (int i = 1; i < parts.length; i++) {
                nameBuilder.append(parts[i]);
                // 在name中加入冒号作为分隔符
                if (i < parts.length - 1) {
                    nameBuilder.append(":");
                }
            }
            return nameBuilder.toString();
        } else {
            return null;
        }
    }
}
