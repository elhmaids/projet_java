package simulation.robot;

import simulation.environment.Case;
import simulation.environment.NatureTerrain;

public class RobotChenille extends Robot {

    private static final int CAPACITE_EAU = 2000;
    private static final int VITESSE_MAX = 60;

    /**
     * Constructeur pour la classe RobotChenille.
     * Initialise un robot de type chenille avec une capacité d'eau et une vitesse maximale spécifiques.
     *
     * @param id        L'identifiant unique du robot
     * @param position  La position initiale du robot sur la carte
     */
    public RobotChenille(int id, Case position) {
        super(id, position, VITESSE_MAX);
        this.versementPerMinute = 900; // 100 litres versés en 8 secondes
        this.reservoirEauCapacity = CAPACITE_EAU;
        this.volEauEnReservoir = this.reservoirEauCapacity;
        this.remplissagePerMinute = 1000; // remplissage complet en 5 minutes
    }

    /**
     * Retourne la vitesse du robot en fonction du type de terrain.
     *
     * @param terrain Le type de terrain sur lequel se trouve le robot
     * @return La vitesse du robot sur le terrain donné
     */
    @Override
    public int getVitesse(NatureTerrain terrain) {
        switch (terrain) {
            case TERRAIN_LIBRE:
                return this.getVitesse(); // Vitesse maximale sur terrain libre
            case FORET:
                return this.getVitesse() / 2; // Vitesse réduite de 50% en forêt
            case EAU:
                return 0; // Robot immobile sur l'eau
            case ROCHE:
                return 0; // Terrain rocheux inaccessible
            default:
                return 0; // Terrain inconnu, inaccessibilité par défaut
        }
    }
}
