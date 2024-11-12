package simulation;

import simulation.environment.Case;
import simulation.environment.Carte;
import simulation.environment.Direction;
import simulation.environment.NatureTerrain;
import simulation.robot.Robot;
import simulation.robot.RobotDrone;

import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;


public class CalculateurChemin {
    private Carte carte;

    public CalculateurChemin(Carte carte) {
        this.carte = carte;
    }

    public int calculerPlusCourtCheminValue(Robot robot, Case destination) {
        HashMap<Case, Integer> distances = new HashMap<>();
        HashMap<Case, Case> predecesseurs = new HashMap<>();
        PriorityQueue<Case> queue = new PriorityQueue<>(Comparator.comparingInt(distances::get));

        // Initialisation des distances
        for (int i = 0; i < carte.getNbLignes(); i++) {
            for (int j = 0; j < carte.getNbColones(); j++) {
                Case c = carte.getCase(i, j);
                distances.put(c, Integer.MAX_VALUE);
                predecesseurs.put(c, null);
            }
        }

        distances.put(robot.getPosition(), 0);
        queue.add(robot.getPosition());

        boolean cheminFound = false;
        Case current = null;
//        boolean stopAtVoisinDestination = false;
//        Case destination_voisin = null;


        // Algorithme de Dijkstra
        while (!queue.isEmpty() && cheminFound == false) {
            current = queue.poll();

            if (current.equals(destination)) {
                cheminFound = true;
                return distances.get(destination);
//                break;
            }


            for (Direction dir : Direction.values()) {
                Direction dir2 = dir;
                if (carte.voisinExiste(current, dir)) {
                    Case voisin = carte.getVoisin(current, dir);

                    // Calcul de la durée de déplacement
                    int duree = duree_elementaire(robot, current, voisin, carte.getTailleCase());

                    // if destination is voisin of current, and can not go to the destination (eau, incendie),
                    // then stop at current
                    if ( voisin.equals(destination) && duree == -1){
                        cheminFound = true;
                        return distances.get(current);
//                        break;
                    }

                    if ( duree == -1){
                        continue;
                    }

                    int nouvelleDistance = distances.get(current) + duree;


                    if (nouvelleDistance < distances.get(voisin)) {
                        distances.put(voisin, nouvelleDistance);
                        predecesseurs.put(voisin, current);
                        queue.add(voisin);

                    }
                }
            }
        }

        return distances.get(destination);

    }

    private List<Direction> reconstruireChemin(HashMap<Case, Case> predecesseurs, HashMap<Case, Direction> directions, Case depart, Case destination) {
        List<Direction> chemin = new ArrayList<>(); // Liste pour stocker le chemin final
        Case current = destination;

        // Remonte les prédécesseurs depuis la destination jusqu'au départ
        while (current != null && !current.equals(depart)) {
            Direction dir = directions.get(current); // Direction pour atteindre la case courante
            if (dir != null) {
                chemin.add(0, dir); // Ajoute la direction au début pour respecter l'ordre du chemin
            }
            current = predecesseurs.get(current); // Passe à la case précédente dans le chemin
        }

        return chemin; // Retourne le chemin avec les directions dans l'ordre
    }


    private int duree_elementaire(Robot robot, Case depart, Case destination, int tailleCase) {
        NatureTerrain terrainA = depart.getNature();
        NatureTerrain terrainB = destination.getNature();
        int vitesseA = robot.getVitesse(terrainA);
        int vitesseB = robot.getVitesse(terrainB);

        if (vitesseA == 0 || vitesseB == 0) {
//            if (vitesseA == 0 ) {
            //System.out.println("Le robot ne peut pas traverser ce terrain !");
            return -1;
        }

        int vitesseMoyenne = (vitesseA + vitesseB) / 2;
//        int vitesseMoyenne = (vitesseA + vitesseA) / 2;
        return tailleCase*1000 / vitesseMoyenne;
    }

