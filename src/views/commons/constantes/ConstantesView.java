package views.commons.constantes;

import java.util.List;

/**
 * TEMPLATE - Options du menu principal, centralisées dans une liste.
 * Recopier ici, dans l'ordre exact du sujet, le libellé de chaque option
 * (SANS le numéro ni le "0 - sortir" : ils sont générés automatiquement).
 *
 * Le numéro affiché pour chaque option correspond à sa POSITION dans la
 * liste (1 pour le 1er élément, 2 pour le 2e...) - garder cet ordre en tête
 * pour que les "case" du switch dans Presenter.start() correspondent.
 *
 * Les 5 lignes ci-dessous correspondent aux 5 types d'actions récurrents
 * observés sur les sujets précédents (cyclisme, foot) - retirer celles qui
 * n'existent pas dans le nouveau sujet, ou en ajouter (type "créer un
 * sous-niveau intermédiaire", ou "afficher un sous-ensemble filtré/bonus").
 */
/*
 * FRONTIERE (à savoir justifier) : ici, UNIQUEMENT la STRUCTURE du menu
 * (titre, libellés des options, libellé sortie) - notée sur sa conformité
 * exacte au sujet et lue par la vue pour déduire la borne du choix.
 * Les messages répétés du Presenter sont des constantes privées DU
 * Presenter (pas de dépendance presenter -> views.commons hors façade).
 * Les prompts et messages uniques restent inline : les centraliser
 * n'apporte que de l'indirection.
 */
public final class ConstantesView {

    private ConstantesView() {
    }

    public static final String MENU_TITRE = "GESTION DES CLUBS";

    public static final List<String> OPTIONS_MENU = List.of(
            "Créer un club",                    // TODO libellés EXACTS du sujet
            "Créer un membre",
            "Ajouter un membre dans un club",
            "Afficher les membres d'un club",
            "Supprimer un membre d'un club",
            "Supprimer un club",
            "Afficher les joueurs titulaires",
            "Passer un joueur titulaire",
            "Afficher les joueurs d'un club triés",
            "Afficher les statistiques d'un club"
    );

    public static final String LIBELLE_SORTIE = "sortir";
}
