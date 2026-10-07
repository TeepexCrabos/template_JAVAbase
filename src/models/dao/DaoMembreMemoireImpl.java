package models.dao;

import models.utils.CollectionUtils;
import models.entities.Joueur;
import models.entities.Personne;

import java.util.List;

/**
 * TEMPLATE - Implémentation mémoire du DAO spécifique : hérite de tout le
 * CRUD générique (la liste interne reste privée dans le parent, on passe
 * par son contrat public lireTous()) et n'implémente que le finder.
 * En JDBC : lireTitulaires() deviendrait
 *     SELECT * FROM membre WHERE titulaire = true
 */
public class DaoMembreMemoireImpl extends DaoMemoireImpl<Personne> implements IDaoMembre {

    @Override
    public List<Joueur> lireTitulaires() {
        // Composition d'outils : d'abord le TYPE, puis l'ATTRIBUT.
        return CollectionUtils.filtrer(
                CollectionUtils.filtrerParType(lireTous(), Joueur.class),
                Joueur::isTitulaire);
    }
}
