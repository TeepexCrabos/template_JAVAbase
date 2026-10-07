package views.commons.utils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

/**
 * Cette classe utilitaire propose diverses méthodes d'affichage sur console.
 * FICHIER FOURNI EN COURS - ne pas modifier, juste coller dans le nouveau projet.
 *
 * @author PDI
 */
public final class AffichageConsole {

    public static final char AFFICHAGE_COIN_MENU = '+';
    public static final char AFFICHAGE_LIGNE_HORIZONTALE = '-';
    public static final char AFFICHAGE_LIGNE_VERTICALE = '|';
    public static final String PATTERN_DATE_PAR_DEFAUT = "dd/MM/yyyy";

    private AffichageConsole() {
    }

    public static void afficherMessageAvecSautLigne(final String msg) {
        System.out.println(msg);
    }

    public static void afficherMessageSansSautLigne(final String msg) {
        System.out.print(msg);
    }

    public static void afficherErreur(final String msg) {
        final String message = String.format("Erreur : %s", msg);
        AffichageConsole.afficherMessageAvecSautLigne(message);
    }

    public static void afficherDate(final Date date, final String pattern) {
        SimpleDateFormat sdf;
        try {
            sdf = new SimpleDateFormat(pattern);
        } catch (IllegalArgumentException e) {
            sdf = new SimpleDateFormat(PATTERN_DATE_PAR_DEFAUT);
        }
        if (!Objects.isNull(date)) {
            System.out.println(sdf.format(date));
        }
    }

    public static void afficherDateAvecSautDeLigne(final Date date, final String pattern) {
        AffichageConsole.afficherDate(date, pattern);
        AffichageConsole.afficherMessageAvecSautLigne("");
    }

    public static void afficherMenuSimple(final List<String> options) {
        for (int i = 0; i < options.size(); i++) {
            AffichageConsole
                    .afficherMessageAvecSautLigne(String.format("%2d - %s", i + 1, options.get(i).toLowerCase()));
        }
        AffichageConsole.afficherChoix("CHOIX");
    }

    public static void afficherMenuSimple(final Map<Integer, String> options) {
        for (Entry<Integer, String> option : options.entrySet()) {
            if (!option.getKey().equals(0)) {
                AffichageConsole.afficherMessageAvecSautLigne(String.format("%2d - %s", option.getKey(), option.getValue().toLowerCase()));
            }
        }
        if (options.containsKey(0)) {
            AffichageConsole.afficherMessageAvecSautLigne(String.format(" 0 - %s", options.get(0).toLowerCase()));
        }
        AffichageConsole.afficherChoix("CHOIX");
    }

    public static void afficherMenuSimpleAvecOptionSortie(final List<String> options, final String libelleSortie) {
        for (int i = 0; i < options.size(); i++) {
            AffichageConsole
                    .afficherMessageAvecSautLigne(String.format("%2d - %s", i + 1, options.get(i).toLowerCase()));
        }
        AffichageConsole.afficherMessageAvecSautLigne(String.format(" 0 - %s", libelleSortie));
        AffichageConsole.afficherChoix("CHOIX");
    }

    public static void afficherMenuEntoureAvecOptionSortie(final List<String> options, final String nomMenu) {
        int nombreMaxCaracteres = AffichageConsole.chercherNombreMaxiCaracteresChaines(options);
        if (nomMenu.length() > nombreMaxCaracteres) {
            nombreMaxCaracteres = nomMenu.length();
        }
        int longueurLigne = nombreMaxCaracteres + 6 + 6;
        AffichageConsole.afficherTitreMenuEntoure(nomMenu, longueurLigne);
        for (int i = 0; i < options.size(); i++) {
            AffichageConsole.afficherOptionEntouree(i + 1, options.get(i), longueurLigne);
        }
        AffichageConsole.afficherOptionEntouree(0, "sortir", longueurLigne);
        AffichageConsole.afficherBasMenuEntoure(longueurLigne);
        AffichageConsole.afficherChoix("CHOIX");
    }

    public static void afficherMenuEntoure(final Map<Integer, String> options, final String nomMenu) {
        int nombreMaxCaracteres = AffichageConsole.chercherNombreMaxiCaracteresChaines(new ArrayList<>(options.values()));
        if (nomMenu.length() > nombreMaxCaracteres) {
            nombreMaxCaracteres = nomMenu.length();
        }
        int longueurLigne = nombreMaxCaracteres + 6 + 6;
        AffichageConsole.afficherTitreMenuEntoure(nomMenu, longueurLigne);
        for (Entry<Integer, String> option : options.entrySet()) {
            if (option.getKey() != 0) {
                AffichageConsole.afficherOptionEntouree(option.getKey(), option.getValue(), longueurLigne);
            }
        }
        if (options.containsKey(0)) {
            AffichageConsole.afficherOptionEntouree(0, options.get(0), longueurLigne);
        }
        AffichageConsole.afficherBasMenuEntoure(longueurLigne);
        AffichageConsole.afficherChoix("CHOIX");
    }

    private static void afficherChoix(final String libelle) {
        System.out.printf("%s : ", libelle);
    }

    private static int chercherNombreMaxiCaracteresChaines(final List<String> lstChaine) {
        int nombreMax = 0;
        for (String chaine : lstChaine) {
            if (chaine.length() > nombreMax) {
                nombreMax = chaine.length();
            }
        }
        return nombreMax;
    }

    private static void afficherTitreMenuEntoure(final String nomMenu, final int longueurLigne) {
        StringBuilder sb = new StringBuilder();
        sb.append(AFFICHAGE_COIN_MENU);
        int nombreCaracteresHorizontal = ((longueurLigne - nomMenu.length()) / 2) - 1;
        sb.append(AffichageConsole.ligneDeCarateresHorizontaux(nombreCaracteresHorizontal,
                AFFICHAGE_LIGNE_HORIZONTALE));
        sb.append(String.format(" %s ", nomMenu));
        sb.append(AffichageConsole.ligneDeCarateresHorizontaux(nombreCaracteresHorizontal,
                AFFICHAGE_LIGNE_HORIZONTALE));
        sb.append(AFFICHAGE_COIN_MENU);
        System.out.println(sb);
    }

    private static void afficherOptionEntouree(final int numeroOption, final String option, final int longueurLigne) {
        StringBuilder sb = new StringBuilder();
        sb.append(AFFICHAGE_LIGNE_VERTICALE);
        sb.append(String.format(" %2d - %s", numeroOption, option));
        int nombreCaracteresHorizontal = longueurLigne - (6 + option.length());
        sb.append(AffichageConsole.ligneDeCarateresHorizontaux(nombreCaracteresHorizontal, ' '));
        sb.append(AFFICHAGE_LIGNE_VERTICALE);
        System.out.println(sb);
    }

    private static void afficherBasMenuEntoure(final int longueurLigne) {
        String sb = AFFICHAGE_COIN_MENU +
                AffichageConsole.ligneDeCarateresHorizontaux(longueurLigne, AFFICHAGE_LIGNE_HORIZONTALE) +
                AFFICHAGE_COIN_MENU;
        System.out.println(sb);
    }

    private static String ligneDeCarateresHorizontaux(final int nombreCaracteres, final char caractere) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < nombreCaracteres; i++) {
            sb.append(caractere);
        }
        return sb.toString();
    }
}
