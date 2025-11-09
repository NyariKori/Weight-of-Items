package me.nyarikori.weightOfItems.service.weight;

/**
 * @author NyariKori
 */
public enum WeightType {
    BASE_WEIGHT("base"),
    MEDIUM_WEIGHT("medium"),
    HIGH_WEIGHT("high"),
    MAX_WEIGHT("max");

    private final String name;

    WeightType(String name) {
        this.name = name;
    }

    public String getConfigName() {
        return name;
    }
}
