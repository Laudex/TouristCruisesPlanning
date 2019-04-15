package algorithm;

import model.Itinerary;

import java.util.List;

public class OptimizeValidSolution {

    public static void optimize(List<Itinerary> itineraries, int delta){
        double timePenalty = Validator.validateTimeConstraint(itineraries);
        double sharedPenalty = Validator.validateNumberOfSharedPorts(itineraries, delta);
        if (sharedPenalty == 0){
            if (timePenalty > 0.01){
                optimizeTimeConstraint(itineraries, timePenalty);
            }
        }



    }

    public static void optimizeTimeConstraint(List<Itinerary> itineraries, double penalty){
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
        System.out.println(newAllPenalty);
    }

    public static void optimizeShared(List<Itinerary> itineraries){

    }


}
