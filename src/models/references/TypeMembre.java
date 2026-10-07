package models.references;

/**
 * TEMPLATE - Enum de CHOIX DE TYPE pour le menu "ajouter" quand le sujet
 * propose plusieurs sous-classes (type Vin / ProduitBrut / ProduitPrepare).
 * Le Presenter fait un switch dessus pour orienter la saisie - voir
 * Presenter.saisirMembre().
 */
public enum TypeMembre {

    ENTRAINEUR("Entraîneur"),
    JOUEUR("Joueur");

    private final String libelle;

    TypeMembre(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
