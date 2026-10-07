package views.commons.utils;

import java.util.List;
import java.util.function.Function;

/**
 * TEMPLATE - Utilitaire de CHOIX au clavier (équivalent du "ViewUtils" du
 * cours, nommé d'après son contenu comme AffichageConsole/LectureConsole).
 *
 * DETAIL D'IMPLEMENTATION de la vue console : appelé UNIQUEMENT par
 * ViewFacadeConsoleImpl, jamais par le Presenter - qui ne connaît que
 * IFacadeView. Factorise le bloc commun libellés -> menu -> lecture que
 * choisirElement et choisirDansEnum dupliquaient.
 */
public final class ChoixConsole {

    private ChoixConsole() {
    }

    /**
     * Affiche un menu numéroté à partir des libellés extraits, lit un choix
     * borné et retourne l'index choisi (base 0). Fonctionne pour une List
     * comme pour un tableau de valeurs d'enum via le nombre d'éléments.
     */
    public static <T> int choisirIndex(List<T> elements, Function<T, String> libelleExtracteur) {
        // map : transformer chaque élément en son libellé - la Function
        // devient l'argument direct du stream.
        List<String> libelles = elements.stream().map(libelleExtracteur).collect(java.util.stream.Collectors.toList());
        AffichageConsole.afficherMenuSimple(libelles);
        return LectureConsole.lectureChoixInt(1, elements.size()) - 1;
    }
}
