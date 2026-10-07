package models.facadeModel;

import models.entities.Club;

import models.entities.Joueur;
import models.entities.Personne;
import models.exception.MetierException;
import models.references.Niveau;
import models.references.Resultat;

import java.time.LocalDate;
import java.util.List;

/**
 * TEMPLATE - Interface de la Façade métier : une méthode par action du
 * menu. Renommer selon les entités réelles du sujet.

 * Trois façons de signaler un échec (voir README section "Validation") :
 * - créerClub : COMBO - le constructeur peut lever IllegalArgumentException
 *   (champ), la méthode retourne un Resultat (doublon).
 * - ajouterMembre : Resultat (règle de COLLECTION - quota, doublon).
 * - MetierException (checked) si le sujet la demande : le "throws" se
 *   déclare ICI aussi, pas seulement dans l'implémentation.
 */
public interface IFacadeMetier {

    /** Tous les clubs, en LECTURE SEULE (vue non modifiable) : pour
     *  modifier, on repasse par la façade - jamais par la liste rendue. */
    List<Club> recupererClubs();

    // Le catalogue COMPLET (pour une action d'affichage si le sujet en a
    // une) ; l'affectation, elle, passe par les DISPONIBLES ci-dessous.
    List<Personne> recupererMembresCatalogue();

    // Les membres du catalogue qui ne sont dans AUCUN club : c'est cette
    // liste que l'action d'affectation propose — un membre placé disparaît,
    // un membre retiré d'un club y réapparaît.
    List<Personne> recupererMembresDisponibles();

    Resultat creerMembre(Personne membre);

    // Exposé au Presenter si le sujet a une action "afficher les
    // titulaires" ; sinon, laisser, mais ne pas l'appeler. La façade se
    // contente de RELAYER la sélection du DAO (aucune décision ici).
    List<Joueur> recupererTitulaires();

    // Le club d'appartenance d'un membre, null s'il n'en a pas. Sert à
    // l'affichage des titulaires ET à la règle "un joueur ne peut pas être
    // dans deux clubs" (qui garantit du même coup qu'il n'est titulaire
    // que dans un seul club).
    Club rechercherClubDeMembre(Personne membre);

    /** Les seuls Joueurs d'un club (sous-ensemble typé des membres) -
     *  servent au tri paramétrable, aux statistiques et à la promotion. */
    List<Joueur> recupererJoueurs(Club club);

    // INVARIANT : un joueur ne peut être titulaire QUE dans un club avec lequel il
    // est. La promotion se fait donc APRES l'affectation, jamais à la
    // création (le catalogue crée des joueurs non titulaires), et les
    // règles titulaires (max 11, gardien unique) se vérifient ICI.
    Resultat promouvoirTitulaire(Club club, Joueur joueur);

    // Le "throws" d'une checked se déclare sur l'INTERFACE aussi, pas
    // seulement dans l'implémentation : la signature est le contrat.
    Resultat creerClub(String nom, LocalDate dateDeCreation, Niveau niveau) throws MetierException;

    /** Suppression sans condition : void, car aucun échec métier n'est
     *  prévu par le sujet (comparer avec supprimerMembre -> Resultat). */
    void supprimerClub(Club club);

    /** Les membres d'UN club (sa liste interne, non modifiable) - à ne
     *  pas confondre avec le catalogue global ci-dessus. */
    List<Personne> recupererMembres(Club club);

    /** Affectation d'un membre du catalogue à un club. Chaque règle a son
     *  Resultat : EFFECTIF_COMPLET, DEJA_Existant, DEJA_DANS_UN_CLUB,
     *  TROP_D_ENTRAINEURS - le Presenter affiche un message par cause. */
    Resultat ajouterMembre(Club club, Personne membre);

    // FACADE DEFENSIVE : ne fait pas confiance à l'appelant — si le membre
    // n'est pas dans le club, INTROUVABLE plutôt qu'un remove silencieux.
    Resultat supprimerMembre(Club club, Personne membre);
}
