package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Port {
    private int portId;
    private int visitLimit;
    private List<Itinerary> visitedItineraries = new ArrayList<>();
    private List<Integer> visitedVesselsId;
    private double minimumServiceTime;
    private double requiredPreServiceTime;
    private double requiredPostServiceTime;

    //Fixed Pre and Post service cost for Vessel
    private Map<Integer, Double> preAndPostCost;

    //Fixed cost for Vessel
    private Map<Integer, Double> fixedCost;

    //In-service cost for Vessel
    private Map<Integer, Double> inServiceCost;

    private List<Vessel> incompatibleVessels;
    //Start of time window
    private double startTime;
    //End of time window
    private double endTime;

    //Variables
    private double timeOfServiceStart;
    private int serviceTime;
    private Vessel currentVessel;

    public Port(int portId) {
        this.portId = portId;
        incompatibleVessels = new ArrayList<>();
        visitedVesselsId = new ArrayList<>();
    }

    public int getPortId() {
        return portId;
    }

    public Map<Integer, Double> getFixedCost() {
        return fixedCost;
    }

    public void setFixedCost(Map<Integer, Double> fixedCost) {
        this.fixedCost = fixedCost;
    }

    public List<Vessel> getIncompatibleVessels() {
        return incompatibleVessels;
    }

    public void setIncompatibleVessels(List<Vessel> incompatibleVessels) {
        this.incompatibleVessels = incompatibleVessels;
    }

    public Map<Integer, Double> getPreAndPostCost() {
        return preAndPostCost;
    }

    public void setPreAndPostCost(Map<Integer, Double> preAndPostCost) {
        this.preAndPostCost = preAndPostCost;
    }

    public Map<Integer, Double> getInServiceCost() {
        return inServiceCost;
    }

    public void setInServiceCost(Map<Integer, Double> inServiceCost) {
        this.inServiceCost = inServiceCost;
    }

    public double getMinimumServiceTime() {
        return minimumServiceTime;
    }

    public void setMinimumServiceTime(double minimumServiceTime) {
        this.minimumServiceTime = minimumServiceTime;
    }

    public double getRequiredPostServiceTime() {
        return requiredPostServiceTime;
    }

    public void setRequiredPostServiceTime(double requiredPostServiceTime) {
        this.requiredPostServiceTime = requiredPostServiceTime;
    }

    public double getRequiredPreServiceTime() {
        return requiredPreServiceTime;
    }

    public void setRequiredPreServiceTime(double requiredPreServiceTime) {
        this.requiredPreServiceTime = requiredPreServiceTime;
    }

    public int getVisitLimit() {
        return visitLimit;
    }

    public void setVisitLimit(int visitLimit) {
        this.visitLimit = visitLimit;
    }

    public double getStartTime() {
        return startTime;
    }

    public void setStartTime(double startTime) {
        this.startTime = startTime;
    }

    public double getEndTime() {
        return endTime;
    }

    public void setEndTime(double endTime) {
        this.endTime = endTime;
    }

    public List<Itinerary> getVisitedItineraries() {
        return visitedItineraries;
    }

    public void setVisitedItineraries(List<Itinerary> visitedItineraries) {
        this.visitedItineraries = visitedItineraries;
    }
}
