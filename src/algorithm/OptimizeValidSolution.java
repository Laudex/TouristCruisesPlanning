package algorithm;

import model.*;
import service.data.Repository;

import java.util.List;

public class OptimizeValidSolution {

    public static void optimize(List<Itinerary> itineraries, int delta){
        double cost = Cost.calculateCost(itineraries);
        //System.out.println("First cost: " + cost);
        for (Itinerary itinerary : itineraries){
            /*int start = 1;
            int finish = itinerary.getNumberOfStops().size() - 1;
            finish -= start;
            int random = start + (int) (Math.random() * ++finish);
            exchangePorts(itinerary, itinerary.getNumberOfStops().get(random).getPortId(), cost);*/
            changeSpeeds(itinerary);
            double newCost = Cost.calculateCost(itinerary);
            newCost = optimizeServiceTime(itinerary, newCost);
        //    System.out.println("New cost: " + newCost);

        }

        double newCost = Cost.calculateCost(itineraries);
        System.out.println("Final cost: " + newCost);

    }
    public static void changeSpeeds(Itinerary itinerary) {
        double cost = Cost.calculateCost(itinerary);
       // System.out.println("Old cost: " + cost);
       // double penalty = Validator.validateTimeConstraint(itinerary);
        List<Speed> availableSpeeds = Repository.getSpeeds();
        for (ArcSolution arcSolution : itinerary.getArcs()) {
            for (Speed speed : availableSpeeds) {
                Speed currentSpeed = arcSolution.getSpeed();
                arcSolution.setSpeed(speed);
                double penalty = Validator.validateTimeConstraint(itinerary);
                if (penalty < 1) {
                    double newCost = Cost.calculateCost(itinerary);
                    if (newCost < cost){
                        cost = newCost;
                        //System.out.println("New cost: " + cost);
                    } else {
                        arcSolution.setSpeed(currentSpeed);
                    }
                } else {
                    arcSolution.setSpeed(currentSpeed);
                }
            }

        }
        double finalPenalty = Validator.validateTimeConstraint(itinerary);
       // System.out.println(finalPenalty);

    }

    public static double optimizeServiceTime(Itinerary itinerary, double cost){
       // double cost = Cost.calculateCost(itinerary);
        for (Port port : itinerary.getNumberOfStops()) {
            if (port.getPortId() != 2 && port.getPortId() != 3) {
                for (double serviceTime = port.getMinimumServiceTime(); serviceTime < 24; serviceTime++) {
                    double currentServiceTime = itinerary.getVessel().getServiceTime().get(port.getPortId());
                    itinerary.getVessel().getServiceTime().put(port.getPortId(), serviceTime);
                    double newPenalty = Validator.validateTimeConstraint(itinerary);
                    if (newPenalty < 1) {
                        double newCost = Cost.calculateCost(itinerary);
                        if (newCost < cost){
                            cost = newCost;
                        } else {
                            itinerary.getVessel().getServiceTime().put(port.getPortId(), currentServiceTime);
                        }
                    } else {
                        itinerary.getVessel().getServiceTime().put(port.getPortId(), currentServiceTime);
                    }

                }
            }
        }
        return cost;
    }

    public static double changeServiceTime(Itinerary itinerary) {
        // double penalty = Validator.validateTimeConstraint(itineraries);
        double penalty = Validator.validateTimeConstraint(itinerary);
        for (Port port : itinerary.getNumberOfStops()) {
            if (port.getPortId() != 2 && port.getPortId() != 3) {
                for (double serviceTime = port.getMinimumServiceTime(); serviceTime < 24; serviceTime++) {
                    double currentServiceTime = itinerary.getVessel().getServiceTime().get(port.getPortId());
                    itinerary.getVessel().getServiceTime().put(port.getPortId(), serviceTime);
                    double newPenalty = Validator.validateTimeConstraint(itinerary);
                    if (newPenalty < penalty) {
                        penalty = newPenalty;
                    } else {
                        itinerary.getVessel().getServiceTime().put(port.getPortId(), currentServiceTime);
                    }

                }
            }
        }
        return Validator.validateTimeConstraint(itinerary);
    }

