package presenter;

import models.utils.CollectionUtils;
import models.utils.ComparateurUtils;
import models.entities.Club;
import models.entities.Joueur;
import models.exception.MetierException;
import models.entities.FactoryEntite;
import models.entities.Personne;
import models.facadeModel.FactoryFacadeMetier;
import models.facadeModel.IFacadeMetier;
import models.references.CritereTriJoueur;
import models.references.Niveau;
import models.references.Poste;
import models.references.Resultat;
import models.references.TypeMembre;
import views.facadeView.FactoryFacadeView;
import views.facadeView.IFacadeView;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

/**
 * TEMPLATE - Presenter (MVP). Chaque action est une DEMONSTRATION d'un
 * mécanisme du template ; le jour J, garder les actions utiles au sujet,
 * renommer, supprimer le reste.

 * PIEGE IMPORTANT : facadeView.choisirElement(liste, ...) bloque en boucle
 * infinie si liste.size() == 0. TOUJOURS vérifier isEmpty() AVANT d'appeler
 * choisirElement(...) - voir choisirClub() et les gardes ci-dessous.

 * VALIDATION : DEUX NIVEAUX, parfois COMBINES sur une même action :
 * - Validation de CHAMP -> IllegalArgumentException -> try/catch ici.
 * - Règle métier sur une COLLECTION -> Resultat -> switch ici.
 * Action 1 = le combo des deux, comme dans les sujets réels.

 * VARIANTE exception personnalisée (si le sujet la demande - voir
 * models.exceptions.MetierException) : remplacer IllegalArgumentException
 * par MetierException dans les catch. Si deux niveaux d'exception peuvent
 * remonter, MULTI-CATCH : catch (MetierException | FactoryException e).
 * Toujours afficher e.getMessage() via la vue, jamais e.printStackTrace().
 */
public class Presenter {

    // Messages REPETES uniquement (DRY : une correction, un seul endroit).
    // Constantes privées ICI et pas dans ConstantesView : le Presenter ne
    // doit pas dépendre de views.commons hors façade. Les messages uniques
    // (prompts, échecs spécifiques) restent inline pour la lisibilité.
    private static final String MSG_AUCUN_CLUB = "Aucun club créé pour l'instant.";
    private static final String MSG_SUCCES_SUPPRESSION = "Supprimé avec succès.";

    // final : ces références ne changent jamais après le constructeur
    // (minimiser la mutabilité, Effective Java).
    private final IFacadeMetier facadeMetier;
    private final IFacadeView facadeView;

    public Presenter() {
        this.facadeMetier = FactoryFacadeMetier.creerFacadeMetier();
        this.facadeView = FactoryFacadeView.creerFacadeView();
    }

    public void start() {
        boolean continuer = true;
        while (continuer) {
            facadeView.afficherMenu();
            int choix = facadeView.demanderChoixMenu();
            switch (choix) {
                case 1 -> action1CreerClub();
                case 2 -> action2CreerMembre();
                case 3 -> action3AjouterMembreClub();
                case 4 -> action4AfficherClub();
                case 5 -> action5SupprimerMembreClub();
                case 6 -> action6SupprimerClub();
                case 7 -> action7AfficherTitulaires();
                case 8 -> action8PasserTitulaire();
                case 9 -> action9AfficherJoueursTries();
                case 10 -> action10AfficherStatistiques();
                case 0 -> continuer = false;
                default -> continuer = applicationQuitte();

            }
        }
    }
    private boolean applicationQuitte() {
        facadeView.afficherMessage("Choix inconnu - fermeture de l'application.");
        return false;
    }
    // COMBO validation de champ (try/catch) + règle de collection (switch
    // Resultat) sur la même action : le constructeur peut refuser la date,
    // la façade peut refuser le doublon.
    private void action1CreerClub() {
        String nom = facadeView.demanderTexteNonVide("Nom : ");
        LocalDate date = facadeView.demanderDatePassee("Date (jj/MM/aaaa) : ");
        // Enum SIMPLE : variante par défaut de choisirDansEnum (Enum ::name,
        // pas de libellé) - à comparer avec Poste/TypeMembre qui passent
        // ::getLibelle.
        Niveau niveau = facadeView.choisirDansEnum("Niveau", Niveau.values());
        try {
            Resultat resultat = facadeMetier.creerClub(nom, date, niveau);
            switch (resultat) {
                case OK -> facadeView.afficherSucces("Créé avec succès.");
                case DEJA_EXISTANT -> facadeView.afficherEchec("Échec : existe déjà.");
                default -> facadeView.afficherEchec("Échec : règle métier non respectée.");
            }
        } catch (MetierException e) {
            facadeView.afficherEchec("Échec : " + e.getMessage());
        }
    }

