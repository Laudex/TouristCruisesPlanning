package algorithm;

import model.Itinerary;

import java.util.List;

public class OptimizeNotValidSolution {

    public static void optimize(List<Itinerary> itineraries, int delta){
        double timePenalty = Validator.validateTimeConstraint(itineraries);
        double sharedPenalty = Validator.validateNumberOfSharedPorts(itineraries);
        if (sharedPenalty == 0){
            if (timePenalty > 0.01){
                double newPenalty = optimizeTimeConstraint(itineraries, timePenalty);
                if (newPenalty < timePenalty){
                    timePenalty = newPenalty;
                }
            }
        }
        if (timePenalty <= 0.01){
            double cost = Cost.calculateCost(itineraries);
            System.out.println(cost);
        } else {
            double newTimePenalty = Validator.validateTimeConstraint(itineraries);
            while (newTimePenalty >= 1) {
                optimizeByExchange(itineraries, delta);
                newTimePenalty = Validator.validateTimeConstraint(itineraries);
            }
            double shared = Validator.validateNumberOfSharedPorts(itineraries);
            if (newTimePenalty < 1){
                double cost = Cost.calculateCost(itineraries);
                System.out.println(cost);
            }
        }




    }

    public static double optimizeTimeConstraint(List<Itinerary> itineraries, double penalty){
        for (Itinerary itinerary : itineraries) {
            double currentPenalty = Validator.validateTimeConstraint(itinerary);
            for (int i = 0; i < 5; i++) {
                AvailableMoves.changeServiceTime(itinerary);
                AvailableMoves.changeSpeeds(itinerary);
                AvailableMoves.changeServiceTime(itinerary);
                double newPenalty = Validator.validateTimeConstraint(itinerary);
                if (newPenalty <= 0.1){
                    break;
                }
                if (newPenalty == currentPenalty){
                    break;
                }
                currentPenalty = newPenalty;
                System.out.println(newPenalty);
            }

        }
        double newAllPenalty = Validator.validateTimeConstraint(itineraries);
        return newAllPenalty;
    }

    public static void optimizeByExchange(List<Itinerary> itineraries, int delta){
        //Будем искать маршрут с наибольшей невалидностью и заменять порт в тех местах маршрута, где эта невалидность появляется
        double maxpenalty = 0;
        Itinerary maxPenItinerary = itineraries.get(0);
        for (Itinerary itinerary : itineraries){
            double penaly = Validator.validateTimeConstraint(itinerary);
            if (penaly >= 1 && penaly > maxpenalty){
                maxpenalty = penaly;
                maxPenItinerary = itinerary;
            }
        }
        if (maxpenalty != 0) {
            for (int i = 1; i < maxPenItinerary.getNumberOfStops().size() - 1; i++) {
                AvailableMoves.exchangePorts(maxPenItinerary, maxPenItinerary.getNumberOfStops().get(i).getPortId());
                //AvailableMoves.exchangePorts(maxPenItinerary,43);
                double newPenalty = Validator.validateTimeConstraint(maxPenItinerary);
                System.out.println("New = " + newPenalty);
                if (newPenalty < 1) {
                    break;
                }
            }
        }

    }


}
