package model;

import service.data.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Solution {
    // PortId - VesselId
    private static Map<Integer, Integer> solution = new HashMap<>();

    private static List<ArcSolution> arcs = new ArrayList<>();

    public static Map<Integer, Integer> getSolution() {
        return solution;
    }

    /*public static double calculateObjFunction(){
        double arcCost = ArcSolution.calculateArcCost();
        double serviceCost = 0;
        for (Map.Entry<Integer, Integer> entry : solution.entrySet()){ ;
            Port port = Repository.getPorts().get(entry.getKey() - 1);
            Vessel vessel = Repository.getVessels().get(entry.getValue() - 1);
            double preAndPostCost = port.getPreAndPostCost().get(vessel.getId());
            double fixedCost = port.getFixedCost().get(vessel.getId());
            double serviceTime = vessel.getServiceTime().get(port.getPortId());
            double inServiceCost = port.getInServiceCost().get(vessel.getId());
            serviceCost = serviceCost + preAndPostCost + fixedCost + serviceTime * inServiceCost;
        }

        return arcCost + serviceCost;
    }*/
}
