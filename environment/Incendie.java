package simulation.environment;

public class Incendie {
    private int id;
    private Case position;

    // Quantité d'eau nécessaire pour éteindre l'incendie
    private int coutEau;
    private int coutEauInitial;
    private EtatIncendie etat;

    /**
     * Constructeur de la classe Incendie.
     * Initialise l'incendie à une position donnée avec un coût en eau spécifique.
     * 
     * @param coutEau La quantité d'eau nécessaire pour éteindre l'incendie
     * @param position La position de l'incendie sur la carte
     */
    public Incendie(int coutEau, Case position) {
        this.position = position;
        this.coutEau = coutEau;
        this.coutEauInitial = coutEau;
        this.etat = EtatIncendie.ACTIVE;
        this.position.setIncendie(this);
    }

    /**
     * Retourne la position de l'incendie.
     * 
     * @return La case où se trouve l'incendie
     */
    public Case getPosition() {
        return position;
    }

    /**
     * Définit l'identifiant unique de l'incendie.
     * 
     * @param id L'identifiant unique de l'incendie
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Retourne la quantité d'eau restante nécessaire pour éteindre l'incendie.
     * 
     * @return La quantité d'eau en litres pour éteindre l'incendie
     */
    public int getCoutEau() {
        return coutEau;
    }

    /**
     * Réduit la quantité d'eau nécessaire pour éteindre l'incendie en fonction de la quantité d'eau reçue.
     * Si la quantité d'eau reçue est suffisante pour éteindre l'incendie, son état passe à "ETEINT".
     * 
     * @param quteEau La quantité d'eau reçue
     * @return La quantité d'eau réellement utilisée pour réduire le coût de l'incendie
     */
    public int reduceCoutEau(int quteEau) {
        if (quteEau < this.coutEau) {
            this.coutEau -= quteEau;
            System.out.printf("%s Incendie %s: reçoit %d eau, coût actuel = %d \n %s", PrintColor.BLUE, this.id, quteEau, this.coutEau, PrintColor.RESET);
            return quteEau;
        } else {
            this.coutEau = 0;
            this.setEtat(EtatIncendie.ETEINT);
            this.position.setIncendie(null);
            System.out.printf("%s Incendie %d : éteint \n %s", PrintColor.YELLOW, this.id, PrintColor.RESET);
            return this.coutEau;
        }
    }

    /**
     * Modifie la position de l'incendie.
     * 
     * @param position La nouvelle position de l'incendie
     */
    public void setPosition(Case position) {
        this.position = position;
    }

    /**
     * Retourne l'état actuel de l'incendie.
     * 
     * @return L'état de l'incendie (ACTIVE, TARGETED, ETEINT)
     */
    public EtatIncendie getEtat() {
        return etat;
    }

    /**
     * Modifie l'état de l'incendie.
     * 
     * @param etat Le nouvel état de l'incendie
     */
    private void setEtat(EtatIncendie etat) {
        this.etat = etat;
    }

    /**
     * Marque l'incendie comme "ciblé" par un robot.
     */
    public void setEtatToTarget() {
        this.etat = EtatIncendie.TARGETED;
    }

    /**
     * Retourne l'identifiant unique de l'incendie.
     * 
     * @return L'identifiant de l'incendie
     */
    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Incendie{" +
                "id=" + id +
                ", coutEau=" + coutEau +
                ", etat=" + etat +
                '}';
    }

    /**
     * Calcule le pourcentage d'extinction de l'incendie en fonction de l'eau utilisée.
     * 
     * @return Le pourcentage d'extinction de l'incendie
     */
    public int getPercetageExtinction() {
        return (this.coutEau * 100) / this.coutEauInitial;
    }
}
