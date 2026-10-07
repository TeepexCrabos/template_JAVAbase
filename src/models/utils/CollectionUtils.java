package models.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Comparator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

/**
 * TEMPLATE - Utilitaire technique de collections (miroir côté modèle de
 * views.commons.utils). Implémentée en STREAMS depuis le cours dédié -
 * l'API n'a pas changé d'une virgule : aucun appelant (façade, DAO,
 * Presenter) n'a été modifié par la migration boucles -> streams. Nommé d'après son CONTENU : seules des méthodes
 * de collections peuvent entrer ici - jamais de logique métier, jamais de
 * fourre-tout "ModelUtils".
 */
public final class CollectionUtils {

    private CollectionUtils() {
    }

    /**
     * Copie n'importe quelle Collection (List, Set...) dans une nouvelle
     * List. Deux usages : la COPIE DEFENSIVE des setters, et l'acceptation
     * d'un Set venant d'un sujet sans changer le reste du code.
     * Restes-en ArrayList : c'est la copie la plus directe, et le Conteneur
     * a besoin d'une liste interne MODIFIABLE (stream().collect(* Collectors.toList()) serait équivalent, en plus verbeux).
     */
    public static <T> List<T> collectionToList(Collection<T> collection) {
        return new ArrayList<>(collection);
    }

    /** Les éléments qui vérifient le critère. La BOUCLE vit ici ; la
     *  DECISION (quel critère, quelle conséquence) reste chez l'appelant. */
    public static <T> List<T> filtrer(Collection<T> collection, Predicate<T> critere) {
        return collection.stream().filter(critere).collect(Collectors.toList());
    }

    /** Nombre d'éléments qui vérifient le critère.
     *  Count() rend un long : cast en int, suffisant pour nos volumes. */
    public static <T> int compter(Collection<T> collection, Predicate<T> critere) {
        return (int) collection.stream().filter(critere).count();
    }

    /** Vrai dès qu'un élément vérifie le critère. anyMatch (s'arrête au
     *  premier trouvé) n'est pas dans le cours Flux : l'équivalent 100 %
     *  cours serait filter (critere).count() > 0, qui parcourt tout. */
    public static <T> boolean existe(Collection<T> collection, Predicate<T> critere) {
        return collection.stream().anyMatch(critere);
    }

    /**
     * COPIE TRIEE de la collection (l'originale n'est pas modifiée -
     * indispensable : nos getters renvoient des vues NON Modifiables, un
     * sort() dessus lèverait UnsupportedOperationException).
     * Le Comparator se construit avec la même logique que nos Function :
     *   trier (clubs, Comparator.comparing (Club::getNom,
     *                    String.CASE_INSENSITIVE_ORDER)) alphabétique
     *     (jamais comparing (Club::getNom) seul : compareTo met Toutes
     *      les majuscules avant les minuscules - 'Z' < 'd')
     *   trier (joueurs, Comparator.comparing (Joueur::getTaille)) numérique
     *   trier (membres, Comparator.comparing (Personne::getNom)
     *                            .thenComparing (Personne::getPrenom)) critère secondaire
     *   ... .reversed() ordre inverse.
     */
    public static <T> List<T> trier(Collection<T> collection, Comparator<T> comparateur) {
        return collection.stream().sorted(comparateur).collect(Collectors.toList());
    }

    /** TRANSFORMER chaque élément en autre chose (le map du cours) :
     *  la liste des noms, des titres, des libellés...
     *  Ex : transformer (joueurs, Joueur::getNom) -> List<String> des noms. */
    public static <T, R> List<R> transformer(Collection<T> collection, Function<T, R> extracteur) {
        return collection.stream().map(extracteur).collect(Collectors.toList());
    }


    /** Le PREMIER élément qui vérifie le critère, null si aucun - le
     *  "chercher par..." universel.
     *  Ex : premier (clubs, c -> c.getNom().equalsIgnoreCase (saisie)) */
    public static <T> T premier(Collection<T> collection, Predicate<T> critere) {
        return collection.stream().filter(critere).findFirst().orElse(null);
    }

    /** Vrai si TOUS les éléments vérifient le critère (vrai aussi sur une
     *  collection vide - convention allMatch). La forme des règles "Y ne
     *  contient QUE des X" (rayon INFORMATIQUE -> que des ESSAI/ROMAN).
     *  Ex : tousVerifient (rayon.getElements(), l -> l.getType() == ESSAI) */
    public static <T> boolean tousVerifient(Collection<T> collection, Predicate<T> critere) {
        return collection.stream().allMatch(critere);
    }

    /** SOMME d'une valeur numérique extraite de chaque élément, dans la
     *  forme exacte du cours Flux : map puis reduce(0, somme).
     *  Ex : somme (joueurs, Joueur::getAge) -> total des âges. */
    public static <T> double somme(Collection<T> collection, ToDoubleFunction<T> extracteur) {
        return collection.stream()
                .map(extracteur::applyAsDouble)
                .reduce(0.0, Double::sum);
    }

    /** GROUPER les éléments par une clé extraite (le groupingBy du TP
     *  Flux) : Map dont chaque clé pointe vers la liste de ses éléments.
     *  Ex : grouperPar (joueurs, Joueur:: getPoste) -> les joueurs par poste ;
     *       grouperPar (personnes, Person : : getCity) -> par ville. */
    public static <T, K> Map<K, List<T>> grouperPar(Collection<T> collection, Function<T, K> extracteur) {
        return collection.stream().collect(Collectors.groupingBy(extracteur));
    }

    /** MOYENNE de la valeur extraite, ZÉRO si la collection est vide (le
     *  garde-fou de la division par zéro est ICI, pas chez l'appelant).
     *  Ex : moyenne (joueurs, Joueur::getAge) */
    public static <T> double moyenne(Collection<T> collection, ToDoubleFunction<T> extracteur) {
        if (collection.isEmpty()) {
            return 0;
        }
        return somme(collection, extracteur) / collection.size();
    }

    /** Le plus PETIT élément selon le comparateur, null si la collection
     *  est vide. Ex : plusPetit (joueurs, ComparateurUtils.parNombre(Joueur::getAge)) */
    public static <T> T plusPetit(Collection<T> collection, Comparator<T> comparateur) {
        // min() rend un Optional<T> (le "peut-être vide" du cours Streams) :
        // orElse(null) conserve le contrat historique "null si vide".
        return collection.stream().min(comparateur).orElse(null);
    }

    /** Le plus GRAND élément selon le comparateur, null si vide.
     *  Ex : plusGrand (joueurs, ComparateurUtils.parNombre(Joueur::getTaille)) */
    public static <T> T plusGrand(Collection<T> collection, Comparator<T> comparateur) {
        return collection.stream().max(comparateur).orElse(null);
    }

    /**
     * Les éléments d'un TYPE donné, typés en retour : remplace la boucle
     * "instanceof + cast" (le pattern récupérerJoueurs / produits bruts).
     * Type.isInstance(e) = la version méthode de "e instanceof T' ;
     * type.cast(e) = le transtypage vérifié.
     */
    public static <T> List<T> filtrerParType(Collection<?> collection, Class<T> type) {
        // filter garde les bons types, map Transforme chaque élément
        // (le fameux map qui manquait à la boîte) — en références de méthode.
        return collection.stream().filter(type::isInstance).map(type::cast).collect(Collectors.toList());
    }
}
