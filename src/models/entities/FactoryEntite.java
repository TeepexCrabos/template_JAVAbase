package models.entities;

import models.exception.MetierException;
import models.references.Poste;

import java.time.LocalDate;

/**
 * TEMPLATE - Factory : centralise la création des entités pour que le
 * Presenter n'écrive jamais "new Joueur(...)" directement.

 * ATTENTION : une fois créée, l'UTILISER réellement dans le Presenter -
 * sinon elle ne compte pas comme "principe mis en œuvre".
 */
public final class FactoryEntite {

    private FactoryEntite() {
    }

    public static Entraineur creerEntraineur(String nom, String prenom, LocalDate dateNaissance) throws MetierException {
        return new Entraineur(nom, prenom, dateNaissance);
    }

    public static Joueur creerJoueur(String nom, String prenom, int numero, double taille,
                                     LocalDate dateNaissance, Poste poste, boolean titulaire) throws MetierException {
        return new Joueur(nom, prenom, numero, taille, dateNaissance, poste, titulaire);
    }
}
