package models.dao;

import models.entities.Club;


/**
 * TEMPLATE - Factory des DAO : centralise le choix de l'implémentation de
 * stockage. Grâce au DAO mémoire générique, une entité racine de plus =
 * une méthode de TROIS lignes ici, zéro nouvelle classe.
 * Passer en base de données = remplacer new DaoMemoireImpl<>() par
 * new DaoXxxJdbcImpl() dans la méthode concernée, rien d'autre.
 */
public final class FactoryDao {

    // Unicité gérée par la FACTORY (variante du Singleton adaptée au
    // générique) : un attribut static appartient à la CLASSE, or à cause de
    // l'effacement des génériques DaoMemoireImpl<Club> et
    // DaoMemoireImpl<Personne> sont la même classe à l'exécution - un
    // getInstance() static y serait partagé entre tous les types. Une
    // constante PAR TYPE ici règle le problème : chaque appel ressert la
    // même instance, la "base mémoire" est unique par entité.
    private static final IDao<Club> DAO_CLUB = new DaoMemoireImpl<>();
    private static final IDaoMembre DAO_MEMBRE = new DaoMembreMemoireImpl();

    private FactoryDao() {
    }

    public static IDao<Club> creerDaoClub() {
        return DAO_CLUB;
    }

    // "Version école" (un DAO par entité) : le CATALOGUE des membres à son
    // propre DAO car les membres ont un cycle de vie indépendant — ils sont
    // créés AVANT d'être affectés à un club (pattern catalogue de produits
    // de l'épicerie). En BDD : la future table MEMBRE.
    // Type de retour SPECIFIC (IDaoMembre) : la façade voit le CRUD
    // hérité + les finders. Une entité sans finder garde le générique
    // (voir créerDaoClub).
    public static IDaoMembre creerDaoMembre() {
        return DAO_MEMBRE;
    }
}
