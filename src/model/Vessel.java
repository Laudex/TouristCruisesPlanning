package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Vessel {

    private Integer id;
    private Speed currentSpeed;
    //Variable. Service Time for portId
    private Map<Integer, Double> serviceTime = new HashMap<>();
    //Fuel Cost for Speed. SpeedId - Cost
    private Map<Integer, Double> fuelCost;
    private Port currentPosition;
    private List<Port> incompatiblePorts;
    private List<Speed> incompatibleSpeeds;

    public Vessel(Integer id) {
        this.id = id;
        incompatiblePorts = new ArrayList<>();
    }

    public Speed getCurrentSpeed() {
        return currentSpeed;
    }

    public Map<Integer, Double> getFuelCost() {
        return fuelCost;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Port> getIncompatiblePorts() {
        return incompatiblePorts;
    }

    public void setIncompatiblePorts(List<Port> incompatiblePorts) {
        this.incompatiblePorts = incompatiblePorts;
    }

    public void setFuelCost(Map<Integer, Double> fuelCost) {
        this.fuelCost = fuelCost;
    }

    public Map<Integer, Double> getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(Map<Integer, Double> serviceTime) {
        this.serviceTime = serviceTime;
    }

    public Port getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(Port currentPosition) {
        this.currentPosition = currentPosition;
    }

    public void setCurrentSpeed(Speed currentSpeed) {
        this.currentSpeed = currentSpeed;
    }


}