    // Choix d'un TYPE via un enum complexe + switch EXPRESSION qui oriente
    // la saisie, création via la FACTORY (jamais de "new" ici), puis
    // Resultat - le pattern des sujets à plusieurs sous-classes.
    // "Version école" : le membre est créé dans le CATALOGUE (son DAO),
    // il sera affecté à un club par l'action 3 - pattern épicerie.
    private void action2CreerMembre() {
        Personne membre;
        try {
            membre = saisirMembre();
        } catch (MetierException e) {
            facadeView.afficherEchec("Échec : " + e.getMessage());
            return;
        }
        Resultat resultat = facadeMetier.creerMembre(membre);
        switch (resultat) {
            case OK -> facadeView.afficherSucces("Membre créé avec succès.");
            case DEJA_EXISTANT -> facadeView.afficherEchec("Échec : ce membre existe déjà.");
            default -> facadeView.afficherEchec("Échec : règle métier non respectée.");
        }
    }

    // Affectation : DEUX listes indépendantes à garder (clubs ET catalogue),
    // chacune sa garde isEmpty avant choisirElement.
    private void action3AjouterMembreClub() {
        List<Club> clubs = facadeMetier.recupererClubs();
        if (clubs.isEmpty()) {
            facadeView.afficherMessage(MSG_AUCUN_CLUB);
            return;
        }
        List<Personne> disponibles = facadeMetier.recupererMembresDisponibles();
        if (disponibles.isEmpty()) {
            facadeView.afficherMessage("Aucun membre disponible pour l'instant.");
            return;
        }
        Club club = choisirClub(clubs);
        // fonction() est polymorphe : "(Gardien de but)" pour un joueur,
        // "(Entraîneur)" pour un entraîneur - aucun instanceof ici.
        // Tri d'AFFICHAGE (côté Presenter, sur une copie) : alphabétique
        // humain via la fabrique de comparateurs, critères enchaînés.
        disponibles = CollectionUtils.trier(disponibles, ComparateurUtils.enchaine(
                ComparateurUtils.parTexte(Personne::getNom),
                ComparateurUtils.parTexte(Personne::getPrenom)));
        Personne membre = facadeView.choisirElement(
                disponibles,
                m -> m.getNom() + " " + m.getPrenom() + " (" + m.fonction() + ")",
                "Choisir un membre :");

        Resultat resultat = facadeMetier.ajouterMembre(club, membre);
        switch (resultat) {
            case OK -> facadeView.afficherSucces("Ajouté avec succès.");
            case DEJA_EXISTANT -> facadeView.afficherEchec("Échec : membre déjà présent.");
            case EFFECTIF_COMPLET -> facadeView.afficherEchec("Échec : effectif complet.");
            case DEJA_DANS_UN_CLUB -> facadeView.afficherEchec("Échec : ce membre est déjà dans un club.");
            case TROP_D_ENTRAINEURS -> facadeView.afficherEchec("Échec : nombre maximum d'entraîneurs atteint.");
            default -> facadeView.afficherEchec("Échec : règle métier non respectée.");
        }
    }

    private Personne saisirMembre() throws MetierException {
        TypeMembre type = facadeView.choisirDansEnum(
                "Type de membre", TypeMembre.values(), TypeMembre::getLibelle);
        String nom = facadeView.demanderTexteNonVide("Nom : ");
        String prenom = facadeView.demanderTexteNonVide("Prénom : ");
        return switch (type) {
            case ENTRAINEUR -> FactoryEntite.creerEntraineur(nom, prenom,
                    facadeView.demanderDatePassee("Date de naissance (jj/MM/aaaa) : "));
            case JOUEUR -> FactoryEntite.creerJoueur(
                    nom,
                    prenom,
                    facadeView.demanderEntier("Numéro de maillot : "),
                    facadeView.demanderDouble("Taille (cm) : "),
                    facadeView.demanderDatePassee("Date de naissance (jj/MM/aaaa) : "),
                    // Enum COMPLEXE : le libellé s'affiche dans le menu
                    // grâce à la référence de méthode ::getLibelle.
                    facadeView.choisirDansEnum("Poste",
                            Poste.values(), Poste::getLibelle),
                    // INVARIANT : un joueur est créé NON titulaire - il ne
                    // peut le devenir qu'une fois dans un club (action 8).
                    false);
        };
    }

    // POLYMORPHISME : la liste contient des Personne de types concrets
    // différents ; presentation() exécute la version de chaque sous-classe.
    private void action4AfficherClub() {
        List<Club> clubs = facadeMetier.recupererClubs();
        if (clubs.isEmpty()) {
            facadeView.afficherMessage(MSG_AUCUN_CLUB);
            return;
        }
        Club club = choisirClub(clubs);

        List<Personne> membres = facadeMetier.recupererMembres(club);
        if (membres.isEmpty()) {
            facadeView.afficherMessage("Aucun membre dans ce club pour l'instant.");
            return;
        }
        // forEach du cours Flux : opération TERMINALE, un Consumer par
        // élément - équivalent stream de la boucle for classique.
        membres.stream().forEach(membre -> facadeView.afficherMessage(membre.presentation()));
    }

