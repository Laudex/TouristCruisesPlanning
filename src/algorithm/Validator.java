package algorithm;

import model.Arc;
import model.Itinerary;
import model.Port;
import model.Vessel;
import service.data.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Validator {
    //Начало отсчета - 00:00 первого дня
    private static int R = 7;
    private static int delta = 1;

    public static double validateNumberOfSharedPorts(List<Itinerary> itineraries) {
        double penalty = 0;
        Set<Integer> portIds = new HashSet<>();
        for (Itinerary itinerary : itineraries) {
            for (Port port : itinerary.getNumberOfStops()) {
                if (port.getPortId() != 2 && port.getPortId() != 3) {
                    int numberOfItineraries = port.getVisitedItineraries().size();
                    if (numberOfItineraries > 1) {
                        portIds.add(port.getPortId());
                    }
                }
            }
        }
        double diff = portIds.size() - delta;
        if (diff > 0) {
            penalty = penalty + 10 * diff;
        }
        return penalty;
    }

    public static double validateNumberOfPorts(List<Itinerary> itineraries, int gamma) {
        double penalty = 0;
        for (Itinerary itinerary : itineraries) {
            Set<Integer> portIds = new HashSet<>();
            for (Port port : itinerary.getNumberOfStops()) {
                portIds.add(port.getPortId());
            }
            int numberOfStops = portIds.size() - 2;
            if (numberOfStops < gamma) {
                penalty = penalty + 10 * (gamma - numberOfStops);
            }
        }
        return penalty;
    }

    public static double validateServiceTime(List<Itinerary> itineraries) {
        double penalty = 0;
        for (Itinerary itinerary : itineraries) {
            Vessel vessel = itinerary.getVessel();
            for (Port port : itinerary.getNumberOfStops()) {
                if (port.getPortId() != 2 && port.getPortId() != 3) {
                    double diff = port.getMinimumServiceTime() - vessel.getServiceTime().get(port.getPortId());
                    if (diff > 0) {
                        penalty = penalty + diff;
                    }
                }
            }
        }
        return penalty;
    }


    public static double validateTimeConstraint(List<Itinerary> itineraries) {
        double penaltyForAllItineraries = 0;
        for (Itinerary itinerary : itineraries) {
            Vessel vessel = itinerary.getVessel();
            double currentTime = 0;
            for (int i = 0, day = 1; i < itinerary.getNumberOfStops().size() - 1; i++, day++) {
                Port finishPort = itinerary.getNumberOfStops().get(i + 1);
                int startPortId = itinerary.getNumberOfStops().get(i).getPortId();
                int finishPortId = finishPort.getPortId();
                Arc arc = Repository.findArcByPorts(startPortId, finishPortId);
                double timeToMove = arc.getDistance() / itinerary.getArcs().get(i).getSpeed().getSpeedValue();
                double arrTime = currentTime + timeToMove;
                if (finishPort.getPortId() == 3) {
                    double depotConstraint = constraintDepot(arrTime, day);
                    penaltyForAllItineraries = penaltyForAllItineraries + depotConstraint;
                } else {
                    double constraint = constraintFirst(arrTime, day, finishPort, vessel);
                    penaltyForAllItineraries = penaltyForAllItineraries + constraint;
                    //Обновляем currentTime (время отправки из конечного порта)
                    currentTime = currentTime + timeToMove + finishPort.getRequiredPreServiceTime() + vessel.getServiceTime().get(finishPort.getPortId()) + finishPort.getRequiredPostServiceTime();
                }
            }


        }
        return penaltyForAllItineraries;
    }

    public static double validateTimeConstraint(Itinerary itinerary) {
        double penaltyForAllItineraries = 0;
        Vessel vessel = itinerary.getVessel();
        double currentTime = 0;
        for (int i = 0, day = 1; i < itinerary.getNumberOfStops().size() - 1; i++, day++) {
            Port finishPort = itinerary.getNumberOfStops().get(i + 1);
            int startPortId = itinerary.getNumberOfStops().get(i).getPortId();
            int finishPortId = finishPort.getPortId();
            Arc arc = Repository.findArcByPorts(startPortId, finishPortId);
            double timeToMove = arc.getDistance() / itinerary.getArcs().get(i).getSpeed().getSpeedValue();
            double arrTime = currentTime + timeToMove;
            if (finishPort.getPortId() == 3) {
                double depotConstraint = constraintDepot(arrTime, day);
                penaltyForAllItineraries = penaltyForAllItineraries + depotConstraint;
            } else {
                double constraint = constraintFirst(arrTime, day, finishPort, vessel);
                penaltyForAllItineraries = penaltyForAllItineraries + constraint;
                //Обновляем currentTime (время отправки из конечного порта)
                currentTime = currentTime + timeToMove + finishPort.getRequiredPreServiceTime() + vessel.getServiceTime().get(finishPort.getPortId()) + finishPort.getRequiredPostServiceTime();
            }
        }


        return penaltyForAllItineraries;
    }

    //Ограничение, что судно успеет прибыть в этот день и ему хватит время на обслуживание до конца дня, прибытие попало во временные окна
    public static double constraintFirst(double arrTime, int r, Port finishPort, Vessel vessel) {
        double penalty = 0;
        double first = 24 * (r - 1) - arrTime;
        if (first > 0) {
            penalty = penalty + first;
        }
        double second = arrTime + finishPort.getRequiredPreServiceTime() + finishPort.getRequiredPostServiceTime() + vessel.getServiceTime().get(finishPort.getPortId()) - 24 * r;
        if (second > 0) {
            penalty = penalty + second;
        }
        if (arrTime > finishPort.getEndTime() * (r - 1) && arrTime < finishPort.getStartTime() * (r - 1)) {
            double third = Math.min(arrTime - finishPort.getEndTime() * (r - 1), finishPort.getStartTime() * (r - 1) - arrTime);
            penalty = penalty + third;
        }
        return penalty;
    }

    public static double constraintDepot(double arrTime, int r) {
        double penalty = 0;
        double first = arrTime - (24 * (r - 1) + 12);
        if (first > 0){
            penalty = penalty + first;
        }
        double second = 24 * (r - 1) - arrTime;
        if (second > 0){
            penalty = penalty + second;
        }
        return penalty;


    }


}
