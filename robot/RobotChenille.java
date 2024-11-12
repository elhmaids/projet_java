package simulation.robot;

import simulation.environment.Case;
import simulation.environment.NatureTerrain;

public class RobotChenille extends Robot {

    private static final int CAPACITE_EAU = 2000;
    private static final int VITESSE_MAX = 60;
    public RobotChenille(int id, Case position){
         super(id, position, VITESSE_MAX);
         this.versementPerMinute = 900; // 100 litre per 8 second
         this.reservoirEauCapacity = this.CAPACITE_EAU;
         this.volEauEnReservoir = this.reservoirEauCapacity;
         this.remplissagePerMinute = 1000; // remplissage complet en 5 mins
    }


    @Override
    public int getVitesse(NatureTerrain terrain) {
        switch (terrain) {
            case TERRAIN_LIBRE:
                return this.getVitesse(); // Utiliser la vitesse courante du robot
            case FORET:
                return this.getVitesse() / 2; // Diminution de 50% en forêt
            case EAU:
                return 0;
            case ROCHE:
                return 0; // Inaccessible
            default:
                return 0; // Terrain inaccessible
        }
    }

}
