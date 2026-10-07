package models.dao;

import models.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

/**
 * TEMPLATE - Implémentation mémoire GENERIQUE du DAO CRUD : une seule
 * classe stocke n'importe quelle entité. Le jour J, aucun DAO à écrire :
 * FactoryDao instancie new DaoMemoireImpl<>() par entité racine.

 * On n'écrit une implémentation SPECIFIQUE (DaoXxxJdbcImpl) que lorsque le
 * stockage diffère vraiment par entité — ex : une table SQL par entité.

 * Rechercher/mettreAJour s'appuient sur equals() : l'identité
 * (equals/hashCode) doit donc porter sur les champs de la phrase
 * d'unicité du sujet, et rien d'autre.
 */
public class DaoMemoireImpl<T> implements IDao<T> {

    private final List<T> elements = new ArrayList<>();

    @Override
    public void creer(T element) {
        elements.add(element);
    }

    @Override
    public List<T> lireTous() {
        return Collections.unmodifiableList(elements);
    }

    @Override
    public T rechercher(T element) {
        int index = elements.indexOf(element);
        if (index < 0) {
            return null;
        }
        return elements.get(index);
    }

    @Override
    public List<T> rechercherTous(Predicate<T> critere) {
        return CollectionUtils.filtrer(elements, critere);
    }

    @Override
    public void mettreAJour(T element) {
        int index = elements.indexOf(element);
        if (index >= 0) {
            elements.set(index, element);
        }
    }

    @Override
    public void supprimer(T element) {
        elements.remove(element);
    }
}
