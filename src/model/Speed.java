package model;

public class Speed {
    private Integer speedId;
    private Double speedValue;

    public Speed(Integer speedId, Double speedValue) {
        this.speedId = speedId;
        this.speedValue = speedValue;
    }

    public Integer getSpeedId() {
        return speedId;
    }

    public Double getSpeedValue() {
        return speedValue;
    }
}
