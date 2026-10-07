package views.commons.utils;

import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * TEMPLATE - Utilitaire de SAISIE VALIDEE : factorise la boucle de
 * re-saisie que demanderTexteNonVide / demanderDatePassee /
 * demanderDateFuture dupliquaient. Nommé d'après son contenu, comme
 * AffichageConsole / LectureConsole / ChoixConsole.
 *
 * Les CINQ interfaces fonctionnelles du template :
 *   Function<T,String>  = T -> String   (extraire un libellé)
 *   Predicate<T>        = T -> boolean  (tester un critère)
 *   Supplier<T>         = () -> T       (produire une valeur : ici, LIRE)
 *   Comparator<T>       = (T, T) -> int (ordonner - voir ComparateurUtils)
 *   Consumer<T>         = T -> rien     (consommer : le forEach du cours Flux)
 */
public final class SaisieConsole {

    private SaisieConsole() {
    }

    /**
     * Relit en boucle via `lecture` tant que `estValide` refuse la valeur,
     * en affichant `messageErreur` à chaque refus. Générique : marche pour
     * un texte, une date, un entier borné... n'importe quel type.
     */
    public static <T> T lireValide(Supplier<T> lecture, Predicate<T> estValide, String messageErreur) {
        while (true) {
            T valeur = lecture.get();
            if (estValide.test(valeur)) {
                return valeur;
            }
            AffichageConsole.afficherErreur(messageErreur);
        }
    }
}
