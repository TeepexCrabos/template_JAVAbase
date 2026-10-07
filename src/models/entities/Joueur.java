package models.entities;

import models.exception.MetierException;
import models.references.ConstanteMetier;
import models.references.Poste;

import models.utils.DateUtils;

import java.time.LocalDate;
import java.util.Objects;

/**
 * TEMPLATE - Sous-classe AVEC attributs propres. Modèle à renommer :
 * Joueur -> Adherent, Vin... Adapter les attributs et les setters.
 */
public class Joueur extends Personne {

    // Primitifs plutôt que WRAPPERS (double pas Double, boolean pas
    // Boolean) sauf si "peut être absent" est un vrai besoin du sujet.
    // PIEGE des wrappers : == et != comparent les REFERENCES.
    private int numero;
    private double taille;
    private LocalDate dateNaissance;
    private Poste poste;
    private boolean titulaire;


    public Joueur(String nom, String prenom, int numero, double taille,
                  LocalDate dateNaissance, Poste poste, boolean titulaire) throws MetierException {
        super(nom, prenom);
        setNumero(numero);
        setTaille(taille);
        setDateNaissance(dateNaissance);
        setPoste(poste);
        setTitulaire(titulaire);
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public double getTaille() {
        return taille;
    }

    public void setTaille(double taille) throws MetierException {
        if (taille < ConstanteMetier.TAILLE_MIN_CM || taille > ConstanteMetier.TAILLE_MAX_CM) {
            throw new MetierException(String.format(
                    "La taille doit être entre %d et %d cm.",
                    ConstanteMetier.TAILLE_MIN_CM, ConstanteMetier.TAILLE_MAX_CM));
        }
        this.taille = taille;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) throws MetierException {
        if (dateNaissance == null) {
            throw new MetierException("Date de naissance obligatoire.");
        }
        // PIEGE Classique : on recule LocalDate.now(), jamais dateNaissance.
        LocalDate dateLimite = LocalDate.now().minusYears(ConstanteMetier.AGE_MINIMAL);
        if (dateNaissance.isAfter(dateLimite)) {
            throw new MetierException(
                    "Âge minimum requis : " + ConstanteMetier.AGE_MINIMAL + " ans.");
        }
        this.dateNaissance = dateNaissance;
    }

    public Integer getAge() {
        return DateUtils.calculerAge(dateNaissance);
    }

    public Poste getPoste() {
        return poste;
    }

    public void setPoste(Poste poste) {
        this.poste = poste;
    }

    public boolean isTitulaire() {
        return titulaire;
    }

    public void setTitulaire(boolean titulaire) {
        this.titulaire = titulaire;
    }

    // Instanceof pattern matching, PUIS super.equals() : l'identité du
    // parent (nom+prénom) + celle du fils.
    // "Le X, Y et Z permettent de rendre… unique."
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Joueur j)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(dateNaissance, j.dateNaissance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), dateNaissance);
    }

    @Override
    public String fonction() {
        return poste.getLibelle();
    }

    @Override
    public String presentation() {
        return String.format("Je suis le joueur %s %s, n°%d, %s", getPrenom(), getNom(), numero, poste.getLibelle());
    }
}
