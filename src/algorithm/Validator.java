package algorithm;

import model.Arc;
import model.Itinerary;
import model.Port;
import model.Vessel;
import service.data.Repository;

import java.util.List;

public class Validator {
    //Начало отсчета - 00:00 первого дня
    private static int R = 7;


    public static void validateTimeConstraint(List<Itinerary> itineraries){
        for (Itinerary itinerary : itineraries){
            for (int day = 1; day <= 7; day ++) {
                Vessel vessel = itinerary.getVessel();
                double currentTime = 0;
                for (int i = 0; i < itinerary.getNumberOfStops().size() - 1; i++) {
                    Port finishPort = itinerary.getNumberOfStops().get(i + 1);
                    int startPortId = itinerary.getNumberOfStops().get(i).getPortId();
                    int finishPortId = finishPort.getPortId();
                    double startTime = 24 * (day - 1) + finishPort.getStartTime();
                    double finishTime = 24 * (day - 1) + finishPort.getEndTime();
                    Arc arc = Repository.findArcByPorts(startPortId, finishPortId);
                    double timeToMove = arc.getDistance() / vessel.getCurrentSpeed().getSpeedValue();
                    double arrTime = currentTime + timeToMove;
                    //if (arrTime + finishPort.getRequiredPreServiceTime() >= startTime || arrTime + finishPort.getRequiredPreServiceTime() + finishPort.getMinimumServiceTime() + finishPort.getRequiredPostServiceTime())

                }
            }
        }
    }
}
