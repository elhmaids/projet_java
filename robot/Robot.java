package simulation.robot;

import evenement.Evenement;
import simulation.environment.*;

public abstract class Robot {
    protected int id;
    protected int reservoirEauCapacity; // Capacité totale du réservoir d'eau en litres
    protected int volEauEnReservoir; // Volume actuel d'eau dans le réservoir en litres
    protected int versementPerMinute; // Quantité d'eau versée par minute en litres
    protected int remplissagePerMinute; // Quantité d'eau remplie par minute en litres
    private Case position; // Position actuelle du robot
    protected int vitesse; // Vitesse du robot

    private RobotState state; // État actuel du robot
    private Evenement currentAction; // Action en cours du robot

    private int targetIncendieId; // Identifiant de l'incendie cible
    private Case targetSourceEau; // Source d'eau cible pour remplir le réservoir

    /**
     * Constructeur de la classe Robot.
     * Initialise un robot avec un identifiant, une position, et une vitesse.
     *
     * @param id Identifiant unique du robot
     * @param position Position initiale du robot
     * @param vitesse Vitesse du robot en km/h
     */
    public Robot(int id, Case position, int vitesse) {
        this.position = position;
        this.id = id;
        this.vitesse = vitesse;
        this.state = RobotState.LIBRE;
        this.currentAction = null;
        this.targetIncendieId = -1;
    }

    /**
     * Obtient la source d'eau cible du robot.
     *
     * @return La case contenant la source d'eau cible
     */
    public Case getTargetSourceEau() {
        return targetSourceEau;
    }

    /**
     * Définit la source d'eau cible pour le robot.
     *
     * @param targetSourceEau La case source d'eau cible
     */
    public void setTargetSourceEau(Case targetSourceEau) {
        this.targetSourceEau = targetSourceEau;
    }

    /**
     * Obtient la position actuelle du robot.
     *
     * @return La case actuelle où se trouve le robot
     */
    public Case getPosition() {
        return this.position;
    }

    /**
     * Déplace le robot vers une nouvelle position si la case de destination est libre.
     *
     * @param destination La case de destination du robot
     */
    public void setPosition(Case destination) {
        this.position.liberate(); // Libère la case actuelle
        this.position = destination;
        this.setState(RobotState.LIBRE);
        destination.setOccupyingRobot(this); // Définit le robot comme occupant de la nouvelle case
    }

    /**
     * Méthode abstraite pour obtenir la vitesse du robot sur un type de terrain donné.
     *
     * @param terrain Le type de terrain
     * @return La vitesse du robot sur le terrain
     */
    public abstract int getVitesse(NatureTerrain terrain);

    /**
     * Obtient la vitesse actuelle du robot.
     *
     * @return La vitesse du robot
     */
    public int getVitesse() {
        return this.vitesse;
    }

    /**
     * Définit la vitesse du robot.
     *
     * @param vitesse La nouvelle vitesse du robot
     */
    public void setVitesse(int vitesse) {
        this.vitesse = vitesse;
    }

