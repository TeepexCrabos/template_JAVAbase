package models.references;

/**
 * TEMPLATE - Résultat d'une opération métier qui peut échouer de Plusieurs
 * façons (contrairement à un boolean qui ne dit pas POURQUOI ça a échoué).
 * Le Presenter affiche un message précis par cause via un switch.
 * (ex : TROP_DE_TITULAIRES, MANGA_RAYON_INTERDIT...).
 */
public enum Resultat {
    OK,
    DEJA_EXISTANT,
    EFFECTIF_COMPLET,
    TROP_DE_TITULAIRES,
    DEJA_UN_GARDIEN_TITULAIRE,
    DEJA_DANS_UN_CLUB,
    TROP_D_ENTRAINEURS,
    DEJA_TITULAIRE,
    INTROUVABLE
}
