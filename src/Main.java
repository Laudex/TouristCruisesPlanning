import algorithm.InitialSolution;
import model.Arc;
import model.Itinerary;
import model.Port;
import model.Speed;
import model.Vessel;
import service.data.DataReaderService;
import service.data.Repository;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    // максимально допустимое число портов, которые могут одновременно состоять в 2-х маршрутах
    public static int delta = 2;
    // минимальное количество портов(остановок) в каждом маршруте
    public static int gamma = 2;
    // максимальное число маршрутов, проходящих через порт i
    public static int Qmax = 2;
    // минимальное число маршрутов, проходящих через порт i
    public static int Qmin = 0;

    public static void main(String[] args) throws FileNotFoundException {
        DataReaderService.readDataFromExcel("instances/base_instance.xlsx");

        List<Port> ports = DataReaderService.getPorts();
        for (Port port : ports){
            port.setVisitLimit(Qmax);
        }
        List<Vessel> vessels = DataReaderService.getVessels();
        List<Arc> arcs = DataReaderService.getArcs();
        List<Speed> speeds = DataReaderService.getSpeeds();
        for (Vessel vessel : vessels){
            vessel.setCurrentPosition(ports.get(1));
            vessel.setCurrentSpeed(speeds.get(2));
        }

        for (Port port : ports){
            port.setStartTime(8);
            port.setEndTime(22);
        }

        Repository.setPorts(ports);
        Repository.setVessels(vessels);
        Repository.setArcs(arcs);
        Repository.setSpeeds(speeds);

        List<Itinerary> itineraries = new ArrayList<>();
        for (Vessel vessel : vessels){
            Itinerary itinerary = new Itinerary(vessel.getId(), gamma, vessel);
            itineraries.add(itinerary);
        }
        Repository.setItineraries(itineraries);

        InitialSolution.findInitialSolution(delta, gamma, Qmax, Qmin);




    }
}
