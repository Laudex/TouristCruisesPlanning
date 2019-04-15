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

    public static Port findPortById(int portId){
        for (Port port : ports){
            if (port.getPortId() == portId){
                return port;
            }
        }
        return null;
    }

    public static Arc findArcByPorts(int startPortId, int finishPortId){
        for (Arc arc : arcs){
            if (arc.getFirstPort().getPortId() == startPortId){
                if (arc.getSecondPort().getPortId() == finishPortId){
                    return arc;
                }
            }
        }
        return null;
    }

    public static boolean canMoveFromPort(Port firstPort){
        boolean canMove = false;
        for (Arc arc : arcs){
            if (arc.getFirstPort().getPortId() == firstPort.getPortId()){
                canMove = true;
                break;
            }
        }
        return canMove;
    }

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