    /**
     * Obtient l'identifiant du robot.
     *
     * @return L'identifiant unique du robot
     */
    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "{" +
                "Robot " + id +
                this.position.toString() +
                '}';
    }

    /**
     * Obtient le volume d'eau actuel dans le réservoir du robot.
     *
     * @return Le volume d'eau en litres
     */
    public int getVolEauEnReservoir() {
        return volEauEnReservoir;
    }

    /**
     * Remplit le réservoir d'eau du robot, en ajoutant une quantité d'eau par minute
     * jusqu'à atteindre la capacité maximale.
     */
    public void remplirReservoir() {
        if (this.volEauEnReservoir + this.remplissagePerMinute >= this.reservoirEauCapacity) {
            this.volEauEnReservoir = this.reservoirEauCapacity;
            System.out.printf("%s Robot %s : reservoir Full %s\n", PrintColor.BLUE, this.id, PrintColor.RESET);
        } else {
            this.volEauEnReservoir += this.remplissagePerMinute;
            System.out.printf("%s Robot %s : remplir +%d dans reservoir %s\n", PrintColor.BLUE, this.id, this.remplissagePerMinute, PrintColor.RESET);
        }
    }

    /**
     * Obtient l'état actuel du robot.
     *
     * @return L'état du robot (e.g., LIBRE, OCCUPE)
     */
    public RobotState getState() {
        return state;
    }

    /**
     * Définit l'état actuel du robot.
     *
     * @param state Le nouvel état du robot
     */
    public void setState(RobotState state) {
        this.state = state;
    }

    /**
     * Obtient l'identifiant de l'incendie cible.
     *
     * @return L'identifiant de l'incendie cible
     */
    public int getTargetIncendieId() {
        return targetIncendieId;
    }

    /**
     * Définit l'identifiant de l'incendie cible pour le robot.
     *
     * @param targetIncendieId L'identifiant de l'incendie cible
     */
    public void setTargetIncendieId(int targetIncendieId) {
        this.targetIncendieId = targetIncendieId;
    }

    /**
     * Obtient l'action en cours du robot.
     *
     * @return L'événement actuel du robot
     */
    public Evenement getCurrentAction() {
        return currentAction;
    }

    /**
     * Définit l'action en cours pour le robot.
     *
     * @param currentEvent L'événement en cours
     */
    public void setCurrentEvent(Evenement currentEvent) {
        this.currentAction = currentEvent;
    }

    /**
     * Vérifie si le réservoir d'eau est vide.
     *
     * @return True si le réservoir est vide, sinon False
     */
    public boolean reservoirEau_isEmpty() {
        return this.volEauEnReservoir == 0;
    }

    /**
     * Vérifie si le réservoir d'eau est plein.
     *
     * @return True si le réservoir est plein, sinon False
     */
    public boolean reservoirEau_isFull() {
        return this.volEauEnReservoir == this.reservoirEauCapacity;
    }

    /**
     * Obtient la quantité d'eau versée par minute par le robot.
     *
     * @return La quantité d'eau versée par minute
     */
    public int getVersementPerMinute() {
        return this.versementPerMinute;
    }

    /**
     * Définit le volume d'eau actuel dans le réservoir du robot.
     *
     * @param volEauEnReservoir Le volume d'eau en litres
     */
    public void setVolEauEnReservoir(int volEauEnReservoir) {
        this.volEauEnReservoir = volEauEnReservoir;
    }

    /**
     * Obtient la quantité d'eau remplie par minute.
     *
     * @return La quantité d'eau remplie par minute
     */
    public int getRemplissagePerMinute() {
        return remplissagePerMinute;
    }

    /**
     * Obtient la capacité totale du réservoir d'eau.
     *
     * @return La capacité du réservoir en litres
     */
    public int getReservoirEauCapacity() {
        return reservoirEauCapacity;
    }

    /**
     * Verse de l'eau sur l'incendie pour le réduire. 
     * L'état de l'incendie passe à ETEINT si le volume versé est suffisant.
     *
     * @param incendie L'incendie cible
     */
    public void verserEauIncendie(Incendie incendie) {
        int versementEffectif;
        if (versementPerMinute >= this.volEauEnReservoir) {
            System.out.printf("%s Robot %s: Reservoir emptied \n %s", PrintColor.YELLOW, this.id, PrintColor.RESET);
            versementEffectif = this.volEauEnReservoir;
            this.volEauEnReservoir = 0;
        } else {
            this.volEauEnReservoir -= this.versementPerMinute;
            versementEffectif = this.versementPerMinute;
        }
        if (incendie.getEtat() == EtatIncendie.TARGETED) {
            System.out.printf("%s Robot %s : verse %d to incendie %s %s\n", PrintColor.BLUE, this.id, versementEffectif, incendie.getId(), PrintColor.RESET);
            incendie.reduceCoutEau(versementEffectif);
        }
    }

    /**
     * Calcule le pourcentage du niveau d'eau restant dans le réservoir.
     *
     * @return Le pourcentage de l'eau restante
     */
    public int getPercetageNiveauEau() {
        return (this.volEauEnReservoir * 100) / this.reservoirEauCapacity;
    }
}
