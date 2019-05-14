package algorithm;

import model.Arc;
import model.ArcSolution;
import model.Itinerary;
import model.Port;
import model.Speed;
import model.Vessel;
import service.data.Repository;

import java.util.List;

public class AvailableMoves {

    public static void changeServiceTime(List<Itinerary> itineraries) {
        // double penalty = Validator.validateTimeConstraint(itineraries);
        for (Itinerary itinerary : itineraries) {
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
        }
        double finalPenalty = Validator.validateTimeConstraint(itineraries);
       // System.out.println(finalPenalty);
    }

    public static void changeServiceTime(Itinerary itinerary) {
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
        double finalPenalty = Validator.validateTimeConstraint(itinerary);
        //System.out.println(finalPenalty);
    }

    public static void changeSpeeds(List<Itinerary> itineraries) {
        double penalty = Validator.validateTimeConstraint(itineraries);
        List<Speed> availableSpeeds = Repository.getSpeeds();
        for (Itinerary itinerary : itineraries) {
            double testTwo = Validator.validateTimeConstraint(itinerary);
            for (ArcSolution arcSolution : itinerary.getArcs()) {
                for (Speed speed : availableSpeeds) {
                    Speed currentSpeed = arcSolution.getSpeed();
                    arcSolution.setSpeed(speed);
                    double newPenalty = Validator.validateTimeConstraint(itineraries);
                    double test = Validator.validateTimeConstraint(itinerary);
                    if (newPenalty < penalty) {
                        penalty = newPenalty;
                    } else {
                        arcSolution.setSpeed(currentSpeed);
                    }
                }

            }
        }
        double finalPenalty = Validator.validateTimeConstraint(itineraries);
       // System.out.println(finalPenalty);

    }

    public static void changeSpeeds(Itinerary itinerary) {
        double penalty = Validator.validateTimeConstraint(itinerary);
        List<Speed> availableSpeeds = Repository.getSpeeds();
        double testTwo = Validator.validateTimeConstraint(itinerary);
        for (ArcSolution arcSolution : itinerary.getArcs()) {
            for (Speed speed : availableSpeeds) {
                Speed currentSpeed = arcSolution.getSpeed();
                arcSolution.setSpeed(speed);
                double newPenalty = Validator.validateTimeConstraint(itinerary);
                double test = Validator.validateTimeConstraint(itinerary);
                if (newPenalty < penalty) {
                    penalty = newPenalty;
                } else {
                    changeServiceTime(itinerary);
                    newPenalty = Validator.validateTimeConstraint(itinerary);
                    if (newPenalty < penalty) {
                        penalty = newPenalty;
                    } else {
                        arcSolution.setSpeed(currentSpeed);
                    }
                }
            }

        }
        double finalPenalty = Validator.validateTimeConstraint(itinerary);
       // System.out.println(finalPenalty);

    }

