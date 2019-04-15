import algorithm.AvailableMoves;
import algorithm.InitialSolution;
import algorithm.OptimizeValidSolution;
import algorithm.Validator;
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
    public static int gamma = 4;
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
            port.setStartTime(16);
            port.setEndTime(14);
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
        for (Itinerary itinerary : Repository.getItineraries()){
            int day = 1;
            for (Port port : itinerary.getNumberOfStops()){
                if (port.getPortId() != 2 && port.getPortId() != 3){
                    port.getVisitedDays().add(day);
                    day++;
                }

            }
        }

        /*double penaltyTimeConstraint = Validator.validateTimeConstraint(Repository.getItineraries());
        double penaltyServiceTime = Validator.validateServiceTime(Repository.getItineraries());
        double penaltyNumberOfStops = Validator.validateNumberOfPorts(Repository.getItineraries(), gamma);
        double penaltyNumberOfSharedStops = Validator.validateNumberOfSharedPorts(Repository.getItineraries(), delta);
       // System.out.println(penaltyNumberOfSharedStops);
       // AvailableMoves.exchangePorts(itineraries.get(0));
        double penalty = Validator.validateTimeConstraint(itineraries.get(0));
        AvailableMoves.changeSpeeds(itineraries);
        AvailableMoves.changeServiceTime(itineraries);
        AvailableMoves.changeSpeeds(itineraries);
        penalty = Validator.validateTimeConstraint(itineraries.get(0));




        AvailableMoves.changeSpeeds(itineraries);
        AvailableMoves.changeServiceTime(itineraries);
        double penalty1 = Validator.validateTimeConstraint(itineraries.get(0));
        double penalty2 = Validator.validateTimeConstraint(itineraries.get(1));
        double penalty3 = Validator.validateTimeConstraint(itineraries.get(2));
        AvailableMoves.changeSpeeds(itineraries.get(2));
        AvailableMoves.changeServiceTime(itineraries.get(2));
       // AvailableMoves.changeSpeeds(itineraries);
        penalty3 = Validator.validateTimeConstraint(itineraries.get(2));
        AvailableMoves.changeSpeeds(itineraries.get(2));
        AvailableMoves.changeServiceTime(itineraries.get(2));
        penalty3 = Validator.validateTimeConstraint(itineraries.get(2));
        double penalty4 = Validator.validateTimeConstraint(itineraries.get(3));
        AvailableMoves.changeSpeeds(itineraries.get(3));
        AvailableMoves.changeServiceTime(itineraries.get(3));
        penalty4 = Validator.validateTimeConstraint(itineraries.get(3));
        AvailableMoves.exchangePorts(itineraries.get(3), 43);
        penalty4 = Validator.validateTimeConstraint(itineraries.get(3));


        double penalty5 = Validator.validateNumberOfPorts(itineraries, gamma);
        AvailableMoves.changeSpeeds(itineraries);
        double penaltyAll = Validator.validateTimeConstraint(itineraries);
        penalty = Validator.validateTimeConstraint(itineraries.get(0));
        AvailableMoves.changeServiceTime(itineraries);*/

        OptimizeValidSolution.optimize(itineraries, delta);



    }
}
