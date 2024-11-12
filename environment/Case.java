package simulation.environment;

import simulation.robot.Robot;

public class Case {
    private int ligne; // Ligne de la case dans la carte
    private int colonne; // Colonne de la case dans la carte
    private NatureTerrain nature; // Nature du terrain de la case (e.g., EAU, TERRAIN_LIBRE)
    private Incendie incendie; // Incendie présent sur la case, s'il y en a un
    private CaseState state; // État de la case (libre ou occupée)

    /**
     * Constructeur de la classe Case.
     * Initialise une case avec ses coordonnées, sa nature de terrain et un état par défaut libre.
     *
     * @param colonne colonne de la case dans la carte
     * @param ligne   ligne de la case dans la carte
     * @param nature  nature du terrain de la case
     */
    public Case(int colonne, int ligne, NatureTerrain nature) {
        this.ligne = ligne;
        this.colonne = colonne;
        this.nature = nature;
        this.state = CaseState.LIBRE;
        this.incendie = null;
    }

    /**
     * Retourne la ligne de la case.
     *
     * @return la ligne de la case
     */
    public int getLigne() {
        return ligne;
    }

    /**
     * Retourne la colonne de la case.
     *
     * @return la colonne de la case
     */
    public int getColonne() {
        return colonne;
    }

    /**
     * Retourne l'incendie présent sur la case, s'il y en a un.
     *
     * @return l'incendie sur la case, ou null si aucun incendie n'est présent
     */
    public Incendie getIncendie() {
        return incendie;
    }

    /**
     * Retourne la nature du terrain de la case.
     *
     * @return la nature du terrain de la case
     */
    public NatureTerrain getNature() {
        return nature;
    }

    /**
     * Définit la nature du terrain de la case.
     *
     * @param nature nouvelle nature du terrain pour la case
     */
    public void setNature(NatureTerrain nature) {
        this.nature = nature;
    }

    /**
     * Retourne l'état actuel de la case (libre ou occupée).
     *
     * @return l'état de la case
     */
    public CaseState getState() {
        return state;
    }

    /**
     * Définit l'état de la case.
     *
     * @param state nouvel état de la case
     */
    public void setState(CaseState state) {
        this.state = state;
    }

    /**
     * Définit l'incendie sur la case.
     *
     * @param incendie incendie à affecter à la case
     */
    public void setIncendie(Incendie incendie) {
        this.incendie = incendie;
    }

    /**
     * Vérifie si un incendie actif est présent sur la case.
     *
     * @return true si un incendie actif est présent, false sinon
     */
    public boolean hasActiveIncendie() {
        return (this.incendie != null && this.incendie.getEtat() == EtatIncendie.ACTIVE);
    }

    /**
     * Retourne une représentation textuelle de la case pour le débogage.
     *
     * @return chaîne de caractères représentant les coordonnées de la case
     */
    @Override
    public String toString() {
        return "Case [" + colonne + "; " + ligne + "]";
    }
}
