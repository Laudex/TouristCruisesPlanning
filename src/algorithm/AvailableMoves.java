package algorithm;

import model.Arc;
import model.Itinerary;
import model.Port;
import model.Vessel;
import service.data.Repository;

import java.util.List;

public class AvailableMoves {

    public static void exchangePorts(Itinerary itinerary) {
        List<Port> allPorts = Repository.getPorts();
        int start = 3;
        int finish = allPorts.size() - 1;
        finish -= start;
        int random = start + (int) (Math.random() * ++finish);
        System.out.println(random);
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
            for (Port port : itinerary.getNumberOfStops()){
                if (port.getPortId() == toRemove.getPortId()){
                    portIsPresent = true;
                    break;
                }
            }
            if (!portIsPresent){
                toRemove.setVisitLimit(randomPort.getVisitLimit() - 1);
                int index = toRemove.getVisitedItineraries().indexOf(itinerary);
                toRemove.getVisitedItineraries().remove(index);
            }
            int index = toRemove.getVisitedDays().indexOf(i);
            toRemove.getVisitedDays().remove(index);

        }

    }
}
