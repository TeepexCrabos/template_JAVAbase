package views.facadeView;

/**
 * TEMPLATE - Factory de la façade vue, symétrique de FactoryFacadeMetier
 * côté modèle. Centralise le choix de l'implémentation concrète de
 * IFacadeView (ici console).
 */
public final class FactoryFacadeView {

    private FactoryFacadeView() {
    }

    // PAS de Singleton ici, et c'est un choix : la vue console est SANS
    // ETAT (aucun attribut de données), deux instances seraient identiques
    // et inoffensives. Le Singleton se réserve aux ressources uniques par
    // nature (état partagé) - l'appliquer partout est l'anti-pattern.
    public static IFacadeView creerFacadeView() {
        return new ViewFacadeConsoleImpl();
    }
}
