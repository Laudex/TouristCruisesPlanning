package service.data;

import model.Arc;
import model.Itinerary;
import model.Port;
import model.Speed;
import model.Vessel;

import java.util.List;

public class Repository {

    private static List<Port> ports;
    private static List<Vessel> vessels;
    private static List<Arc> arcs;
    private static List<Speed> speeds;
    private static List<Itinerary> itineraries;

    public static List<Port> getPorts() {
        return ports;
    }

    public static void setPorts(List<Port> ports) {
        Repository.ports = ports;
    }

    public static List<Vessel> getVessels() {
        return vessels;
    }

    public static void setVessels(List<Vessel> vessels) {
        Repository.vessels = vessels;
    }

    public static List<Arc> getArcs() {
        return arcs;
    }

    public static void setArcs(List<Arc> arcs) {
        Repository.arcs = arcs;
    }

    public static List<Speed> getSpeeds() {
        return speeds;
    }

    public static void setSpeeds(List<Speed> speeds) {
        Repository.speeds = speeds;
    }

    public static List<Itinerary> getItineraries() {
        return itineraries;
    }

    public static void setItineraries(List<Itinerary> itineraries) {
        Repository.itineraries = itineraries;
    }
}
