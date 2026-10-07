package models.dao;

import models.entities.Joueur;
import models.entities.Personne;

import java.util.List;

/**
 * TEMPLATE - DAO SPECIFIC : étend l'interface générique quand une entité
 * a besoin de requêtes de SELECTION en plus du CRUD ("finders").

 * Tout le CRUD (créer, lireTous, rechercher, mettreAJour, supprimer) est
 * hérité de IDao<Personne> ; on ne déclare ici que le supplément.
 * FRONTIERE : une SELECTION de données stockées ("récupère les X où...")
 * est légitime ici — en JDBC ce sera un SELECT… WHERE, filtré par la
 * base. Une DECISION métier (quota, Resultat) reste dans la Façade.

 * ATTENTION : ce finder porte sur le CATALOGUE ENTIER. Les règles "max 11
 * titulaires PAR CLUB" comptent sur club.getElements(), pas ici — deux
 * questions différentes.
 */
public interface IDaoMembre extends IDao<Personne> {

    List<Joueur> lireTitulaires();
}
