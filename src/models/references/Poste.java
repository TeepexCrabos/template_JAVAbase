package models.references;

/**
 * TEMPLATE - Enum COMPLEXE (valeur + libellé) : le sujet montre un tableau
 * Valeur / Libellé. Modèle à renommer : Poste -> TypeVin, CatégorieRayon...
 */
public enum Poste {

    GARDIEN("Gardien de but"),
    DEFENSEUR("Défenseur"),
    MILIEU("Milieu de terrain"),
    ATTAQUANT("Attaquant");

    private final String libelle;

    Poste(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }

    // Redéfinition vue dans le cours Enumérations (exemple TypeArme) :
    // "Constante (libellé)" — pratique si le sujet demande d'afficher les
    // deux. Sinon, getLibelle() seul suffit pour les menus.
    @Override
    public String toString() {
        return String.format("%s (%s)", super.toString(), libelle);
    }
}
