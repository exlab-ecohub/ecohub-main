package team.exlab.ecohub.recyclingpoint.model;

import java.util.HashMap;
import java.util.Map;

public enum ERecyclableType {
    PLASTIC("пластик"),
    PAPER("бумага"),
    GLASS("стекло"),
    BULKY_WASTE("крупногабаритные отходы"),
    APPLIANCES("электронная и бытовая техника"),
    HAZARDOUS_WASTE("опасные отходы"),
    METAL("металл"),
    CLOTHES("одежда"),
    SECOND_LIFE("вторая жизнь"),
    RAGS("ветошь"),
    SHOES("обувь");

    private final String rusName;
    private static final Map<String, ERecyclableType> RUS_NAMES_TO_VALUES = new HashMap<>();

    static {
        for (ERecyclableType e : values()){
            RUS_NAMES_TO_VALUES.put(e.rusName, e);
        }
    }

    ERecyclableType(String rusName) {
        this.rusName = rusName;
    }

    public String getRusName() {
        return rusName;
    }

    public static ERecyclableType getTypeByRusName(String rusName) {
        if (RUS_NAMES_TO_VALUES.containsKey(rusName)){
            return RUS_NAMES_TO_VALUES.get(rusName);
        }
        throw new IllegalArgumentException(
                String.format("ERecyclableType value with rusName=%s not found", rusName)
        );
    }
}