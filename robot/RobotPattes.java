package simulation.robot;

import simulation.environment.*;

public class RobotPattes extends Robot {

    private static final int CAPACITE_EAU = 1000;
    private static final int VITESSE_MAX = 30;

    /**
     * Constructeur pour la classe RobotPattes.
     * Initialise un robot de type pattes avec une capacité d'eau spécifique et une vitesse maximale.
     *
     * @param id        L'identifiant unique du robot
     * @param position  La position initiale du robot sur la carte
     */
    public RobotPattes(int id, Case position) {
        super(id, position, VITESSE_MAX);
        this.versementPerMinute = 600; // 10 L par seconde
        this.reservoirEauCapacity = CAPACITE_EAU;
        this.volEauEnReservoir = this.reservoirEauCapacity;
        this.remplissagePerMinute = 0; // Pas de remplissage
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
                return this.getVitesse(); // Même vitesse en forêt
            case EAU:
                return 0; // Robot immobile sur l'eau
            case ROCHE:
                return 10; // Vitesse réduite sur terrain rocheux
            default:
                return 0; // Terrain inconnu ou inaccessible
        }
    }

    /**
     * Verse de l'eau sur un incendie ciblé.
     * Pour le RobotPattes, le réservoir est considéré comme infini.
     *
     * @param incendie L'incendie sur lequel le robot verse de l'eau
     */
    @Override
    public void verserEauIncendie(Incendie incendie) {
        if (incendie.getEtat() == EtatIncendie.TARGETED) {
            System.out.printf("%s Robot %s : verse %d à l'incendie %s %s\n", 
                              PrintColor.BLUE, this.id, this.versementPerMinute, incendie.getId(), PrintColor.RESET);
            incendie.reduceCoutEau(this.versementPerMinute);
        }
    }
}
