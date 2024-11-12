package simulation.robot;

import evenement.Planficateur;
import simulation.DonneeSimulation;
import simulation.environment.*;

import java.util.*;

public class RobotChefPompier extends Robot {
    private DonneeSimulation donneeSimulation;
    private Planficateur planficateur;

    /**
     * Constructeur de la classe RobotChefPompier.
     * Initialise un chef pompier sans position spécifique.
     *
     * @param donneeSimulation L'objet DonneeSimulation contenant les données de la simulation
     */
    public RobotChefPompier(DonneeSimulation donneeSimulation) {
        super(-1, null, 0);
        this.donneeSimulation = donneeSimulation;
        this.planficateur = null;
    }

    /**
     * Définit le planificateur utilisé par le chef pompier pour affecter les trajets.
     *
     * @param planficateur Le planificateur des trajets des robots
     */
    public void setPlanficateur(Planficateur planficateur) {
        this.planficateur = planficateur;
    }

    /**
     * Cherche un robot libre dans la simulation.
     *
     * @return Un robot libre ou null si aucun robot libre n'est disponible
     */
    public Robot chercheRobotLibre() {
        return donneeSimulation.chercheRobotLibre();
    }

    /**
     * Assigne des robots libres aux incendies actifs.
     *
     * @param activeIncendies Liste des incendies actifs
     */
    public void assignRobotsToIncendies(LinkedList<Incendie> activeIncendies) {
        while (!activeIncendies.isEmpty()) {
            if (!planficateur.robotFreeList_isEmpty()) {
                Robot robot = donneeSimulation.getRobot(planficateur.getFirstFreeRobot());
                Incendie closest_incendie = this.chercheIncendieLePlusProche(robot, activeIncendies);
                boolean removeIncendie_success = activeIncendies.remove(closest_incendie);
                assert (removeIncendie_success);
                this.assignSingleRobotToIncendie(robot, closest_incendie);
            } else {
                System.out.printf("Chef Pompier : pas de Robot libre pour affecter à l'incendie No %d\n", activeIncendies.peek().getId());
                break;
            }
        }
    }

    /**
     * Cherche le robot le plus proche d'un incendie donné.
     *
     * @param incendie L'incendie cible
     * @param libreRobotList Liste des robots libres
     * @return Le robot le plus proche de l'incendie
     */
    private Robot chercheRobotLePlusProche(Incendie incendie, LinkedList<Integer> libreRobotList) {
        int min = Integer.MAX_VALUE;
        Robot selected_robot = null;
        for (Integer robotId : libreRobotList) {
            Robot robot = donneeSimulation.getRobot(robotId);
            int temps_trajet = donneeSimulation.calculateurChemin.calculerPlusCourtCheminValue(robot, incendie.getPosition());
            if (temps_trajet < min) {
                min = temps_trajet;
                selected_robot = robot;
            }
        }
        return selected_robot;
    }

    /**
     * Cherche l'incendie le plus proche d'un robot donné.
     *
     * @param robot Le robot en question
     * @param activeIncendies Liste des incendies actifs
     * @return L'incendie le plus proche
     */
    private Incendie chercheIncendieLePlusProche(Robot robot, LinkedList<Incendie> activeIncendies) {
        int min = Integer.MAX_VALUE;
        Incendie selected_incendie = null;
        for (Incendie incendie : activeIncendies) {
            int temps_trajet = donneeSimulation.calculateurChemin.calculerPlusCourtCheminValue(robot, incendie.getPosition());
            if (temps_trajet < min) {
                min = temps_trajet;
                selected_incendie = incendie;
            }
        }
        return selected_incendie;
    }

    /**
     * Assigne un robot à un incendie spécifique.
     *
     * @param robot Le robot à assigner
     * @param incendie L'incendie à éteindre
     * @return True si l'assignation a réussi, sinon False
     */
    public boolean assignSingleRobotToIncendie(Robot robot, Incendie incendie) {
        robot.setTargetIncendieId(incendie.getId());
        incendie.setEtatToTarget();
        System.out.printf("Chef Pompier: Affect robot %d to Incendie %d \n", robot.getId(), incendie.getId());
        RobotActionTypeEnum[] trajet = this.getRobotTrajet(robot, incendie.getPosition(), RobotActionTypeEnum.ETEINDRE_INCENDIE);
        if (trajet == null) {
            return false;
        }
        planficateur.addRobotTrajet(robot.getId(), trajet);
        return true;
    }

    /**
     * Envoie les robots avec un réservoir vide vers la source d'eau la plus proche.
     */
    public void sendRobotsFillWater() {
        while (planficateur.hasEmptyRobot()) {
            int robotId = planficateur.getEmptyRobotFromList();
            Robot robot = donneeSimulation.getRobot(robotId);
            this.sendSingleRobotFillWater(robot);
        }
    }

