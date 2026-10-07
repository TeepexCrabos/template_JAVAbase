package models.facadeModel;

import models.utils.CollectionUtils;
import models.dao.FactoryDao;
import models.dao.IDao;
import models.dao.IDaoMembre;
import models.entities.Club;
import models.entities.Entraineur;
import models.entities.Joueur;
import models.entities.Personne;
import models.references.ConstanteMetier;
import models.exception.MetierException;
import models.references.Niveau;
import models.references.Poste;
import models.references.Resultat;

import java.time.LocalDate;
import java.util.List;

/**
 * TEMPLATE - Façade métier : porte les REGALES (Resultat, quotas, doublons)
 * et délègue tout le STOCKAGE au DAO. Le choix du support (mémoire, BDD)
 * appartient au DAO, pas à elle.
 */
public class FacadeMetierImpl implements IFacadeMetier {

    // SINGLETON canonique — les TROIS ingrédients :
    // 1. l'unique instance, détenue par la classe (static)
    // 2. le constructeur PRIVE : plus aucun "new" possible dehors
    // 3. getInstance() : crée à la première demande, ressert ensuite
    // Justification : la façade porte l'état de l'application (ses DAO) ;
    // deux instances = deux jeux de données incohérents.
    private static FacadeMetierImpl instance;

    // Un DAO par entité à cycle de vie propre : les clubs, et le CATALOGUE
    // des membres (créés indépendamment, puis affectés à un club).
    private final IDao<Club> daoClub;
    private final IDaoMembre daoMembre;

    private FacadeMetierImpl() {
        this.daoClub = FactoryDao.creerDaoClub();
        this.daoMembre = FactoryDao.creerDaoMembre();
    }

    public static FacadeMetierImpl getInstance() {
        if (instance == null) {
            instance = new FacadeMetierImpl();
        }
        return instance;
    }

    @Override
    public List<Club> recupererClubs() {
        return daoClub.lireTous();
    }

    @Override
    public Resultat creerClub(String nom, LocalDate dateDeCreation, Niveau niveau) throws MetierException {
        // la façade ne la traite pas (elle ne peut rien en faire), elle la
        // laisse remonter au Presenter — règle du cours : la couche qui
        // attrape est celle qui peut REAGIR.
        Club club = new Club(nom, dateDeCreation, niveau);
        if (daoClub.rechercher(club) != null) {
            return Resultat.DEJA_EXISTANT;
        }
        // (pattern "4 comptoirs max par catégorie") : boucler sur
        // daoClub.lireTous() en comptant par attribut, retourner un
        // Résultat dédié si le maximum est atteint.
        daoClub.creer(club);
        return Resultat.OK;
    }

    @Override
    public void supprimerClub(Club club) {
        daoClub.supprimer(club);
    }

    @Override
    public List<Personne> recupererMembresCatalogue() {
        return daoMembre.lireTous();
    }

    @Override
    public List<Personne> recupererMembresDisponibles() {
        // Un membre est disponible s'il n'appartient à aucun club.
        return CollectionUtils.filtrer(daoMembre.lireTous(),
                membre -> rechercherClubDeMembre(membre) == null);
    }

    @Override
    public List<Joueur> recupererTitulaires() {
        return daoMembre.lireTitulaires();
    }

    @Override
    public Club rechercherClubDeMembre(Personne membre) {
        // Le "chercher par critère" universel : premier() rend null si
        // aucun club ne contient ce membre.
        return CollectionUtils.premier(daoClub.lireTous(), c -> c.contientElement(membre));
    }

    @Override
    public Resultat creerMembre(Personne membre) {
        if (daoMembre.rechercher(membre) != null) {
            return Resultat.DEJA_EXISTANT;
        }
        daoMembre.creer(membre);
        return Resultat.OK;
    }

    @Override
    public List<Personne> recupererMembres(Club club) {
        // getElements() renvoie déjà une vue non modifiable (voir Conteneur).
        return club.getElements();
    }

