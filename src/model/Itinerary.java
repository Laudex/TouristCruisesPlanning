package model;

import java.util.ArrayList;
import java.util.List;

public class Itinerary {
    private final double timeHorizont = 168;
    private int routeId;
    private Vessel vessel;
    private List<Port> numberOfStops = new ArrayList<>();
    private List<Arc> arcs = new ArrayList<>();
    private int minimunNumberOfStops;
    private double routeTime;

    public Itinerary(int routeId, int minimunNumberOfStops, Vessel vessel) {
        this.routeId = routeId;
        this.minimunNumberOfStops = minimunNumberOfStops;
        this.vessel = vessel;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public List<Port> getNumberOfStops() {
        return numberOfStops;
    }

    public void setNumberOfStops(List<Port> numberOfStops) {
        this.numberOfStops = numberOfStops;
    }

    public int getMinimunNumberOfStops() {
        return minimunNumberOfStops;
    }

    public void setMinimunNumberOfStops(int minimunNumberOfStops) {
        this.minimunNumberOfStops = minimunNumberOfStops;
    }

    public double getRouteTime() {
        return routeTime;
    }

    public void setRouteTime(double routeTime) {
        this.routeTime = routeTime;
    }

    public Vessel getVessel() {
        return vessel;
    }

    public void setVessel(Vessel vessel) {
        this.vessel = vessel;
    }
}