    /**
     * Cherche la source d'eau la plus proche d'un robot.
     *
     * @param robot Le robot qui cherche une source d'eau
     * @return La case contenant la source d'eau la plus proche
     */
    private Case chercheSourceEauPlusProche(Robot robot) {
        Case res = null;
        ArrayList<Case> sourceEauList = Carte.getSourceEauList();
        if (sourceEauList.isEmpty()) {
            return res;
        }
        int min = Integer.MAX_VALUE;
        for (Case sourceEau : sourceEauList) {
            int temps_trajet = donneeSimulation.calculateurChemin.calculerPlusCourtCheminValue(robot, sourceEau);
            if (min > temps_trajet) {
                min = temps_trajet;
                res = sourceEau;
            }
        }
        return res;
    }

    /**
     * Envoie un robot spécifique à remplir son réservoir d'eau à la source la plus proche.
     *
     * @param robot Le robot à envoyer
     * @return True si l'envoi a réussi, sinon False
     */
    public boolean sendSingleRobotFillWater(Robot robot) {
        if (robot != null) {
            Case sourceEau = this.chercheSourceEauPlusProche(robot);
            if (sourceEau == null) {
                System.out.printf("Chef Pompier: try to send robot %d find water, no water source found");
                return false;
            }
            System.out.printf(" %s : send robot %d to find water at sourceEau %s \n", this.toString(), robot.getId(), sourceEau);
            RobotActionTypeEnum[] trajet = getRobotTrajet(robot, sourceEau, RobotActionTypeEnum.REMPLIR_EAU);
            robot.setTargetSourceEau(sourceEau);
            planficateur.addRobotTrajet(robot.getId(), trajet);
            return true;
        } else {
            System.out.printf(" %s : robot null \n", this.toString());
            return false;
        }
    }

    /**
     * Génère le trajet d'un robot vers une cible avec une action finale spécifiée.
     *
     * @param robot Le robot en déplacement
     * @param target La case cible
     * @param action L'action finale du robot
     * @return Un tableau de directions pour le trajet
     */
    public RobotActionTypeEnum[] getRobotTrajet(Robot robot, Case target, RobotActionTypeEnum action) {
        List<Direction> list_direction = donneeSimulation.calculateurChemin.calculerPlusCourtChemin3(robot, target);
        if (list_direction == null) {
            System.out.printf("Robot Chef Pompier: No trajet found for %s to get to %s", robot, target);
            return null;
        }
        RobotActionTypeEnum[] trajet = ConvertDirectionToMoveAction(list_direction, action);
        return trajet;
    }

    /**
     * Envoie un robot vers une case spécifique.
     *
     * @param robot Le robot en déplacement
     * @param destination La destination cible
     */
    public void sendRobotToCase(Robot robot, Case destination) {
        if (robot != null) {
            System.out.printf("Chef Pompier : send robot %s to Case %s \n", robot.toString(), destination.toString());
            RobotActionTypeEnum[] trajet = getRobotTrajet(robot, destination, null);
            planficateur.addRobotTrajet(robot.getId(), trajet);
        } else {
            System.out.printf(" %s : robot null \n", this.toString());
        }
    }

    /**
     * Convertit une liste de directions en actions de mouvement pour le robot.
     *
     * @param list_direction Liste des directions à suivre
     * @param lastAction_to_add Action finale (e.g., ETEINDRE_INCENDIE, REMPLIR_EAU)
     * @return Un tableau d'actions de mouvement pour le trajet
     */
    RobotActionTypeEnum[] ConvertDirectionToMoveAction(List<Direction> list_direction, RobotActionTypeEnum lastAction_to_add) {
        List<RobotActionTypeEnum> trajet = new ArrayList<>();
        for (Direction direction : list_direction) {
            switch (direction) {
                case EST -> trajet.add(RobotActionTypeEnum.MOVE_EST);
                case OUEST -> trajet.add(RobotActionTypeEnum.MOVE_OUEST);
                case NORD -> trajet.add(RobotActionTypeEnum.MOVE_NORD);
                case SUD -> trajet.add(RobotActionTypeEnum.MOVE_SUD);
            }
        }
        if (lastAction_to_add == RobotActionTypeEnum.ETEINDRE_INCENDIE || lastAction_to_add == RobotActionTypeEnum.REMPLIR_EAU) {
            trajet.add(lastAction_to_add);
        }
        return trajet.toArray(new RobotActionTypeEnum[0]);
    }

    @Override
    public int getVitesse(NatureTerrain terrain) {
        return 0;
    }

    @Override
    public String toString() {
        return PrintColor.RED + "ChefPompier" + PrintColor.RESET;
    }
}
