# CHECKLIST DE RELECTURE - les 15 dernières minutes du contrôle

Leçon du TF Drone : les points perdus n'étaient plus dans l'architecture,
ils étaient dans des détails d'une ligne. Cette passe se fait SUR LE CODE
FINI, dans l'ordre, montre en main (~1 min par point).

## Les 4 tueurs silencieux (Ctrl+Shift+F sur tout le projet)
1. `new ` + nom d'exception  -> chaque occurrence précédée de `throw ` ?
2. `== ` sur String ou wrapper (Double, Integer...) -> Objects.equals ?
   (equals des entités EN PREMIER : tout contains/rechercher repose dessus)
3. `choisirElement(` -> une garde isEmpty() AVANT, dans chaque action ?
4. `getClass().getSimpleName()` dans une CONDITION -> remplacer par
   instanceof (le nom en texte casse au moindre accent/renommage).

## Conformité au sujet (énoncé à la main, ligne par ligne)
5. Chaque VALEUR chiffrée du sujet == ma constante (10 chiffres, pas 15...).
6. Chaque action du menu fait EXACTEMENT ce que sa page décrit
   (("d'un pilote" = choisir un pilote, pas tout le parc).
7. Les champs calculés dans le BON sens (un "temps restant" est positif).
8. Titre du menu + libellés = ceux du sujet (pas ceux du template).

## Hygiène de rendu
9. Zéro commentaire TEMPLATE / zéro référence à l'ancien domaine (clubs...).
10. Fichiers fournis par le cours NON modifiés (le besoin passe par
    SaisieConsole.lireValide, jamais par une retouche de LectureConsole).
11. Champs d'entités tous `private` ; méthodes en camelCase.
12. main() sans throws : le Presenter attrape tout.

## Le test final : CHAQUE règle dans les DEUX sens
13. Pour chaque règle métier : un essai qui DOIT réussir (vert) et un
    essai qui DOIT échouer (rouge). Une règle testée seulement côté
    succès n'est pas testée - c'est comme ça que 5 "new sans throw"
    ont survécu à un test complet de l'application.
