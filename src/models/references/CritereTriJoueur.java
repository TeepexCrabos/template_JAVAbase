package models.references;

/**
 * TEMPLATE - Enum de CHOIX DE TRI pour le menu "afficher les joueurs
 * triés" : chaque valeur correspond à une fabrique de ComparateurUtils
 * (voir Presenter.comparateurPour). Ajouter un critère = une valeur ici
 * + un case dans le switch.
 */
public enum CritereTriJoueur {

    NOM("Nom (A -> Z)"),
    TAILLE_CROISSANTE("Taille croissante"),
    TAILLE_DECROISSANTE("Taille décroissante"),
    AGE("Âge (du plus jeune au plus âgé)"),
    NUMERO("Numéro de maillot");

    private final String libelle;

    CritereTriJoueur(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
