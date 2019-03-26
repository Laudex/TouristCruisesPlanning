package model;

public class ArcSolution {
    private int arcSolutionId;
    private Arc arc;
    private Vessel vessel;
    private Speed speed;

    public ArcSolution(int arcSolutionId, Arc arc, Vessel vessel, Speed speed) {
        this.arcSolutionId = arcSolutionId;
        this.arc = arc;
        this.vessel = vessel;
        this.speed = speed;
    }

    public double calculateTime(){
        return arc.getDistance() / speed.getSpeedValue();
    }

    public double calculateArcCost(){
        double distance = arc.getDistance();
        int speedId = speed.getSpeedId();
        double fuelCost = vessel.getFuelCost().get(speedId);
        double time = distance / speed.getSpeedValue();
        return time * fuelCost;
    }

    public ArcSolution(int arcSolutionId) {
        this.arcSolutionId = arcSolutionId;
    }

    public int getArcSolutionId() {
        return arcSolutionId;
    }

    public void setArcSolutionId(int arcSolutionId) {
        this.arcSolutionId = arcSolutionId;
    }

    public Arc getArc() {
        return arc;
    }

    public void setArc(Arc arc) {
        this.arc = arc;
    }

    public Vessel getVessel() {
        return vessel;
    }

    public void setVessel(Vessel vessel) {
        this.vessel = vessel;
    }

    public Speed getSpeed() {
        return speed;
    }

    public void setSpeed(Speed speed) {
        this.speed = speed;
    }
}
