package views.facadeView;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;

/**
 * TEMPLATE - Interface de la Façade vue.
 *
 * Les méthodes générT-driven (choisirElement, choisirDansEnum) remplacent
 * ce qui aurait sinon été une méthode "choisirXxx" dupliquée par entité/enum
 * (choisirClub, choisirMembre, choisirEnum...). Voir l'explication
 * détaillée fournie séparément pour bien comprendre <T> et Function<T,String>
 * avant de t'en servir en contrôle.
 */
public interface IFacadeView {

    void afficherMenu();
    int demanderChoixMenu();
    void afficherMessage(String message);

    // Messages de RESULTAT colorés (vert/rouge). L'implémentation console
    // utilise les codes ANSI ; une autre implémentation ferait autrement -
    // c'est pour ça que la couleur est un service de la VUE, pas du
    // Presenter.
    void afficherSucces(String message);

    void afficherEchec(String message);
    String demanderTexte(String message);

    // Re-saisie en boucle tant que le texte est vide/blanc (même principe
    // que demanderDatePassee : le SENS du contrôle est câblé une fois ici).
    String demanderTexteNonVide(String message);
    LocalDate demanderDate(String message);

    // Variantes anti-piège : la boucle de re-saisie et le SENS de la
    // comparaison sont câblés une fois pour toutes ici.
    LocalDate demanderDatePassee(String message);
    LocalDate demanderDateFuture(String message);

    int demanderEntier(String message);

    int demanderEntierPositif(String message);

    double demanderDouble(String message);
    boolean demanderBoolean(String message);
    int choixUtilisateurMenu(int min, int max);

    /**
     * Fait choisir UN élément dans une liste, quel que soit son type T.
     *
     * @param elements          la liste dans laquelle choisir
     * @param libelleExtracteur comment extraire le texte à afficher pour
     *                          chaque élément (ex : Club::getNom,
     *                          ou une lambda e -> e.getNom() + " " + e.getPrenom())
     * @param message           message affiché avant le menu
     */
    <T> T choisirElement(List<T> elements, Function<T, String> libelleExtracteur, String message);

    /**
     * Fait choisir une valeur d'enum, avec un libellé personnalisé par valeur.
     * Utiliser T::getLibelle si l'enum a un attribut libelle, ou la version
     * sans extracteur ci-dessous si le nom brut de la constante suffit.
     */
    <T extends Enum<T>> T choisirDansEnum(String libelle, T[] valeurs, Function<T, String> libelleExtracteur);

    /** Variante pratique : affiche le nom brut de chaque constante (Enum::name). */
    default <T extends Enum<T>> T choisirDansEnum(String libelle, T[] valeurs) {
        return choisirDansEnum(libelle, valeurs, Enum::name);
    }
}
