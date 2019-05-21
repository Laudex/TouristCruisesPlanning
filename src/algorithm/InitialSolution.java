package algorithm;

import model.Arc;
import model.ArcSolution;
import model.Itinerary;
import model.Port;
import model.Vessel;
import service.data.Repository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InitialSolution {

    private int depPortId = 2;
    private int arrPortId = 3;
    // 7 дней умноженные на 24 часа
    private int maxTime = 168;

    public static void findInitialSolution(int delta, int gamma, int Qmax, int Qmin) {
        List<ArcSolution> arcSolutions = new ArrayList<>();
        Set<Port> sharedPorts = new HashSet<>();
        int arcSoluId = 1;
        //Итерация по остановкам
        for (int i = 0; i < gamma; i++) {
            List<Port> portVisitedInSameDay = new ArrayList<>();
            for (Itinerary itinerary : Repository.getItineraries()) {
                Port currentPort = itinerary.getVessel().getCurrentPosition();
                List<Arc> availableArcs = findAvailableArc(itinerary.getVessel(), currentPort);
                Arc nextArc = findNextArc(availableArcs, itinerary, portVisitedInSameDay, sharedPorts, delta, Qmax);
                if (nextArc == null) {
                    System.out.println("Плыть больше некуда!");
                    continue;
                }
                itinerary.getVessel().setCurrentPosition(nextArc.getSecondPort());
                portVisitedInSameDay.add(nextArc.getSecondPort());
                itinerary.getNumberOfStops().add(nextArc.getSecondPort());
                boolean itineraryIsNotPresent = false;
                for (Itinerary itinerary1 : nextArc.getSecondPort().getVisitedItineraries()) {
                    if (itinerary1.getRouteId() == itinerary.getRouteId()) {
                        itineraryIsNotPresent = true;
                        break;
                    }
                }
                if (!itineraryIsNotPresent) {
                    nextArc.getSecondPort().setVisitLimit(nextArc.getSecondPort().getVisitLimit() - 1);
                    nextArc.getSecondPort().getVisitedItineraries().add(itinerary);
                }
                if (nextArc.getSecondPort().getVisitedItineraries().size() > 1){
                    sharedPorts.add(nextArc.getSecondPort());
                }
                itinerary.getVessel().getServiceTime().put(nextArc.getSecondPort().getPortId(), nextArc.getSecondPort().getMinimumServiceTime());
                ArcSolution arcSolution = new ArcSolution(arcSoluId, nextArc, itinerary.getVessel(), itinerary.getVessel().getCurrentSpeed());
                itinerary.getArcs().add(arcSolution);
                arcSoluId++;
                arcSolutions.add(arcSolution);
            }


        }
        Port arrivalPort = Repository.getPorts().get(2);
        List<Itinerary> notFinished = new ArrayList<>();
        for (Itinerary itinerary : Repository.getItineraries()) {
            List<Port> nodes = itinerary.getNumberOfStops();
            Port lastPort = nodes.get(nodes.size() - 1);
            Arc lastArc = canMoveToArrivalPort(lastPort);
            if (lastArc != null) {
                itinerary.getNumberOfStops().add(arrivalPort);
                ArcSolution arcSolution = new ArcSolution(arcSoluId, lastArc, itinerary.getVessel(), itinerary.getVessel().getCurrentSpeed());
                arcSoluId++;
                itinerary.getArcs().add(arcSolution);
            } else {
                notFinished.add(itinerary);
            }

        }
        while (!notFinished.isEmpty()) {
            buildExtraNodesToFinish(notFinished, arcSoluId, arcSolutions, sharedPorts, delta, Qmax);
            List<Integer> indexToRemove = new ArrayList<>();
            for (Itinerary itinerary : notFinished) {
                List<Port> nodes = itinerary.getNumberOfStops();
                Port lastPort = nodes.get(nodes.size() - 1);
                Arc lastArc = canMoveToArrivalPort(lastPort);
                if (lastArc != null) {
                    itinerary.getNumberOfStops().add(arrivalPort);
                    ArcSolution arcSolution = new ArcSolution(arcSoluId, lastArc, itinerary.getVessel(), itinerary.getVessel().getCurrentSpeed());
                    arcSoluId++;
                    itinerary.getArcs().add(arcSolution);
                    indexToRemove.add(itinerary.getRouteId());
                }

            }

            for (Integer index : indexToRemove) {
                for(Itinerary itinerary : notFinished) {
                    if (itinerary.getRouteId() == index){
                        notFinished.remove(itinerary);
                        break;
                    }
              //      Itinerary toRemove = notFinished.get(index);
                //    notFinished.remove(toRemove);
                }
            }
        }
        for (Itinerary itinerary : Repository.getItineraries()) {
            itinerary.getNumberOfStops().add(0, Repository.getPorts().get(1));
        }
        /*System.out.println(arcSoluId);
        List<Itinerary> finalIters = Repository.getItineraries();
        for (Itinerary itinerary : finalIters){
            System.out.println("Route Id: " + itinerary.getRouteId());
            System.out.println("Vessel Id: " + itinerary.getVessel().getId());
            for (Port port : itinerary.getNumberOfStops()){
                System.out.print(port.getPortId() + " ");
            }
            System.out.println();
        }*/

    }

    public static void buildExtraNodesToFinish(List<Itinerary> notFinished, int arcSoluId, List<ArcSolution> arcSolutions, Set<Port> sharedPorts, int delta, int qmax) {
        List<Port> portVisitedInSameDay = new ArrayList<>();
        for (Itinerary itinerary : notFinished) {
            Port currentPort = itinerary.getVessel().getCurrentPosition();
            List<Arc> availableArcs = findAvailableArc(itinerary.getVessel(), currentPort);
            Arc nextArc = findNextArc(availableArcs, itinerary, portVisitedInSameDay, sharedPorts, delta, qmax);
            if (nextArc == null) {
                System.out.println("Плыть больше некуда!");
                continue;
            }
            itinerary.getVessel().setCurrentPosition(nextArc.getSecondPort());
            portVisitedInSameDay.add(nextArc.getSecondPort());
            itinerary.getNumberOfStops().add(nextArc.getSecondPort());
            boolean itineraryIsNotPresent = false;
            for (Itinerary itinerary1 : nextArc.getSecondPort().getVisitedItineraries()) {
                if (itinerary1.getRouteId() == itinerary.getRouteId()) {
                    itineraryIsNotPresent = true;
                    break;
                }
            }
            if (!itineraryIsNotPresent) {
                nextArc.getSecondPort().setVisitLimit(nextArc.getSecondPort().getVisitLimit() - 1);
                nextArc.getSecondPort().getVisitedItineraries().add(itinerary);
            }
            if (nextArc.getSecondPort().getVisitedItineraries().size() > 0){
                sharedPorts.add(nextArc.getSecondPort());
            }
            itinerary.getVessel().getServiceTime().put(nextArc.getSecondPort().getPortId(), nextArc.getSecondPort().getMinimumServiceTime());
            ArcSolution arcSolution = new ArcSolution(arcSoluId, nextArc, itinerary.getVessel(), itinerary.getVessel().getCurrentSpeed());
            itinerary.getArcs().add(arcSolution);
            arcSoluId++;
            arcSolutions.add(arcSolution);
        }

    }

    public static Arc canMoveToArrivalPort(Port port) {
        int arrivalPortId = 3;
        for (Arc arc : Repository.getArcs()) {
            if (arc.getFirstPort().getPortId() == port.getPortId()) {
                if (arc.getSecondPort().getPortId() == arrivalPortId) {
                    return arc;
                }
            }
        }
        return null;
    }

    public static List<Arc> findAvailableArc(Vessel vessel, Port arrPort) {
        List<Arc> availableArcs = new ArrayList<>();
        //Проверка по списку всех Arcs
        for (Arc arc : Repository.getArcs()) {
            if (arc.getFirstPort().getPortId() == arrPort.getPortId()) {
                availableArcs.add(arc);
            }
        }
        List<Arc> availableArcsSecond = new ArrayList<>();
        //Проверка на валидность судна для арки
        for (Arc arc : availableArcs) {
            for (Vessel availableVessel : arc.getAvailableVessels()) {
                if (availableVessel.getId() == vessel.getId()) {
                    availableArcsSecond.add(arc);
                    break;
                }
            }
        }
        List<Arc> availableArcsThird = new ArrayList<>();
        //Проверка на валидность порта для судна
        for (Arc arc : availableArcsSecond) {
            boolean isInc = false;
            for (Port incPort : vessel.getIncompatiblePorts()) {
                if (arc.getSecondPort().getPortId() == incPort.getPortId()) {
                    isInc = true;
                    break;
                }
            }
            if (!isInc) {
                availableArcsThird.add(arc);
            }
        }
        List<Arc> availableArcsFinal = new ArrayList<>();
        for (Arc arc : availableArcsThird) {
            if (arc.getSecondPort().getPortId() != 3) {
                availableArcsFinal.add(arc);
            }
        }
        return availableArcsFinal;
    }

    private static Arc findNextArc(List<Arc> availableArcs, Itinerary itinerary, List<Port> visitedPortsTheSameDay, Set<Port> sharedPorts, int delta, int qmax) {
        List<Arc> availableArcsNew = new ArrayList<>();
        for (Arc arc : availableArcs) {
            boolean isPresentInItinerary = false;
            for (Port port : itinerary.getNumberOfStops()) {
                if (port.getPortId() == arc.getSecondPort().getPortId()) {
                    isPresentInItinerary = true;
                    break;
                }
            }
            if (!isPresentInItinerary) {
                availableArcsNew.add(arc);
            }
        }
        List<Arc> availableArcsNewSecond = new ArrayList<>();
        if (!availableArcsNew.isEmpty()) {
            for (Arc arc : availableArcsNew) {
                if (arc.getSecondPort().getVisitLimit() > 0) {
                    boolean portIsPresent = false;
                    for (Port port : visitedPortsTheSameDay) {
                        if (port.getPortId() == arc.getSecondPort().getPortId()) {
                            portIsPresent = true;
                            break;
                        }
                    }
                    if (!portIsPresent) {
                        availableArcsNewSecond.add(arc);
                    }
                } else if (arc.getSecondPort().getVisitLimit() == 0) {
                    List<Itinerary> visitedItineraries = arc.getSecondPort().getVisitedItineraries();
                    for (Itinerary itinerary1 : visitedItineraries) {
                        if (itinerary1.getRouteId() == itinerary.getRouteId()) {
                            boolean portIsPresent = false;
                            for (Port port : visitedPortsTheSameDay) {
                                if (port.getPortId() == arc.getSecondPort().getPortId()) {
                                    portIsPresent = true;
                                    break;
                                }
                            }
                            if (!portIsPresent) {
                                availableArcsNewSecond.add(arc);
                            }
                        }
                    }
                }
            }
        }
        List<Arc> availableArcsNewThird = new ArrayList<>();
        if (sharedPorts.size() >= delta){
            for (Arc arc : availableArcsNewSecond){
                if (arc.getSecondPort().getVisitedItineraries().size() == 0){
                    availableArcsNewThird.add(arc);
                } else {
                    for (Port shared : sharedPorts){
                        if (shared.getPortId() == arc.getSecondPort().getPortId() && arc.getSecondPort().getVisitLimit() < qmax){
                            availableArcsNewThird.add(arc);
                        }
                    }

                }
            }
        } else {
            availableArcsNewThird = availableArcsNewSecond;
        }
        if (!availableArcsNewThird.isEmpty()){
            Arc finalArc = availableArcsNewThird.get(0);
            double minDistance = finalArc.getDistance();
            for (Arc arc : availableArcsNewThird){
                if (arc.getDistance() < minDistance && Repository.canMoveFromPort(arc.getSecondPort())){
                    minDistance = arc.getDistance();
                    finalArc = arc;
                }
            }
            return finalArc;
        }
        return null;
    }



}
