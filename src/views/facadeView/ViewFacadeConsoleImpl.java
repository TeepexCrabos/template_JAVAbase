package views.facadeView;

import views.commons.constantes.ConstantesView;
import views.commons.utils.AffichageConsole;
import views.commons.utils.ChoixConsole;
import views.commons.utils.SaisieConsole;
import views.commons.utils.LectureConsole;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * TEMPLATE - Implémentation CONSOLE de la façade vue : la seule classe qui
 * sait que l'IHM est un terminal. Elle ne fait qu'ASSEMBLER les quatre
 * utilitaires fournis/maison (AffichageConsole affiche, LectureConsole lit,
 * ChoixConsole fait choisir, SaisieConsole fait saisir valide) - aucune
 * logique métier, aucun System.out direct.
 *
 * Une future vue graphique implémenterait IFacadeView autrement sans
 * toucher au Presenter : c'est tout l'intérêt du contrat d'interface.
 *
 * PIEGE connu : liste vide -> boucle infinie dans lectureChoixInt(1, 0).
 * Cette classe NE vérifie PAS isEmpty() (elle ne saurait pas quoi afficher
 * à la place) - c'est la responsabilité du Presenter, AVANT chaque
 * choisirElement. Voir les gardes systématiques dans Presenter.java.
 */
public class ViewFacadeConsoleImpl implements IFacadeView {

    // Codes ANSI (constantes de la VUE console uniquement)
    private static final String VERT = "\u001B[32m";
    private static final String ROUGE = "\u001B[31m";
    private static final String RESET = "\u001B[0m";

    @Override
    public void afficherMenu() {
        AffichageConsole.afficherMessageAvecSautLigne(ConstantesView.MENU_TITRE);
        AffichageConsole.afficherMenuSimpleAvecOptionSortie(ConstantesView.OPTIONS_MENU, ConstantesView.LIBELLE_SORTIE);
        // Génère automatiquement : "1 - option1", "2 - option2"..., "0 - sortir", puis "CHOIX : "
    }

    @Override
    public int demanderChoixMenu() {
        // Borne haute déduite de la taille de la liste : plus de risque d'oubli
        // de mise à jour si on ajoute/retire une option (piège déjà rencontré).
        return LectureConsole.lectureChoixInt(0, ConstantesView.OPTIONS_MENU.size());
    }

    @Override
    public void afficherMessage(String message) {
        AffichageConsole.afficherMessageAvecSautLigne(message);
    }

    @Override
    public void afficherSucces(String message) {
        AffichageConsole.afficherMessageAvecSautLigne(VERT + message + RESET);
    }

    @Override
    public void afficherEchec(String message) {
        AffichageConsole.afficherMessageAvecSautLigne(ROUGE + message + RESET);
    }

    @Override
    public String demanderTexte(String message) {
        return LectureConsole.lectureChaineCaracteres(message);
    }

    // Les trois "demander... valide" partagent la même boucle de re-saisie,
    // factorisée dans SaisieConsole.lireValide : on ne fournit plus que la
    // LECTURE (Supplier) et le CRITERE (Predicate).
    @Override
    public String demanderTexteNonVide(String message) {
        return SaisieConsole.lireValide(
                () -> LectureConsole.lectureChaineCaracteres(message),
                texte -> texte != null && !texte.isBlank(),
                "La saisie ne peut pas être vide.").trim();
    }

    @Override
    public LocalDate demanderDate(String message) {
        return LectureConsole.lectureLocalDate(message, "dd/MM/yyyy");
    }

    @Override
    public LocalDate demanderDatePassee(String message) {
        return SaisieConsole.lireValide(
                () -> demanderDate(message),
                date -> !date.isAfter(LocalDate.now()),
                "La date doit être passée ou aujourd'hui.");
    }

    @Override
    public LocalDate demanderDateFuture(String message) {
        return SaisieConsole.lireValide(
                () -> demanderDate(message),
                date -> date.isAfter(LocalDate.now()),
                "La date doit être dans le futur.");
    }

    @Override
    public int demanderEntier(String message) {
        return LectureConsole.lectureEntier(message);
    }

    @Override
    public int demanderEntierPositif(String message) {return LectureConsole.lectureEntierPositif(message);}

    @Override
    public double demanderDouble(String message) {
        return LectureConsole.lectureDouble(message);
    }

    @Override
    public boolean demanderBoolean(String message) {
        return LectureConsole.lectureBoolean(message);
    }

    @Override
    public int choixUtilisateurMenu(int min, int max) {
        return LectureConsole.lectureChoixInt(min, max);
    }

    // Une seule méthode générique remplace choisirEntiteConteneur/choisirElement
    // dupliqués : le "T" s'adapte au type réel passé par le Presenter.
    // Le bloc libellés -> menu -> lecture est factorisé dans ChoixConsole
    // (détail d'implémentation console, invisible du Presenter).
    @Override
    public <T> T choisirElement(List<T> elements, Function<T, String> libelleExtracteur, String message) {
        AffichageConsole.afficherMessageAvecSautLigne(message);
        return elements.get(ChoixConsole.choisirIndex(elements, libelleExtracteur));
    }

    // Une seule méthode générique remplace choisirEnum dupliqué par enum.
    @Override
    public <T extends Enum<T>> T choisirDansEnum(String libelle, T[] valeurs, Function<T, String> libelleExtracteur) {
        AffichageConsole.afficherMessageAvecSautLigne(libelle + " :");
        List<String> libelles = new ArrayList<>();
        for (T valeur : valeurs) {
            libelles.add(libelleExtracteur.apply(valeur));
        }
        AffichageConsole.afficherMenuSimple(libelles);
        int choix = LectureConsole.lectureChoixInt(1, valeurs.length);
        return valeurs[choix - 1];
        // ATTENTION : .length (pas .size()) et [choix-1] (pas .get()) car "valeurs" est un tableau
    }
}
