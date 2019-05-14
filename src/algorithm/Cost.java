package algorithm;

import model.*;

import java.util.List;

public class Cost {

    public static double calculateCost(List<Itinerary> itineraries){
        double cost = 0;
        for (Itinerary itinerary : itineraries){
            cost = cost + calculateCost(itinerary);
        }
        return cost;
    }

    public static double calculateCost(Itinerary itinerary){
        double cost = 0;
        for (ArcSolution arcSolution : itinerary.getArcs()){
            double fuelCost = calculateFuelCost(arcSolution);
            double fixedCost = 0;
            double serviceCost = 0;
            Port secondPort = arcSolution.getArc().getSecondPort();
            Vessel vessel = itinerary.getVessel();
            if (secondPort.getPortId() != 3){
                fixedCost = secondPort.getPreAndPostCost().get(vessel.getId()) + secondPort.getFixedCost().get(vessel.getId());
                serviceCost = secondPort.getInServiceCost().get(vessel.getId()) * vessel.getServiceTime().get(secondPort.getPortId());
            }
            cost = cost + fuelCost + fixedCost + serviceCost;
        }
        return cost;
    }

    public static double calculateFuelCost(ArcSolution arcSolution){
        double distance = arcSolution.getArc().getDistance();
        Speed speed = arcSolution.getSpeed();
        double time = distance / speed.getSpeedValue();
        return time * arcSolution.getVessel().getFuelCost().get(speed.getSpeedId());
    }
}