    public static void exchangePorts(Itinerary itinerary) {
        List<Port> allPorts = Repository.getPorts();
        int start = 3;
        int finish = allPorts.size() - 1;
        finish -= start;
        int random = start + (int) (Math.random() * ++finish);
       // System.out.println(random);
        Port randomPort = allPorts.get(random);
        while (!randomPort.getVisitedItineraries().isEmpty()) {
            random = start + (int) (Math.random() * ++finish);
            randomPort = allPorts.get(random);
        }

        for (int i = 1; i < itinerary.getNumberOfStops().size() - 1; i++) {
            Port toRemove = itinerary.getNumberOfStops().get(i);
            Port prev = itinerary.getNumberOfStops().get(i - 1);
            Port next = itinerary.getNumberOfStops().get(i + 1);
            Arc first = Repository.findArcByPorts(prev.getPortId(), randomPort.getPortId());
            if (first == null) {
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
            Arc second = Repository.findArcByPorts(randomPort.getPortId(), next.getPortId());
            if (second == null) {
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
            /*if (randomPort.getVisitedDays().contains(i)) {
                continue;
            }*/
            for (Port incPort : itinerary.getVessel().getIncompatiblePorts()) {
                if (randomPort.getPortId() == incPort.getPortId()) {
                    continue;
                }
            }
            itinerary.getNumberOfStops().remove(i);
            itinerary.getNumberOfStops().add(i, randomPort);
            randomPort.setVisitLimit(randomPort.getVisitLimit() - 1);
            randomPort.getVisitedDays().add(i);
            randomPort.getVisitedItineraries().add(itinerary);
            boolean portIsPresent = false;
            for (Port port : itinerary.getNumberOfStops()) {
                if (port.getPortId() == toRemove.getPortId()) {
                    portIsPresent = true;
                    break;
                }
            }
            if (!portIsPresent) {
                toRemove.setVisitLimit(randomPort.getVisitLimit() - 1);
                int index = toRemove.getVisitedItineraries().indexOf(itinerary);
                toRemove.getVisitedItineraries().remove(index);
            }
            int index = toRemove.getVisitedDays().indexOf(i);
            toRemove.getVisitedDays().remove(index);

        }

    }

    public static void exchangePorts(Itinerary itinerary, int portId) {
        List<Port> allPorts = Repository.getPorts();
        for (int i = 1; i < itinerary.getNumberOfStops().size() - 1; i++) {
            if (itinerary.getNumberOfStops().get(i).getPortId() == portId) {
                Port toRemove = itinerary.getNumberOfStops().get(i);
                Port prev = itinerary.getNumberOfStops().get(i - 1);
                Port next = itinerary.getNumberOfStops().get(i + 1);
                for (Port port : allPorts) {
                    Arc first = Repository.findArcByPorts(prev.getPortId(), port.getPortId());
                    if (first == null) {
                        continue;
                    }
                    Arc second = Repository.findArcByPorts(port.getPortId(), next.getPortId());
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
                        if (port.getPortId() == incPort.getPortId()) {
                            continue;
                        }
                    }
                    if (port.getVisitLimit() == 0) {
                        continue;
                    }
                    // Условие неповторения порта в маршруте
                    List<Itinerary> visitedItineraries = port.getVisitedItineraries();
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
                    for (Integer visitedDays : port.getVisitedDays()){
                        if (visitedDays == i){
                            continue;
                        }
                    }

                 //   System.out.println(port.getPortId());
                    boolean findbetter = tryToSwap(itinerary, toRemove, port, i);
                    if (findbetter){
                        return;
                    }

                }
            }
        }

    }

    public static boolean tryToSwap(Itinerary itinerary, Port toRemove, Port toAdd, int index) {
        changeSpeeds(itinerary);
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
        for (Port port : itinerary.getNumberOfStops()){
            if (port.getPortId() == toRemove.getPortId()){
                portIsPresentInItinerary = true;
            }
        }
        if (!portIsPresentInItinerary){
            for (int i = 0; i < toRemove.getVisitedItineraries().size(); i++){
                if (toRemove.getVisitedItineraries().get(i).getRouteId() == itinerary.getRouteId()){
                    toRemove.getVisitedItineraries().remove(i);
                    break;
                }
            }
            toRemove.setVisitLimit(toRemove.getVisitLimit() + 1);
        }
        //Add Itinerary to port toAdd
        boolean newPortIsPresent = false;
        for (Itinerary itineraryToAdd : toAdd.getVisitedItineraries()){
            if (itineraryToAdd.getRouteId() == itinerary.getRouteId()){
                newPortIsPresent = true;
            }
        }
        if (!newPortIsPresent){
            toAdd.getVisitedItineraries().add(itinerary);
            toAdd.setVisitLimit(toAdd.getVisitLimit() - 1);
        }
        changeSpeeds(itinerary);

        double penaltyNew = Validator.validateTimeConstraint(itinerary);
        double penaltyShared = Validator.validateNumberOfSharedPorts(Repository.getItineraries(), 2);
        if (penaltyNew < penaltyOld && penaltyShared == 0) {
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
            for (Itinerary itineraryToRemove : toRemove.getVisitedItineraries()){
                if (itineraryToRemove.getRouteId() == itinerary.getRouteId()){
                    toRemoveIsPresent = true;
                }
            }
            if (!toRemoveIsPresent){
                toRemove.getVisitedItineraries().add(itinerary);
                toRemove.setVisitLimit(toRemove.getVisitLimit() - 1);
            }

            boolean toAddIsPresent = false;
            for (Port port : itinerary.getNumberOfStops()){
                if (port.getPortId() == toAdd.getPortId()){
                    toAddIsPresent = true;
                }
            }
            if (!toAddIsPresent){
                for (int i = 0; i < toAdd.getVisitedItineraries().size(); i++){
                    if (toAdd.getVisitedItineraries().get(i).getRouteId() == itinerary.getRouteId()){
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