    // Action à DEUX niveaux (conteneur puis élément) : chaque choisirElement
    // a SA garde isEmpty. Ici une LAMBDA car aucun getter tout fait ne
    // combine nom + prénom ; ailleurs une référence de méthode (::) suffit.
    private void action5SupprimerMembreClub() {
        List<Club> clubs = facadeMetier.recupererClubs();
        if (clubs.isEmpty()) {
            facadeView.afficherMessage(MSG_AUCUN_CLUB);
            return;
        }
        Club club = choisirClub(clubs);

        List<Personne> membres = facadeMetier.recupererMembres(club);
        if (membres.isEmpty()) {
            facadeView.afficherMessage("Aucun membre à supprimer dans ce club.");
            return;
        }
        Personne membre = facadeView.choisirElement(
                membres,
                elem -> elem.getNom() + " " + elem.getPrenom(),
                "Choisir un membre :");

        Resultat resultat = facadeMetier.supprimerMembre(club, membre);
        switch (resultat) {
            case OK -> facadeView.afficherSucces(MSG_SUCCES_SUPPRESSION);
            case INTROUVABLE -> facadeView.afficherEchec("Échec : membre introuvable dans ce club.");
            default -> facadeView.afficherEchec("Échec : règle métier non respectée.");
        }
    }

    private void action6SupprimerClub() {
        List<Club> clubs = facadeMetier.recupererClubs();
        if (clubs.isEmpty()) {
            facadeView.afficherMessage(MSG_AUCUN_CLUB);
            return;
        }
        Club club = choisirClub(clubs);

        facadeMetier.supprimerClub(club);
        facadeView.afficherSucces(MSG_SUCCES_SUPPRESSION);
    }

    // Le FINDER du DAO spécifique (IDaoMembre.lireTitulaires), relayé par
    // la façade : une SELECTION sur le catalogue entier, pas une décision.
    private void action7AfficherTitulaires() {
        List<Joueur> titulaires = facadeMetier.recupererTitulaires();
        if (titulaires.isEmpty()) {
            facadeView.afficherMessage("Aucun joueur titulaire pour l'instant.");
            return;
        }
        for (Joueur titulaire : CollectionUtils.trier(titulaires,
                ComparateurUtils.parTexte(Joueur::getNom))) {
            Club clubDuJoueur = facadeMetier.rechercherClubDeMembre(titulaire);
            String appartenance = clubDuJoueur == null
                    ? "sans club"
                    : "club : " + clubDuJoueur.getNom();
            facadeView.afficherMessage(titulaire.presentation() + " - " + appartenance);
        }
    }

    private void action8PasserTitulaire() {
        List<Club> clubs = facadeMetier.recupererClubs();
        if (clubs.isEmpty()) {
            facadeView.afficherMessage(MSG_AUCUN_CLUB);
            return;
        }
        Club club = choisirClub(clubs);

        List<Joueur> joueurs = facadeMetier.recupererJoueurs(club);
        if (joueurs.isEmpty()) {
            facadeView.afficherMessage("Aucun joueur dans ce club.");
            return;
        }
        Joueur joueur = facadeView.choisirElement(
                joueurs,
                j -> j.getNom() + " " + j.getPrenom(),
                "Choisir un joueur :");

        Resultat resultat = facadeMetier.promouvoirTitulaire(club, joueur);
        switch (resultat) {
            case OK -> facadeView.afficherSucces("Joueur passé titulaire.");
            case DEJA_TITULAIRE -> facadeView.afficherEchec("Échec : ce joueur est déjà titulaire.");
            case TROP_DE_TITULAIRES -> facadeView.afficherEchec("Échec : nombre maximum de titulaires atteint.");
            case DEJA_UN_GARDIEN_TITULAIRE -> facadeView.afficherEchec("Échec : il y a déjà un gardien titulaire.");
            case INTROUVABLE -> facadeView.afficherEchec("Échec : joueur introuvable dans ce club.");
            default -> facadeView.afficherEchec("Échec : règle métier non respectée.");
        }
    }