    public static boolean exchangePorts(Itinerary itinerary, int portIdToAdd) {
        Port toAdd = Repository.findPortById(portIdToAdd);
       // List<Port> allPorts = Repository.getPorts();
        for (int i = 1; i < itinerary.getNumberOfStops().size() - 1; i++) {
                Port toRemove = itinerary.getNumberOfStops().get(i);
                Port prev = itinerary.getNumberOfStops().get(i - 1);
                Port next = itinerary.getNumberOfStops().get(i + 1);
                    Arc first = Repository.findArcByPorts(prev.getPortId(), toAdd.getPortId());
                    if (first == null) {
                        continue;
                    }
                    Arc second = Repository.findArcByPorts(toAdd.getPortId(), next.getPortId());
                    if (second == null) {
                        continue;
                    }
                    boolean arcNotCompatibleWithVessel = true;
                    for (Vessel availableVessel : first.getAvailableVessels()) {
                        if (availableVessel.getId() == itinerary.getVessel().getId()) {
                            arcNotCompatibleWithVessel = false;
                            break;
                        }
                    }
                    if (arcNotCompatibleWithVessel) {
                        continue;
                    }
                    arcNotCompatibleWithVessel = true;
                    for (Vessel availableVessel : second.getAvailableVessels()) {
                        if (availableVessel.getId() == itinerary.getVessel().getId()) {
                            arcNotCompatibleWithVessel = false;
                            break;
                        }
                    }
                    if (arcNotCompatibleWithVessel) {
                        continue;
                    }
                    for (Port incPort : itinerary.getVessel().getIncompatiblePorts()) {
                        if (toAdd.getPortId() == incPort.getPortId()) {
                            continue;
                        }
                    }
                    if (toAdd.getVisitLimit() == 0) {
                        continue;
                    }
                    // Условие неповторения порта в маршруте
                    List<Itinerary> visitedItineraries = toAdd.getVisitedItineraries();
                    boolean portNotVisitedByRoute = true;
                    for (Itinerary itinerary1 : visitedItineraries) {
                        if (itinerary1.getRouteId() == itinerary.getRouteId()) {
                            portNotVisitedByRoute = false;
                            break;
                        }
                    }
                    if (!portNotVisitedByRoute) {
                        continue;
                    }
                    for (Integer visitedDays : toAdd.getVisitedDays()) {
                        if (visitedDays == i) {
                            continue;
                        }
                    }

                 //   System.out.println(toAdd.getPortId());
                    boolean foundNewValid = tryToSwap(itinerary, toRemove, toAdd, i);
                    if (foundNewValid) {
                        return true;
                    }

        }
        return false;

    }
    public static boolean tryToSwap(Itinerary itinerary, Port toRemove, Port toAdd, int index) {
        //AvailableMoves.changeSpeeds(itinerary);
        double penaltyOld = Validator.validateTimeConstraint(itinerary);
        Port prev = itinerary.getNumberOfStops().get(index - 1);
        Port next = itinerary.getNumberOfStops().get(index + 1);
        itinerary.getNumberOfStops().remove(index);
        itinerary.getNumberOfStops().add(index, toAdd);
        ArcSolution prevArcSol = itinerary.getArcs().get(index - 1);
        ArcSolution nextArcSol = itinerary.getArcs().get(index);
        Arc prevOld = prevArcSol.getArc();
        Arc nextOld = nextArcSol.getArc();
        Arc prevArcNew = Repository.findArcByPorts(prev.getPortId(), toAdd.getPortId());
        Arc nextArcNew = Repository.findArcByPorts(toAdd.getPortId(), next.getPortId());
        prevArcSol.setArc(prevArcNew);
        nextArcSol.setArc(nextArcNew);
        Vessel vessel = itinerary.getVessel();
        double serviceTimeOld = vessel.getServiceTime().get(toRemove.getPortId());
        vessel.getServiceTime().remove(toRemove.getPortId());
        vessel.getServiceTime().put(toAdd.getPortId(), serviceTimeOld);
        toRemove.getVisitedDays().remove(Integer.valueOf(index));
        toAdd.getVisitedDays().add(Integer.valueOf(index));
        //Remove Itinerary from port toRemove
        boolean portIsPresentInItinerary = false;
        for (Port port : itinerary.getNumberOfStops()) {
            if (port.getPortId() == toRemove.getPortId()) {
                portIsPresentInItinerary = true;
            }
        }
        if (!portIsPresentInItinerary) {
            for (int i = 0; i < toRemove.getVisitedItineraries().size(); i++) {
                if (toRemove.getVisitedItineraries().get(i).getRouteId() == itinerary.getRouteId()) {
                    toRemove.getVisitedItineraries().remove(i);
                    break;
                }
            }
            toRemove.setVisitLimit(toRemove.getVisitLimit() + 1);
        }
        //Add Itinerary to port toAdd
        boolean newPortIsPresent = false;
        for (Itinerary itineraryToAdd : toAdd.getVisitedItineraries()) {
            if (itineraryToAdd.getRouteId() == itinerary.getRouteId()) {
                newPortIsPresent = true;
            }
        }
        if (!newPortIsPresent) {
            toAdd.getVisitedItineraries().add(itinerary);
            toAdd.setVisitLimit(toAdd.getVisitLimit() - 1);
        }
        AvailableMoves.changeSpeeds(itinerary);

        double penaltyNew = Validator.validateTimeConstraint(itinerary);
        double penaltyShared = Validator.validateNumberOfSharedPorts(Repository.getItineraries(), 2);
        if (penaltyNew < 1 && penaltyShared == 0) {
            return true;
        } else {

            itinerary.getNumberOfStops().remove(index);
            itinerary.getNumberOfStops().add(index, toRemove);

            prevArcSol.setArc(prevOld);
            nextArcSol.setArc(nextOld);

            vessel.getServiceTime().remove(toAdd.getPortId());
            vessel.getServiceTime().put(toRemove.getPortId(), serviceTimeOld);
            toRemove.getVisitedDays().add(Integer.valueOf(index));
            toAdd.getVisitedDays().remove(Integer.valueOf(index));

            boolean toRemoveIsPresent = false;
            for (Itinerary itineraryToRemove : toRemove.getVisitedItineraries()) {
                if (itineraryToRemove.getRouteId() == itinerary.getRouteId()) {
                    toRemoveIsPresent = true;
                }
            }
            if (!toRemoveIsPresent) {
                toRemove.getVisitedItineraries().add(itinerary);
                toRemove.setVisitLimit(toRemove.getVisitLimit() - 1);
            }

            boolean toAddIsPresent = false;
            for (Port port : itinerary.getNumberOfStops()) {
                if (port.getPortId() == toAdd.getPortId()) {
                    toAddIsPresent = true;
                }
            }
            if (!toAddIsPresent) {
                for (int i = 0; i < toAdd.getVisitedItineraries().size(); i++) {
                    if (toAdd.getVisitedItineraries().get(i).getRouteId() == itinerary.getRouteId()) {
                        toAdd.getVisitedItineraries().remove(i);
                        break;
                    }
                }
                toAdd.setVisitLimit(toAdd.getVisitLimit() + 1);
            }
            return false;
        }
    }
}
