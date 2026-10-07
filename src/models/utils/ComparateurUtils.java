package models.utils;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;

/**
 * TEMPLATE - Fabrique de COMPARATEURS prêts à l'emploi pour trier(...).
 * Nommé d'après son contenu : uniquement des constructions de Comparator.
 * Chaque méthode couvre un besoin type des sujets ; elles s'ENCHAINENT
 * toutes avec .thenComparing(...) et s'inversent avec inverse(...).
 */
public final class ComparateurUtils {

    private ComparateurUtils() {
    }

    /** Ordre ALPHABETIQUE humain (insensible à la casse) sur un texte.
     *  Ex : parTexte(Personne::getNom) */
    public static <T> Comparator<T> parTexte(Function<T, String> extracteur) {
        return Comparator.comparing(extracteur, String.CASE_INSENSITIVE_ORDER);
    }

    /** Ordre NUMERIQUE croissant (int, double, âge, taille, prix...).
     *  Ex : parNombre(Joueur::getTaille), parNombre(Joueur::getAge) */
    public static <T> Comparator<T> parNombre(ToDoubleFunction<T> extracteur) {
        return Comparator.comparingDouble(extracteur);
    }

    /** Ordre CHRONOLOGIQUE (du plus ancien au plus récent).
     *  Ex : parDate(Club::getDateDeCreation) */
    public static <T> Comparator<T> parDate(Function<T, LocalDate> extracteur) {
        return Comparator.comparing(extracteur);
    }

    /** Ordre par LONGUEUR d'un texte (du plus court au plus long).
     *  Ex : parLongueurTexte(Musique::getTitre) */
    public static <T> Comparator<T> parLongueurTexte(Function<T, String> extracteur) {
        return Comparator.comparingInt(element -> extracteur.apply(element).length());
    }

    /** LA forme GENERALE : tri sur n'importe quelle clé Comparable
     *  (enum, LocalDate, Integer, String sensible à la casse...).
     *  parTexte / parDate n'en sont que des spécialisations lisibles.
     *  Ex : parCle (Joueur:: getPoste) -> ordre de déclaration de l'enum ;
     *       parCle (Club::getNom) -> alphabétique STRICT (majuscules d'abord). */
    public static <T, U extends Comparable<U>> Comparator<T> parCle(Function<T, U> extracteur) {
        return Comparator.comparing(extracteur);
    }

    /** Ordre NATUREL d'un type Comparable (String brut, Integer, LocalDate...). */
    public static <T extends Comparable<T>> Comparator<T> naturel() {
        return Comparator.naturalOrder();
    }

    /** Le même ordre, INVERSE (décroissant, du plus récent, Z->A...).
     *  Ex : inverse(parNombre(Joueur::getTaille)) = du plus grand au plus petit */
    public static <T> Comparator<T> inverse(Comparator<T> comparateur) {
        return comparateur.reversed();
    }

    /** ENCHAINE plusieurs critères : le suivant départage les ex aequo.
     *  Ex : enchaine(parTexte(Personne::getNom), parTexte(Personne::getPrenom)). */
    @SafeVarargs
    public static <T> Comparator<T> enchaine(Comparator<T> premier, Comparator<T>... suivants) {
        Comparator<T> resultat = premier;
        for (Comparator<T> suivant : suivants) {
            resultat = resultat.thenComparing(suivant);
        }
        return resultat;
    }
}