    // TRI PARAMETRABLE : le critère se choisit dans un enum (nouvelle
    // démo de choisirDansEnum), le switch EXPRESSION le traduit en
    // Comparator via la fabrique - ajouter un critère = 2 lignes.
    private void action9AfficherJoueursTries() {
        List<Club> clubs = facadeMetier.recupererClubs();
        if (clubs.isEmpty()) {
            facadeView.afficherMessage(MSG_AUCUN_CLUB);
            return;
        }
        Club club = choisirClub(clubs);
        List<Joueur> joueurs = facadeMetier.recupererJoueurs(club);
        if (joueurs.isEmpty()) {
            facadeView.afficherMessage("Aucun joueur dans ce club.");
            return;
        }
        CritereTriJoueur critere = facadeView.choisirDansEnum(
                "Trier par", CritereTriJoueur.values(), CritereTriJoueur::getLibelle);
        for (Joueur joueur : CollectionUtils.trier(joueurs, comparateurPour(critere))) {
            facadeView.afficherMessage(String.format("%s - %.0f cm - %d ans - n°%d",
                    joueur.presentation(), joueur.getTaille(), joueur.getAge(), joueur.getNumero()));
        }
    }

    private Comparator<Joueur> comparateurPour(CritereTriJoueur critere) {
        return switch (critere) {
            case NOM -> ComparateurUtils.parTexte(Joueur::getNom);
            case TAILLE_CROISSANTE -> ComparateurUtils.parNombre(Joueur::getTaille);
            case TAILLE_DECROISSANTE -> ComparateurUtils.inverse(ComparateurUtils.parNombre(Joueur::getTaille));
            case AGE -> ComparateurUtils.parNombre(Joueur::getAge);
            case NUMERO -> ComparateurUtils.parNombre(Joueur::getNumero);
        };
    }

    // STATISTIQUES : la vitrine de compter / plusPetit / plusGrand -
    // que de la CONSULTATION, donc tout se calcule ici, la façade ne
    // change pas.
    private void action10AfficherStatistiques() {
        List<Club> clubs = facadeMetier.recupererClubs();
        if (clubs.isEmpty()) {
            facadeView.afficherMessage(MSG_AUCUN_CLUB);
            return;
        }
        Club club = choisirClub(clubs);

        List<Personne> membres = facadeMetier.recupererMembres(club);
        List<Joueur> joueurs = facadeMetier.recupererJoueurs(club);
        int nbTitulaires = CollectionUtils.compter(joueurs, Joueur::isTitulaire);

        facadeView.afficherMessage(String.format("Membres : %d (%d joueur(s) dont %d titulaire(s), %d entraîneur(s))",
                membres.size(), joueurs.size(), nbTitulaires, membres.size() - joueurs.size()));

        if (joueurs.isEmpty()) {
            return;
        }
        Joueur plusGrand = CollectionUtils.plusGrand(joueurs, ComparateurUtils.parNombre(Joueur::getTaille));
        Joueur plusPetit = CollectionUtils.plusPetit(joueurs, ComparateurUtils.parNombre(Joueur::getTaille));
        Joueur doyen = CollectionUtils.plusGrand(joueurs, ComparateurUtils.parNombre(Joueur::getAge));
        Joueur benjamin = CollectionUtils.plusPetit(joueurs, ComparateurUtils.parNombre(Joueur::getAge));

        facadeView.afficherMessage(String.format("Plus grand joueur : %s %s (%.0f cm)",
                plusGrand.getPrenom(), plusGrand.getNom(), plusGrand.getTaille()));
        facadeView.afficherMessage(String.format("Plus petit joueur : %s %s (%.0f cm)",
                plusPetit.getPrenom(), plusPetit.getNom(), plusPetit.getTaille()));
        facadeView.afficherMessage(String.format("Doyen : %s %s (%d ans)",
                doyen.getPrenom(), doyen.getNom(), doyen.getAge()));
        facadeView.afficherMessage(String.format("Benjamin : %s %s (%d ans)",
                benjamin.getPrenom(), benjamin.getNom(), benjamin.getAge()));
        facadeView.afficherMessage(String.format("Âge moyen des joueurs : %.1f ans",
                CollectionUtils.moyenne(joueurs, Joueur::getAge)));
        // grouperPar (groupingBy du TP Flux) : une Map poste -> joueurs ;
        // forEach de Map = un BiConsumer (clé, valeur).
        facadeView.afficherMessage("Répartition par poste :");
        CollectionUtils.grouperPar(joueurs, Joueur::getPoste)
                .forEach((poste, liste) -> facadeView.afficherMessage(
                        String.format("  %s : %d", poste.getLibelle(), liste.size())));
    }

    // Factorisation : le même choix revient dans 4 actions sur 5 - une
    // méthode privée plutôt que 4 copies de la même lambda.
    private Club choisirClub(List<Club> clubs) {
        return facadeView.choisirElement(
                CollectionUtils.trier(clubs, ComparateurUtils.parTexte(Club::getNom)),
                Club::getNom, "Choisir un club :");
    }
}
