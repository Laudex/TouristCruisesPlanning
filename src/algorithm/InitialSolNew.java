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

public class InitialSolNew {

    public static void findInitialSolution(int delta, int gamma, int Qmax, int Qmin) {
        List<ArcSolution> arcSolutions = new ArrayList<>();
        Set<Port> sharedPorts = new HashSet<>();
        for (Itinerary currentItinerary : Repository.getItineraries()){
            int minimum = gamma;
            int day = 1;
            Port startPort = currentItinerary.getVessel().getCurrentPosition();
            Port finishPort = Repository.findPortById(3);
            Port linkedPort = findAvailablePortBetweenPorts(startPort, finishPort, currentItinerary.getVessel(), currentItinerary, delta, sharedPorts, day);
            if (linkedPort == null){
                System.out.println("Портов больше нет!!!");
                return;
            }

            currentItinerary.getNumberOfStops().add(startPort);
            currentItinerary.getNumberOfStops().add(linkedPort);
            currentItinerary.getNumberOfStops().add(finishPort);

            currentItinerary.getVessel().getServiceTime().put(linkedPort.getPortId(), linkedPort.getMinimumServiceTime());

            linkedPort.setVisitLimit(linkedPort.getVisitLimit() - 1);
            linkedPort.getVisitedDays().add(day);
            linkedPort.getVisitedItineraries().add(currentItinerary);
            if (linkedPort.getVisitedItineraries().size() > 1) {
                sharedPorts.add(linkedPort);
            }



            /*ArcSolution arcSolution = new ArcSolution(arcSoluId, Repository.findArcByPorts(startPort.getPortId(), linkedPort.getPortId()), currentItinerary.getVessel(), currentItinerary.getVessel().getCurrentSpeed());
            currentItinerary.getArcs().add(arcSolution);
            arcSolutions.add(arcSolution);
            arcSoluId++;
            arcSolution = new ArcSolution(arcSoluId, Repository.findArcByPorts(linkedPort.getPortId(), finishPort.getPortId()), currentItinerary.getVessel(), currentItinerary.getVessel().getCurrentSpeed());
            currentItinerary.getArcs().add(arcSolution);
            arcSoluId++;
            arcSolutions.add(arcSolution);*/

            minimum--;
            while (minimum > 0){
                day++;
                for (int i = 0; i < currentItinerary.getNumberOfStops().size() - 1; i++){
                    startPort = currentItinerary.getNumberOfStops().get(i);
                    finishPort = currentItinerary.getNumberOfStops().get(i+1);
                    linkedPort = findAvailablePortBetweenPorts(startPort, finishPort, currentItinerary.getVessel(), currentItinerary, delta, sharedPorts, day);
                    if (linkedPort == null){
                        System.out.println("Портов дальше нет!!!");
                        continue;
                    }
                    currentItinerary.getNumberOfStops().add(i+1, linkedPort);

                    currentItinerary.getVessel().getServiceTime().put(linkedPort.getPortId(), linkedPort.getMinimumServiceTime());

                    linkedPort.setVisitLimit(linkedPort.getVisitLimit() - 1);
                    linkedPort.getVisitedDays().add(day);
                    linkedPort.getVisitedItineraries().add(currentItinerary);
                    if (linkedPort.getVisitedItineraries().size() > 1) {
                        sharedPorts.add(linkedPort);
                    }



                   /* arcSolution = new ArcSolution(arcSoluId, Repository.findArcByPorts(startPort.getPortId(), linkedPort.getPortId()), currentItinerary.getVessel(), currentItinerary.getVessel().getCurrentSpeed());
                    currentItinerary.getArcs().add(arcSolution);
                    arcSolutions.add(arcSolution);
                    arcSoluId++;
                    arcSolution = new ArcSolution(arcSoluId, Repository.findArcByPorts(linkedPort.getPortId(), finishPort.getPortId()), currentItinerary.getVessel(), currentItinerary.getVessel().getCurrentSpeed());
                    currentItinerary.getArcs().add(arcSolution);
                    arcSoluId++;
                    arcSolutions.add(arcSolution);*/
                    minimum--;
                    break;
                }
            }
            int arcSoluId = 1;
            for (int i = 0; i < currentItinerary.getNumberOfStops().size() - 1; i++){
                Port first = currentItinerary.getNumberOfStops().get(i);
                Port second = currentItinerary.getNumberOfStops().get(i+1);
                Arc arc = Repository.findArcByPorts(first.getPortId(), second.getPortId());
                ArcSolution arcSolution = new ArcSolution(arcSoluId, arc, currentItinerary.getVessel(), currentItinerary.getVessel().getCurrentSpeed());
                currentItinerary.getArcs().add(arcSolution);
                arcSolutions.add(arcSolution);
                arcSoluId++;
            }

        }
    }

    public static Port findAvailablePortBetweenPorts(Port firstPort, Port secondPort, Vessel vessel, Itinerary itinerary, int delta, Set<Port> sharedPorts, int day){
        for (Port port : Repository.getPorts()){
            if (port.getPortId() != 2 && port.getPortId() != 3){
                boolean isInc = false;
                for (Port incPort : vessel.getIncompatiblePorts()) {
                    if (port.getPortId() == incPort.getPortId()) {
                        continue;
                    }
                }
                Arc firstArc = Repository.findArcByPorts(firstPort.getPortId(), port.getPortId());
                Arc secondArc = Repository.findArcByPorts(port.getPortId(), secondPort.getPortId());

                if (firstArc == null || secondArc == null){
                    continue;
                }
                List<Vessel> availableVesselsForFirstArc = firstArc.getAvailableVessels();
                List<Vessel> availableVesselsForSecondArc = secondArc.getAvailableVessels();

                boolean isAvailableFirst = false;
                for (Vessel vessel1 : availableVesselsForFirstArc){
                    if (vessel1.getId() == vessel.getId()){
                        isAvailableFirst = true;
                    }
                }
                boolean isAvailableSecond = false;
                for (Vessel vessel1 : availableVesselsForSecondArc){
                    if (vessel1.getId() == vessel.getId()){
                        isAvailableSecond = true;
                    }
                }

                if (!isAvailableFirst || !isAvailableSecond){
                    continue;
                }

                if (port.getVisitLimit() == 0){
                    continue;
                }
                if (port.getVisitLimit() == 1){
                    boolean isInItinerary = false;
                    List<Port> portsInItinerary = itinerary.getNumberOfStops();
                    for (Port port1 : portsInItinerary){
                        if (port1.getPortId() == port.getPortId()){
                            isInItinerary = true;
                            break;
                        }
                    }
                    if (isInItinerary){
                        continue;
                    }
                    if (sharedPorts.size() >= delta){
                        boolean isShared = false;
                        for (Port shared : sharedPorts){
                            if (shared.getPortId() == port.getPortId()){
                                isShared = true;
                                break;
                            }
                        }
                        if (!isShared){
                            continue;
                        }
                    }
                }
                List<Integer> visitedDays = port.getVisitedDays();
                boolean isVisitedThisDay = false;
                for (Integer day1 : visitedDays){
                    if (day1 == day){
                        isVisitedThisDay = true;
                        break;
                    }
                }
                if (isVisitedThisDay){
                    continue;
                }
                return port;


            }
        }
        return null;

    }
}
