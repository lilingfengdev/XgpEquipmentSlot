package cn.xgpjun.xgpequipmentslot.equipmentSlot;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class Slot {
    private String type;
    private String lore;
    private String regex;
    private Map<String,String> other;
}
