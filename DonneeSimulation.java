package simulation;

import evenement.Planficateur;
import simulation.environment.*;
import simulation.robot.Robot;
import simulation.robot.RobotChefPompier;
import simulation.robot.RobotState;

import java.util.ArrayList;
import java.util.LinkedList;

public class DonneeSimulation {
    private Carte carte;
    private ArrayList<Incendie> allIncendiesList;

    private LinkedList<Incendie> activeIncendies;

    private LinkedList<Robot> emptyRobots;
    private ArrayList<Robot> robots;

    private RobotChefPompier robotChefPompier;

    private Planficateur planficateur;

    public CalculateurChemin calculateurChemin;

    private int incendieIdCounter = -1;

    public DonneeSimulation(Carte carte) {
        this.carte = carte;
        this.robots = new ArrayList<>();
        this.allIncendiesList = new ArrayList<>();
        this.activeIncendies = new LinkedList<>();
//        this.emptyRobots = new LinkedList<Robot>();
        this.calculateurChemin = new CalculateurChemin(this.carte);
        this.robotChefPompier = new RobotChefPompier(this);
    }

    public void setPlanificateur(Planficateur planficateur){
        this.planficateur = planficateur;
        this.robotChefPompier.setPlanficateur(planficateur);
    }

    public Carte getCarte() {
        return carte;
    }

    public Case getRobotPosition(int i){
        return this.robots.get(i).getPosition();
    }

    public Case getIncendiePosition(int i){
        return this.allIncendiesList.get(i).getPosition();
    }

    public String printRobotInfo(int robotId){
        return this.robots.get(robotId).toString();
    }

    public String printIncendieInfo(int Id){
        return this.allIncendiesList.get(Id).toString();
    }

    public int getNbRobot(){
        return this.robots.size();
    }

    public Robot getRobot(int robotId){
        return this.robots.get(robotId);
    }

    public ArrayList<Robot> getRobots(){
        return this.robots;
    }

    public Incendie getIncendie(int incendieId){
        return this.allIncendiesList.get(incendieId);
    }

    public ArrayList<Incendie> getAllIncendiesList() {
        return allIncendiesList;
    }

    public void addIncendie(Incendie incendie) {
        this.incendieIdCounter++;
        incendie.setId(this.incendieIdCounter);
        this.allIncendiesList.addLast(incendie);
        this.activeIncendies.addLast(incendie);

    }

    public void addRobot(Robot robot) {
        this.robots.addLast(robot);
        this.planficateur.addRobotToFreeList(robot.getId());
    }

    public RobotChefPompier getRobotChefPompier() {
        return robotChefPompier;
    }

    // change robot state, and return estimated moving duration


//    // return null if can not move robot
//    public MoveRobotAction createMoveRobotAction(int robotId, Direction direction){
//        Case destination = this.carte.findVoisin(robots[robotId].getPosition(), direction);
//        if (destination != null && destination.getState() == CaseState.LIBRE){
//            return new MoveRobotAction(robots[robotId], destination, this.carte.getTailleCase());
//        } else {
//            return null;
//        }
//    }

    // return 0 if can not find free robot
    public int findClosestRobot(int incendieId){
        for (int i = 0; i < robots.size(); i++){
            if (robots.get(i).getState() == RobotState.LIBRE) {
                return robots.get(i).getId();
            }
        }
        return 0;
    }



    // cherche libre robot, retourner null si aucun libre
    public Robot chercheRobotLibre(){
        for (int i = 0; i < robots.size(); i++){
            if (robots.get(i).getState() == RobotState.LIBRE && !robots.get(i).reservoirEau_isEmpty()) {
                return robots.get(i);
            }
        }
        return null;
    }

    // cherche active incendie, return -1 if none found
    public Incendie chercheActiveIncendie(){
        // poll return null if list is empty
        return this.activeIncendies.poll();
    }

    // return active incendie Id, return -1 if no found
//    public Incendie checkActiveIncendie(){
//        // poll return null if list is empty
//        if (!this.activeIncendies.isEmpty()){
//            return this.activeIncendies.peek();
//        } else {
//            return null;
//        }
//    }
    public boolean hasActiveIncendie(){
        return !this.activeIncendies.isEmpty();
    }

    public void addIncendieToActiveList(Incendie incendie){
        assert(allIncendiesList != null);
        this.activeIncendies.add(incendie);
        System.out.printf("Donne Simulation >> Add incendie %d to Active List, new total = %d \n", incendie.getId(), this.activeIncendies.size());

    }


    // GNG : obsolete, no use
//    public void addEmptyRobotToList(Robot robot){
//        assert(robot.reservoirEau_isEmpty());
//        this.emptyRobots.add(robot);
//        System.out.printf(" %s >> Add empty Robot no %d to List, new total = %d \n", this.selfPrintColor(), robot.getId(), this.emptyRobots.size());
//
//    }

    public void assignRobotToIncendies(){
        robotChefPompier.assignRobotsToIncendies(this.activeIncendies);
    }



    // verifier si le robot est au voisinage de son incendie cible
    public boolean checkVoisinIncendie(int robotId){
        int incendieId = robots.get(robotId).getTargetIncendieId();
        if (incendieId != -1 ){
            Case incendiePosition = allIncendiesList.get(incendieId).getPosition();
            Case robotPosition = robots.get(robotId).getPosition();
            return this.carte.isVoisin(robotPosition, incendiePosition);
        }
        return false;
    }

    public boolean checkVoisinage(Case a, Case b){
        assert(a != null & b != null);
        return this.carte.isVoisin(a, b);
    }

    public boolean checkRobot_isEmpty(int robotId){
        return this.robots.get(robotId).reservoirEau_isEmpty();
    }

    public void sendEmptiesRobotFillWater(){
        this.robotChefPompier.sendRobotsFillWater();
    }

    public String selfPrintColor() {
        return PrintColor.YELLOW + "DonneSimulation >> " + PrintColor.RESET;
    }

}

