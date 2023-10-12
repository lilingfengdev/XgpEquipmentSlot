package cn.xgpjun.xgpequipmentslot.equipmentSlot;

import cn.xgpjun.xgpequipmentslot.utils.NMSUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.*;

@Data
@AllArgsConstructor
public class PlayerSlotInfo{
    private transient String name;
    private transient UUID player;
    private Map<Integer, ItemStack> equipments;



    public Collection<ItemStack> getAllItemStacks(){

        return new HashSet<>(getEquipments().values());
    }

    public String toJson(){
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(PlayerSlotInfo.class, new PlayerSlotInfoAdapter())
                .setPrettyPrinting()
                .create();
        return gson.toJson(this);
    }

    public static PlayerSlotInfo fromJson(String json,UUID player,String name){
        PlayerSlotInfo playerSlotInfo = new GsonBuilder()
                .registerTypeAdapter(PlayerSlotInfo.class, new PlayerSlotInfoAdapter())
                .setPrettyPrinting()
                .create()
                .fromJson(json, PlayerSlotInfo.class);
        playerSlotInfo.setPlayer(player);
        playerSlotInfo.setName(name);
        return playerSlotInfo;
    }

    public PlayerSlotInfo clone() {
        Map<Integer,ItemStack> temp = new HashMap<>();
        equipments.forEach((integer, itemStack) -> temp.put(integer,itemStack.clone()));
        return new PlayerSlotInfo(name,player,temp);
    }
}

class PlayerSlotInfoAdapter extends TypeAdapter<PlayerSlotInfo>{

    @Override
    public void write(JsonWriter out, PlayerSlotInfo value) throws IOException {
        if(value == null||value.getEquipments()==null){
            out.nullValue();
            return;
        }
        out.beginObject();
        for(int i:value.getEquipments().keySet()){
            out.name(String.valueOf(i)).value(NMSUtils.toNBTString(value.getEquipments().get(i)));
        }
        out.endObject();
    }

    @Override
    public PlayerSlotInfo read(JsonReader in) throws IOException {
        Map<Integer, ItemStack> equipments = new HashMap<>();
        if (in.peek() == null) {
            return new PlayerSlotInfo(null,null,equipments);
        }
        in.beginObject();
        ItemStack item;
        while (in.hasNext()){
            String key = in.nextName();
            item = NMSUtils.toItem(in.nextString());
            equipments.put(Integer.valueOf(key),item);
        }
        in.endObject();
        return new PlayerSlotInfo(null,null,equipments);
    }
}

