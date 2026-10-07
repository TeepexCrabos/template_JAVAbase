package models.entities;

import models.exception.MetierException;

import models.utils.DateUtils;

import java.time.LocalDate;
import java.util.Objects;

/**
 * TEMPLATE - Sous-classe avec un attribut date. Modèle à renommer :
 * Entraineur -> Bibliothécaire, Auteur...
 */
public class Entraineur extends Personne {

    private LocalDate dateNaissance;

    public Entraineur(String nom, String prenom, LocalDate dateNaissance) throws MetierException {
        super(nom, prenom);
        setDateNaissance(dateNaissance);
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    // On stocke la DONNEE STABLE (la date) ; l'âge, qui change chaque
    // année, est CALCULÉ à la demande par getAge() - un âge stocké serait
    // faux dès l'anniversaire suivant.
    public void setDateNaissance(LocalDate dateNaissance) throws MetierException {
        if (dateNaissance == null || dateNaissance.isAfter(LocalDate.now())) {
            throw new MetierException("Date de naissance invalide.");
        }
        this.dateNaissance = dateNaissance;
    }

    public Integer getAge() {
        return DateUtils.calculerAge(dateNaissance);
    }

    @Override
    public String fonction() {
        return "Entraîneur";
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Entraineur that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(dateNaissance, that.dateNaissance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), dateNaissance);
    }

    @Override
    public String presentation() {
        return "Je suis l'entraîneur " + getPrenom() + " " + getNom() + ", " + getAge() + " ans";
    }
}
