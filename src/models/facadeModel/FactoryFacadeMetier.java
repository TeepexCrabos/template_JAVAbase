package models.facadeModel;

/**
 * TEMPLATE - Factory de la façade métier : centralise le CHOIX de
 * l'implémentation concrète de IFacadeMetier, pour que le Presenter n'ait
 * jamais à écrire "new FacadeMetierImpl()" directement.
 * Symétrique de FactoryEntité (qui fabrique des ENTITÉS), mais celle-ci
 * fabrique la FAÇADE elle-même - à utiliser si le sujet montre explicitement
 * une "FacadeFactory" dans son architecture attendue.
 */
public final class FactoryFacadeMetier {

    private FactoryFacadeMetier() {
    }

    // Factory + Singleton se combinent : la Factory reste le point d'accès
    // public (le Presenter ne connaît qu'elle), le Singleton garantit
    // l'unicité derrière.
    public static IFacadeMetier creerFacadeMetier() {
        return FacadeMetierImpl.getInstance();
    }
}
