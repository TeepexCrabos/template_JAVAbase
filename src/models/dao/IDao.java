package models.dao;

import java.util.List;
import java.util.function.Predicate;

/**
 * TEMPLATE - Interface DAO générique, calquée sur le CRUD :

 *   C - Create → créer(T)
 *   R - Read → lireTous() et rechercher(T)
 *   U - Update → mettreAJour(T)
 *   D - Delete → supprimer(T)

 * Générique <T> : une seule interface pour tous les DAO du projet, comme
 * choisirElement (List<T>, ...) côté vue. Chaque entité racine a son
 * implémentation : DaoXxxMemoireImpl implements IDao<Xxx>.

 * Aucune règle métier ici : quotas, doublons et Résultat restent dans la
 * Façade métier ; le DAO ne sait QUE stocker.
 */
public interface IDao<T> {

    void creer(T element);

    List<T> lireTous();

    T rechercher(T element);

    /**
     * Sélection GÉNÉRIQUE par critère : couvre n'importe quel filtre de
     * n'importe quel sujet sans créer d'interface spécifique.
     * Predicate<T> = cousin de Function<T,String> : une fonction T → boolean,
     * évaluée par Test (element). Ex :
     *     daoClub.rechercherTous(c → c.getNiveau() == Niveau.NATIONAL)

     * LIMITE à connaître : un Predicate est du code Java, il ne deviendra
     * JAMAIS un WHERE SQL – en JDBC cette méthode chargerait tout puis
     * filtrerait en mémoire. Pour une requête pérenne/nommée, préférer un
     * finder dédié dans une interface spécifique (voir IDaoMembre).
     */
    List<T> rechercherTous(Predicate<T> critere);

    void mettreAJour(T element);

    void supprimer(T element);
}
