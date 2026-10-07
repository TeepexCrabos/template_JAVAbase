package models.entities;

import models.exception.MetierException;

import java.util.Objects;

/**
 * TEMPLATE - Classe MERE de tous les membres : porte ce que toutes les
 * sous-classes partagent (identité nom/prénom + validations) et impose
 * leur contrat commun (presentation, fonction).

 * Abstraite : une "Personne" seule n'a pas de sens métier, on n'instancie
 * que des sous-classes concrètes (Entraineur, Joueur) - le compilateur
 * interdit new Personne(...).

 * ENCAPSULATION (le principe n°1 du cours) : champs privés, accès par
 * getters/setters, et le Constructeur PASSE PAR LES SETTERS pour que
 * chaque validation ne soit écrite qu'à UN endroit - créer ou modifier,
 * même contrôle.
 */
public abstract class Personne {
    private String nom;
    private String prenom;

    protected Personne(String nom, String prenom) throws MetierException {
        setNom(nom);
        setPrenom(prenom);
    }

    public String getNom() { return nom; }
    public void setNom(String nom) throws MetierException {
        if (nom == null || nom.isBlank()) {
            throw new MetierException("Le nom ne peut pas être vide.");
        }
        this.nom = nom;
    }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) throws MetierException {
        if (prenom == null || prenom.isBlank()) {
            throw new MetierException("Le prénom ne peut pas être vide.");
        }
        this.prenom = prenom;
    }

    public abstract String presentation();

    // CONTRAT POLYMORPHE n°1 : chaque sous-classe se présente à sa façon,
    // l'appelant affiche sans connaître le type concret (action 4).
    // CONTRAT POLYMORPHE n°2 — le "poste" entre parenthèses dans les
    // listes de choix :
    // Polymorphisme au service de la vue, sans instanceof dans le Presenter.
    public abstract String fonction();

    // Pattern matching instanceof (Java 16+) : teste le type ET caste en
    // une seule instruction. Voir explication détaillée demandée par
    // l'utilisateur après cette modification (nuance de symétrie avec
    // getClass() quand une sous-classe redéfinit equals()).
    // IDENTITÉ COMMUNE : nom + prénom identifient toute Personne. Les
    // sous-classes qui ajoutent un champ d'unicité (ex : dateNaissance du
    // Joueur) COMPLÈTENT via super.equals(o) - jamais en le remplaçant.
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Personne personne)) return false;
        return Objects.equals(nom, personne.nom) && Objects.equals(prenom, personne.prenom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nom, prenom);
    }
}
