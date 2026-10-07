package models.exception;

/**
 * TEMPLATE - Exception métier personnalisée (checked).
 *
 * ACTIVE dans ce template : toutes les validations de CHAMP la lèvent,
 * avec le "throws" propagé sur la chaîne setter -> constructeur ->
 * FactoryEntite -> façade (impl ET interface) -> Presenter (qui attrape).
 * Les règles de COLLECTION restent signalées par Resultat (jamais une
 * exception pour un échec attendu).
 *
 * ========================= PIEGE N°1 - VECU EN TEST ====================
 * "new MetierException(...)" seul CREE l'objet mais ne le LANCE PAS :
 * la méthode continue comme si de rien n'était, aucune règle ne s'applique,
 * et aucun catch ne se déclenche. La forme correcte est TOUJOURS :
 *
 *     throw new MetierException("...");
 *
 * Auto-contrôle avant rendu : Ctrl+Shift+F sur "new MetierException" -
 * chaque occurrence DOIT être précédée de "throw " (même chose pour toute
 * autre exception). Corollaire : une exception n'affiche JAMAIS rien
 * elle-même (pas de println dans ses constructeurs - c'est la vue qui
 * affiche getMessage(), et un println ici masquerait précisément ce bug).
 * =======================================================================
 *
 * Si un sujet ne demande PAS d'exception personnalisée, la version légère
 * est IllegalArgumentException (unchecked) : mêmes throw dans les setters,
 * ZERO "throws" à propager - retirer les throws et changer les catch.
 *
 * Si le sujet montre PLUSIEURS exceptions (ex : XxxException +
 * XxxFactoryException), dupliquer/renommer cette classe par couche, et
 * faire la TRADUCTION dans la couche intermédiaire en préservant la cause :
 *
 *     try {
 *         zone.verifier(texte);
 *     } catch (MetierException e) {
 *         throw new FactoryException("[Factory] " + e.getMessage(), e);
 *     }                                                            ^^^
 *                        toujours passer "e" en cause, sinon la
 *                        stacktrace d'origine est perdue.
 *
 * PIEGE vu en POC : le constructeur (message, cause) doit appeler
 * super(message, cause) - PAS super(message) seul, qui jette la cause.
 * PIEGE associé : comparer du String avec equals(), jamais == ou !=
 * ("mdp" != texte compare les références, marche par accident sur des
 * littéraux, casse sur une saisie Scanner).
 *
 * BONNES PRATIQUES (à savoir justifier à l'oral) :
 * - Une exception personnalisée ne se justifie que si elle APPORTE quelque
 *   chose par rapport aux standards du JDK ; sinon IllegalArgumentException
 *   avec un bon message suffit.
 * - checked (extends Exception) = situation RECUPERABLE que l'appelant doit
 *   gérer ; unchecked (extends RuntimeException) = erreur de programmation.
 *   Les sujets PDI demandent en général du checked.
 * - Message clair : dire QUOI a échoué et POURQUOI, avec les valeurs en
 *   cause ("Âge minimum requis : 7 ans", pas "erreur").
 * - Jamais d'exception pour le flux NORMAL du programme (un doublon attendu
 *   -> Resultat, pas une exception).
 * - catch du plus spécifique au plus général, et jamais de catch vide.
 *
 * REGLES DU COURS "Les exceptions" (le référentiel du correcteur) :
 * - "Les Exceptions, ça ne se subit pas, ça s'utilise !"
 * - Les exceptions METIER se placent dans le MODELE (ce package).
 * - Catcher les exceptions classiques pour les TRANSFORMER en métier.
 * - NE JAMAIS propager une exception telle quelle vers l'IHM : le Presenter
 *   attrape, la vue affiche getMessage(), jamais la stacktrace - et le
 *   main ne déclare JAMAIS "throws MetierException".
 * - finally : s'exécute dans tous les cas ; on n'y lève jamais d'exception.
 * - On étend Exception (checked), jamais Throwable ni Error.
 */
public class MetierException extends Exception {

    public MetierException(String message) {
        super(message);
    }

    public MetierException(String message, Throwable cause) {
        super(message, cause);
    }
}
