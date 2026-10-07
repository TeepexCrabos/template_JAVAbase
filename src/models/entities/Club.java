package models.entities;

import models.exception.MetierException;
import models.references.Niveau;

import java.time.LocalDate;
import java.util.Objects;

/**
 * TEMPLATE - Entité conteneur concrète : hérite de Conteneur<E> qui porte
 * toute la mécanique de collection (List, copie défensive, ajouter/
 * supprimer/contient/compter). Ne restent ici que l'IDENTITE (champs de la
 * phrase d'unicité + equals/hashCode) et les validations de champ.
 * Conteneur<Personne> : accepte TOUTES les sous-classes (Entraineur,
 * Joueur) et l'affichage appelle presentation() polymorphe.
 * Modèle à dupliquer par niveau de conteneur du sujet :
 *   Bibliotheque extends Conteneur<Rayon> ; Rayon extends Conteneur<Livre>.
 */
public class Club extends Conteneur<Personne> {

    private String nom;
    private LocalDate dateDeCreation;
    private Niveau niveau;

    public Club(String nom, LocalDate dateDeCreation, Niveau niveau) throws MetierException {
        setNom(nom);
        setDateDeCreation(dateDeCreation);
        setNiveau(niveau);
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) throws MetierException {
        if (nom == null || nom.isBlank()) {
            throw new MetierException("Le nom ne peut pas être vide.");
        }
        this.nom = nom;
    }

    public LocalDate getDateDeCreation() {
        return dateDeCreation;
    }

    public void setDateDeCreation(LocalDate dateDeCreation) {

        if (dateDeCreation.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La date ne peut pas être dans le futur.");
        }
        this.dateDeCreation = dateDeCreation;
    }

    public Niveau getNiveau() {
        return niveau;
    }

    public void setNiveau(Niveau niveau) {
        this.niveau = niveau;
    }

    // VARIANTE si le sujet impose une exception personnalisée (voir
    // models.exceptions.MetierException) : la signature devient
    //     public void setDateDeCreation(LocalDate d) throws MetierException
    // et le throw devient throw new MetierException("...").
    // Le "throws" se propage alors au constructeur, à la façade (impl ET
    // interface) et au Presenter — voir Presenter.action1CreerClub().

    // CONTRAT equals/hashCode : toujours ENSEMBLE, sur les MEMES champs
    // (ceux de la phrase d'unicité), jamais sur la collection ni un champ
    // qui bouge. Le niveau n'y est PAS : il ne fait pas partie de la
    // phrase d'unicité (nom + date), un club peut changer de niveau.
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Club club)) return false;
        return Objects.equals(nom, club.nom) && Objects.equals(dateDeCreation, club.dateDeCreation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nom, dateDeCreation);
    }
}
