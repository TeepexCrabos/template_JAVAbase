package views.commons.utils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Scanner;

/**
 * Cette classe encapsule la classe Scanner. Elle offre les services utiles à la
 * lecture des informations saisies par l'utilisateur.
 * FICHIER FOURNI EN COURS - ne pas modifier, juste coller dans le nouveau projet.
 *
 * @author PDI
 * @version 1.0
 */
public final class LectureConsole {

    private static final Scanner lectureClavier = new Scanner(System.in);

    private LectureConsole() {
        super();
    }

    public static int lectureChoixInt(final int index1, final int index2) {
        String message = "Choix invalide";
        int choix = -1;
        boolean boucle = true;
        do {
            boucle = false;
            try {
                String choixString = lectureClavier.nextLine();
                choix = Integer.parseInt(choixString);
                if (choix < index1 || choix > index2)
                    throw new NumberFormatException();
            } catch (NumberFormatException e) {
                System.out.println(message);
                boucle = true;
            }
        } while (boucle);

        return choix;
    }

    public static String lectureChaineCaracteres() {
        return lectureClavier.nextLine();
    }

    public static String lectureChaineCaracteres(final String entete) {
        AffichageConsole.afficherMessageSansSautLigne(entete);
        return lectureChaineCaracteres();
    }

    public static double lectureDouble() {
        double retour = -1;
        boolean boucle = true;
        do {
            boucle = false;
            try {
                String chaine = lectureClavier.nextLine();
                retour = Double.parseDouble(chaine);
            } catch (NumberFormatException e) {
                System.out.println("Choix invalide");
                boucle = true;
            }
        } while (boucle);

        return retour;
    }

    public static double lectureDouble(final String entete) {
        AffichageConsole.afficherMessageSansSautLigne(entete);
        return lectureDouble();
    }

    public static BigDecimal lectureBigDecimal() {
        BigDecimal retour = null;
        boolean boucle = true;
        do {
            boucle = false;
            try {
                String chaine = lectureClavier.nextLine();
                retour = new BigDecimal(chaine);
            } catch (NumberFormatException e) {
                System.out.println("Choix invalide");
                boucle = true;
            }
        } while (boucle);

        return retour;
    }

    public static BigDecimal lectureBigDecimal(final String entete) {
        AffichageConsole.afficherMessageSansSautLigne(entete);
        return lectureBigDecimal();
    }

    public static Date lectureDate(final String pattern) {
        SimpleDateFormat formater = new SimpleDateFormat(pattern);
        String dateString;
        Date dateJour = new Date();
        Date date = dateJour;
        boolean dateValide;
        do {
            try {
                dateValide = true;
                dateString = lectureClavier.nextLine();
                formater.setLenient(false);
                date = formater.parse(dateString);
            } catch (ParseException e) {
                System.out.println("Date erronée");
                dateValide = false;
            }
        } while (!dateValide);

        return date;
    }

    public static Date lectureDate(final String entete, final String pattern) {
        AffichageConsole.afficherMessageSansSautLigne(entete);
        return lectureDate(pattern);
    }

    public static LocalDate lectureLocalDate(final String pattern) {
        String dateString;
        LocalDate date = null;
        boolean dateValide;
        do {
            try {
                dateValide = true;
                dateString = lectureClavier.nextLine();
                date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern(pattern));
            } catch (DateTimeParseException e) {
                System.out.println("Date erronée");
                dateValide = false;
            }
        } while (!dateValide);

        return date;
    }

    public static LocalDate lectureLocalDate(final String entete, final String pattern) {
        AffichageConsole.afficherMessageSansSautLigne(entete);
        return lectureLocalDate(pattern);
    }

    public static int lectureEntier() {
        int retour = -1;
        boolean boucle = true;
        do {
            boucle = false;
            try {
                String chaine = lectureClavier.nextLine();
                retour = Integer.parseInt(chaine);
            } catch (NumberFormatException e) {
                System.out.println("Choix invalide");
                boucle = true;
            }
        } while (boucle);

        return retour;
    }

    public static int lectureEntierPositif() {
        int retour = -1;
        boolean boucle = true;
        do {
            boucle = false;
            try {
                String chaine = lectureClavier.nextLine();
                retour = Integer.parseInt(chaine);
                if(retour < 0) {
                    boucle = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Choix invalide");
                boucle = true;
            }
        } while (boucle);

        return retour;
    }

    public static int lectureEntier(String entete) {
        AffichageConsole.afficherMessageSansSautLigne(entete);
        return lectureEntier();
    }

    public static int lectureEntierPositif(String entete) {
        AffichageConsole.afficherMessageSansSautLigne(entete);
        return lectureEntierPositif();
    }

    public static boolean lectureBoolean(String libelle) {
        boolean resultat = false;

        AffichageConsole.afficherMessageSansSautLigne(String.format("%s (oui/non) : ", libelle));
        String reponse = LectureConsole.lectureChaineCaracteres().toUpperCase();
        switch (reponse) {
            case "OUI":
                resultat = true;
                break;
            case "O":
                resultat = true;
                break;
            case "NON":
                resultat = false;
                break;
            case "N":
                resultat = false;
                break;
            default:
                AffichageConsole.afficherErreur("La valeur saisie n'est pas valide");
                lectureBoolean(libelle);
                break;
        }

        return resultat;
    }
}