    public List<Direction> calculerPlusCourtChemin2(Robot robot, Case destination) {
        // Map pour stocker la distance minimale entre la position de départ et chaque case
        HashMap<Case, Integer> distances = new HashMap<>();

        // Map pour stocker le prédécesseur de chaque case sur le chemin le plus court
        HashMap<Case, Case> predecesseurs = new HashMap<>();

        // Map pour stocker la direction utilisée pour atteindre chaque case
        HashMap<Case, Direction> directions = new HashMap<>();

        // File de priorité pour extraire les cases selon la distance minimale
        PriorityQueue<Case> queue = new PriorityQueue<>(Comparator.comparingInt(distances::get));

        // Initialisation des distances et prédécesseurs pour chaque case de la carte
        for (int i = 0; i < carte.getNbColones(); i++) {
            for (int j = 0; j < carte.getNbLignes(); j++) {
                Case c = carte.getCase(i, j);
                distances.put(c, Integer.MAX_VALUE); // Distance infinie pour toutes les cases au début
                predecesseurs.put(c, null); // Aucun prédécesseur initialement
                directions.put(c, null); // Aucune direction initialement
            }
        }

        // Initialisation de la position de départ du robot
        distances.put(robot.getPosition(), 0); // La distance de départ est 0
        queue.add(robot.getPosition()); // Ajouter la case de départ dans la file
        int nbCaseExplored = 0;
        boolean cheminFound = false;

        // Boucle principale de l'algorithme de Dijkstra
        while (!queue.isEmpty() && cheminFound == false) {
            // Extraire la case avec la plus petite distance
            Case current = queue.poll();

            // Si on a atteint la destination, on peut arrêter l'algorithme
            if (current.equals(destination)) {
                cheminFound = true;
                break;
            }

            // Explorer les voisins de la case courante dans chaque direction possible
            for (Direction dir : Direction.values()) {
                if (carte.voisinExiste(current, dir)) { // Vérifie si le voisin existe dans la direction donnée
                    Case voisin = carte.getVoisin(current, dir);
                    nbCaseExplored++;

                    // Calcul de la durée de déplacement
                    int duree = duree_elementaire(robot, current, voisin, carte.getTailleCase());


                    if ( voisin.equals(destination) && duree == -1){
                        cheminFound = true;
                        return reconstruireChemin(predecesseurs, directions, robot.getPosition(), current);
//                        break;
                    }

                    // Ignore le voisin si la durée de déplacement est invalide (chemin inaccessible)
                    if (duree == -1) {
                        continue; // Passe au prochain voisin
                    }

                    // Calcul de la nouvelle distance pour atteindre le voisin
                    int nouvelleDistance = distances.get(current) + duree;

                    // Mise à jour de la distance si un chemin plus court est trouvé
                    if (nouvelleDistance < distances.get(voisin)) {
                        distances.put(voisin, nouvelleDistance); // Mise à jour de la distance
                        predecesseurs.put(voisin, current); // Enregistrement du prédécesseur
                        directions.put(voisin, dir); // Enregistrement de la direction
                        queue.add(voisin); // Ajouter le voisin dans la file de priorité
                    }
                }
            }
        }

        // Reconstruction du chemin depuis le départ jusqu'à la destination
        return reconstruireChemin(predecesseurs, directions, robot.getPosition(), destination);
    }

    public List<Direction> calculerPlusCourtChemin3(Robot robot, Case destination) {
        // Map pour stocker la distance minimale entre la position de départ et chaque case
        HashMap<Case, Integer> distances = new HashMap<>();

        // Map pour stocker le prédécesseur de chaque case sur le chemin le plus court
        HashMap<Case, Case> predecesseurs = new HashMap<>();

        // Map pour stocker la direction utilisée pour atteindre chaque case
        HashMap<Case, Direction> directions = new HashMap<>();

        // File de priorité pour extraire les cases selon la distance minimale
        PriorityQueue<Case> queue = new PriorityQueue<>(Comparator.comparingInt(distances::get));

        // Initialisation des distances et prédécesseurs pour chaque case de la carte
        for (int i = 0; i < carte.getNbLignes(); i++) {
            for (int j = 0; j < carte.getNbColones(); j++) {
                Case c = carte.getCase(i, j);
                distances.put(c, Integer.MAX_VALUE); // Distance infinie pour toutes les cases au début
                predecesseurs.put(c, null); // Aucun prédécesseur initialement
                directions.put(c, null); // Aucune direction initialement
            }
        }

        // Initialisation de la position de départ du robot
        distances.put(robot.getPosition(), 0); // La distance de départ est 0
        queue.add(robot.getPosition()); // Ajouter la case de départ dans la file

        // Boucle principale de l'algorithme de Dijkstra
        while (!queue.isEmpty()) {
            // Extraire la case avec la plus petite distance
            Case current = queue.poll();

            // Si on a atteint la destination, on peut arrêter l'algorithme
            if (current.equals(destination)) {
                break;
            }

            // Explorer les voisins de la case courante dans chaque direction possible
            for (Direction dir : Direction.values()) {
                if (carte.voisinExiste(current, dir)) { // Vérifie si le voisin existe dans la direction donnée
                    Case voisin = carte.getVoisin(current, dir);

                    // Calcul de la durée de déplacement
                    int duree = duree_elementaire(robot, current, voisin, carte.getTailleCase());

                    // Ignore le voisin si la durée de déplacement est invalide (chemin inaccessible)
                    if (duree == -1 && !voisin.equals(destination)) {
                        continue; // Passe au prochain voisin, sauf si c'est la destination
                    }

                    // Calcul de la nouvelle distance pour atteindre le voisin
                    int nouvelleDistance = distances.get(current) + duree;

                    // Mise à jour de la distance si un chemin plus court est trouvé
                    if (nouvelleDistance < distances.get(voisin)) {
                        distances.put(voisin, nouvelleDistance); // Mise à jour de la distance
                        predecesseurs.put(voisin, current); // Enregistrement du prédécesseur
                        directions.put(voisin, dir); // Enregistrement de la direction
                        queue.add(voisin); // Ajouter le voisin dans la file de priorité
                    }
                }
            }
        }

        // Appliquer des règles spéciales en fonction du type de robot et de la destination
        if (!(robot instanceof RobotDrone) && (destination.getNature() == NatureTerrain.EAU || destination.hasActiveIncendie())) {
            // Si ce n'est pas un drone et que la destination est de l'eau ou un incendie,
            // on modifie la destination pour être la case avant-dernière
            destination = predecesseurs.get(destination);
        }

        // Reconstruction du chemin depuis le départ jusqu'à la destination (case avant-dernière si nécessaire)
        return reconstruireChemin(predecesseurs, directions, robot.getPosition(), destination);

    }
}

