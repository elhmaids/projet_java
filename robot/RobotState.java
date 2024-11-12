package simulation.robot;

public enum RobotState {
    LIBRE,
    EN_ATTENTE, // finir un etape du trajet, attendre prochain etape
    MOVING,
    REMPLIT_EAU,
    ETEINDRE_INCENDIE,
}
