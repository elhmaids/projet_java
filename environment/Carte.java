package simulation.environment;

import simulation.Setting;
import java.util.ArrayList;

public class Carte {
    private final int nbLignes; // Nombre de lignes dans la carte
    private final int nbColones; // Nombre de colonnes dans la carte
    private final Case[][] cases; // Matrice de cases représentant la carte

    private static final ArrayList<Case> sourceEauList = new ArrayList<>(); // Liste des cases d'eau

    /**
     * Constructeur de la classe Carte.
     * Initialise une matrice de cases de dimensions spécifiées.
     *
     * @param nbLignes  nombre de lignes de la carte
     * @param nbColones nombre de colonnes de la carte
     */
    public Carte(int nbLignes, int nbColones) {
        this.nbLignes = nbLignes;
        this.nbColones = nbColones;

        // Initialisation de la matrice des cases
        this.cases = new Case[nbColones][nbLignes];

        // Création des objets Case avec un terrain par défaut
        for (int i = 0; i < nbColones; i++) {
            for (int j = 0; j < nbLignes; j++) {
                cases[i][j] = new Case(i, j, NatureTerrain.TERRAIN_LIBRE);
            }
        }
    }

    /**
     * Définit la nature d'une case spécifique et ajoute les sources d'eau à la liste.
     *
     * @param colone        indice de la colonne de la case
     * @param ligne         indice de la ligne de la case
     * @param natureTerrain type de terrain à affecter à la case
     */
    public void setCaseNature(int colone, int ligne, NatureTerrain natureTerrain) {
        this.cases[colone][ligne].setNature(natureTerrain);
        if (natureTerrain == NatureTerrain.EAU) {
            sourceEauList.add(this.cases[colone][ligne]);
        }
    }

    /**
     * Récupère la case située aux coordonnées spécifiées.
     *
     * @param col indice de la colonne de la case
     * @param lig indice de la ligne de la case
     * @return la case située aux coordonnées (col, lig)
     */
    public Case getCase(int col, int lig) {
        return cases[col][lig];
    }

    /**
     * Retourne le nombre de lignes de la carte.
     *
     * @return le nombre de lignes
     */
    public int getNbLignes() {
        return nbLignes;
    }

    /**
     * Retourne le nombre de colonnes de la carte.
     *
     * @return le nombre de colonnes
     */
    public int getNbColones() {
        return nbColones;
    }

    /**
     * Récupère la case voisine dans une direction donnée.
     *
     * @param c         case de départ
     * @param direction direction dans laquelle rechercher le voisin
     * @return la case voisine dans la direction donnée, ou null si elle n'existe pas
     */
    public Case getVoisin(Case c, Direction direction) {
        Case res = null;
        switch (direction) {
            case OUEST:
                if (c.getColonne() >= 1) {
                    res = cases[c.getColonne() - 1][c.getLigne()];
                }
                break;
            case EST:
                if (c.getColonne() <= this.nbColones - 1) {
                    res = cases[c.getColonne() + 1][c.getLigne()];
                }
                break;
            case NORD:
                if (c.getLigne() >= 1) {
                    res = cases[c.getColonne()][c.getLigne() - 1];
                }
                break;
            case SUD:
                if (c.getLigne() <= this.nbLignes - 1) {
                    res = cases[c.getColonne()][c.getLigne() + 1];
                }
                break;
        }
        return res;
    }

    /**
     * Vérifie si un voisin existe dans une direction donnée à partir de la case source.
     *
     * @param src case de départ
     * @param dir direction de vérification du voisin
     * @return true si un voisin existe dans la direction donnée, false sinon
     */
    public boolean voisinExiste(Case src, Direction dir) {
        int lig = src.getLigne();
        int col = src.getColonne();
        switch (dir) {
            case NORD:
                return lig > 0;
            case SUD:
                return lig < nbLignes - 1;
            case OUEST:
                return col > 0;
            case EST:
                return col < nbColones - 1;
            default:
                return false;
        }
    }

    /**
     * Retourne la taille d'une case, en fonction des paramètres de simulation.
     *
     * @return la taille de la case (en mètres)
     */
    public int getTailleCase() {
        return Setting.DISTANCE_CASE;
    }

    /**
     * Retourne la liste des sources d'eau de la carte.
     *
     * @return liste des cases contenant de l'eau
     */
    public static ArrayList<Case> getSourceEauList() {
        return sourceEauList;
    }

    /**
     * Vérifie si deux cases sont adjacentes.
     *
     * @param a première case
     * @param b deuxième case
     * @return true si les cases sont voisines, false sinon
     */
    public boolean isVoisin(Case a, Case b) {
        return (a.getColonne() == b.getColonne() + 1 && a.getLigne() == b.getLigne()) ||
               (a.getColonne() == b.getColonne() - 1 && a.getLigne() == b.getLigne()) ||
               (a.getColonne() == b.getColonne() && a.getLigne() == b.getLigne() + 1) ||
               (a.getColonne() == b.getColonne() && a.getLigne() == b.getLigne() - 1);
    }
}
