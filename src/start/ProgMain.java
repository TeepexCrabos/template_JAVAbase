package start;

import presenter.Presenter;

/**
 * TEMPLATE - Point d'entrée du programme. Il ne fait QUE construire le
 * Presenter et lui passer la main : aucune logique ici, volontairement.
 * Tout le déroulé (menu, boucle, actions) appartient au Presenter -
 * si l'application changeait de chef d'orchestre (tests automatisés,
 * autre IHM), cette classe serait la seule à adapter.
 */
public class ProgMain {

    public static void main(String[] args) {
        Presenter presenter = new Presenter();
        presenter.start();
    }
}
