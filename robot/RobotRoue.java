package simulation.robot;

import simulation.environment.Case;
import simulation.environment.NatureTerrain;

public class RobotRoue extends Robot {

    private static final int CAPACITE_EAU = 5000;
    private static final int VITESSE_MAX = 80;

    /**
     * Constructeur pour la classe RobotRoue.
     * Initialise un robot de type roues avec une capacité d'eau spécifique et une vitesse maximale.
     *
     * @param id        L'identifiant unique du robot
     * @param position  La position initiale du robot sur la carte
     */
    public RobotRoue(int id, Case position) {
        super(id, position, VITESSE_MAX);
        this.versementPerMinute = 1200; // 100 litres par 5 secondes
        this.reservoirEauCapacity = CAPACITE_EAU;
        this.volEauEnReservoir = this.reservoirEauCapacity;
        this.remplissagePerMinute = 500; // Remplissage complet en 10 minutes
    }

    /**
     * Retourne la vitesse du robot en fonction du type de terrain.
     * Le robot à roues ne peut pas accéder à certains types de terrains comme l'eau et les roches.
     *
     * @param terrain Le type de terrain sur lequel se trouve le robot
     * @return La vitesse du robot sur le terrain donné, ou 0 si le terrain est inaccessible
     */
    @Override
    public int getVitesse(NatureTerrain terrain) {
        switch (terrain) {
            case TERRAIN_LIBRE:
                return VITESSE_MAX; // Vitesse maximale sur terrain libre
            case HABITAT:
                return this.getVitesse(); // Vitesse courante dans l'habitat
            case EAU:
                return 0; // Immobile sur l'eau
            case ROCHE:
                return 0; // Inaccessible sur terrain rocheux
            default:
                return 0; // Terrain inconnu ou inaccessible
        }
    }
}