    @Override
    public Resultat ajouterMembre(Club club, Personne membre) {
        // Une vérification par règle de collection, chacune son Resultat :
        if (club.nbElements() >= ConstanteMetier.NB_MAX_MEMBRES) {
            return Resultat.EFFECTIF_COMPLET;
        }
        if (club.contientElement(membre)) {
            return Resultat.DEJA_EXISTANT;
        }
        // REGLE inter-conteneurs : un MEMBRE (joueur comme entraîneur) ne
        // peut être que dans un seul club — filet de sécurité derrière la
        // liste des disponibles, qui empêche déjà de le sélectionner.
        if (rechercherClubDeMembre(membre) != null) {
            return Resultat.DEJA_DANS_UN_CLUB;
        }
        // Quota sur un SOUS-ENSEMBLE filtré par type : max CINQ entraîneurs.
        if (membre instanceof Entraineur
                && compterEntraineurs(club) >= ConstanteMetier.NB_MAX_ENTRAINEURS) {
            return Resultat.TROP_D_ENTRAINEURS;
        }


        club.ajouterElement(membre);
        // U du CRUD : transparent en mémoire, mais c'est cet appel qui
        // porterait l'UPDATE avec un DAO JDBC.
        daoClub.mettreAJour(club);
        return Resultat.OK;
    }

    // La FACADE garde les CRITERIA métier (c'est la règle) ; la boîte à
    // outils porte les boucles (c'est la mécanique). Une nouvelle règle
    // chiffrée = une méthode d'une ligne ici.
    private int compterTitulaires(Club club) {
        return CollectionUtils.compter(club.getElements(),
                m -> m instanceof Joueur joueur && joueur.isTitulaire());
    }

    private int compterEntraineurs(Club club) {
        return CollectionUtils.compter(club.getElements(),
                Entraineur.class::isInstance);
    }

    private boolean possedeGardienTitulaire(Club club) {
        return CollectionUtils.existe(club.getElements(),
                m -> m instanceof Joueur joueur && joueur.isTitulaire()
                        && joueur.getPoste() == Poste.GARDIEN);
    }

    @Override
    public Resultat supprimerMembre(Club club, Personne membre) {
        if (!club.contientElement(membre)) {
            return Resultat.INTROUVABLE;
        }
        // INVARIANT "titulaire seulement dans un club" : en quittant le
        // club, un joueur perd son statut de titulaire.
        if (membre instanceof Joueur joueur) {
            joueur.setTitulaire(false);
        }
        club.supprimerElement(membre);
        daoClub.mettreAJour(club);
        return Resultat.OK;
    }

    @Override
    public List<Joueur> recupererJoueurs(Club club) {
        return CollectionUtils.filtrerParType(club.getElements(), Joueur.class);
    }

    @Override
    public Resultat promouvoirTitulaire(Club club, Joueur joueur) {
        // LA règle : pas titulaire hors d'un club — le joueur doit être
        // dans CE club pour y être promu.
        if (!club.contientElement(joueur)) {
            return Resultat.INTROUVABLE;
        }
        if (joueur.isTitulaire()) {
            return Resultat.DEJA_TITULAIRE;
        }
        // REGLE avec FILTRE type + attribut (comptage conditionnel) :
        if (compterTitulaires(club) >= ConstanteMetier.NB_MAX_TITULAIRES) {
            return Resultat.TROP_DE_TITULAIRES;
        }
        // REGLE d'UNICITÉ dans un sous-ensemble : un seul gardien titulaire.
        if (joueur.getPoste() == Poste.GARDIEN && possedeGardienTitulaire(club)) {
            return Resultat.DEJA_UN_GARDIEN_TITULAIRE;
        }
        joueur.setTitulaire(true);
        daoClub.mettreAJour(club);
        return Resultat.OK;
    }
}
