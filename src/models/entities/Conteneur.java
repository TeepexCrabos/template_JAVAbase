package models.entities;

import models.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * TEMPLATE - Classe de base GÉNÉRIQUE pour toute entité qui contient une
 * liste d'une autre entité. E = Element (convention Java : T pour Type,
 * E pour Element, K/V pour clé/valeur).

 * Chaque conteneur du sujet devient une ligne extends :
 *   - 1 niveau : Club extends Conteneur<Personne> (ce template)
 *   - 2 niveaux : Bibliothèque extends Conteneur<Rayon>
 *                 Rayon extends Conteneur<Livre>
 * La copie défensive, l'encapsulation de la collection et le comptage sont
 * hérités : plus rien à réécrire par sujet.

 * Les REGLES métier (quota, doublons -> Resultat) restent dans la Façade :
 * ce conteneur sait stocker et compter, pas décider.
 */
public abstract class Conteneur<E> {

    private List<E> elements = new ArrayList<>();

    public List<E> getElements() {
        return Collections.unmodifiableList(elements);
    }

    // Collection<E> en paramètre (pas List) : accepte une List OU un Set
    // selon ce que le sujet fournit ; collectionToList fait la copie
    // défensive dans tous les cas.
    public void setElements(Collection<E> elements) {
        this.elements = CollectionUtils.collectionToList(elements);
    }

    public void ajouterElement(E element) {
        elements.add(element);
    }

    public void supprimerElement(E element) {
        elements.remove(element);
    }

    public boolean contientElement(E element) {
        return elements.contains(element);
    }

    public int nbElements() {
        return elements.size();
    }
}
