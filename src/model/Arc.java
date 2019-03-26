package model;

import java.util.ArrayList;
import java.util.List;

public class Arc {
    private int arcId;
    private Vessel currentVessel;
    private List<Vessel> availableVessels;
    private Port firstPort;
    private Port secondPort;
    private double distance;
    private double travellingCost;
    private double travellingTime;
    private Speed speedOfVessel;

    public Arc() {
        availableVessels = new ArrayList<>();
    }

    public double calculateTravellingCost(){
        int speedId = currentVessel.getCurrentSpeed().getSpeedId();
        double speedValue = currentVessel.getCurrentSpeed().getSpeedValue();
        double fuelCost = currentVessel.getFuelCost().get(speedId);
        double time = distance / speedValue;
        return time * fuelCost;

    }

    public void setArcId(int arcId) {
        this.arcId = arcId;
    }

    public void setFirstPort(Port firstPort) {
        this.firstPort = firstPort;
    }

    public void setSecondPort(Port secondPort) {
        this.secondPort = secondPort;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getArcId() {
        return arcId;
    }

    public Port getFirstPort() {
        return firstPort;
    }

    public Port getSecondPort() {
        return secondPort;
    }

    public double getDistance() {
        return distance;
    }

    public List<Vessel> getAvailableVessels() {
        return availableVessels;
    }

    public void setAvailableVessels(List<Vessel> availableVessels) {
        this.availableVessels = availableVessels;
    }
}
