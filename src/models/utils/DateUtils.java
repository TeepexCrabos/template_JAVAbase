package models.utils;

import java.time.LocalDate;
import java.time.Period;

/**
 * TEMPLATE - Utilitaire technique de dates. Nommé d'après son contenu :
 * seules des méthodes de dates peuvent entrer ici.
 */
public final class DateUtils {

    private DateUtils() {
    }

    /** Âge en années révolues à aujourd'hui. */
    public static int calculerAge(LocalDate dateNaissance) {
        return Period.between(dateNaissance, LocalDate.now()).getYears();
    }
}
