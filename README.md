# Template Java Base — PDI (v8)

Squelette MVP réutilisable pour les tests Java de 4 h. Chaque classe est un
modèle à renommer/adapter, pas du code à comprendre le jour J : tout ce qui
demande réflexion est déjà tranché et documenté ici.

Le programme compile et TOURNE tel quel : lancer `ProgMain` et dérouler les
5 options du menu pour voir chaque mécanisme en action.

---

## 1. L'architecture en une image

```
        views                    presenter                 models
  ┌──────────────────┐      ┌───────────────┐      ┌─────────────────────────┐
  │ IFacadeView      │◄─────┤   Presenter   ├─────►│ IFacadeMetier           │
  │ ViewFacadeConsole│      │ (le chef      │      │ FacadeMetierImpl        │
  │ AffichageConsole │      │  d'orchestre) │      │   │ les REGLES métier   │
  │ LectureConsole   │      └───────────────┘      │   ▼                     │
  └──────────────────┘                             │ IDao<T>                 │
   parle à l'utilisateur                           │ DaoMemoireImpl<T>       │
   (affiche, saisit)                               │   │ le STOCKAGE (CRUD)  │
                                                   │   ▼                     │
                                                   │ entities / references   │
                                                   │ l'état + les validations│
                                                   └─────────────────────────┘
```

Règle de circulation : le Presenter ne connaît que les deux interfaces de
façade. La façade métier ne connaît que `IDao`. Personne ne saute une couche.
Chaque couche a sa Factory (`FactoryFacadeView`, `FactoryFacadeMetier`,
`FactoryDao`, `FactoryEntite`) : le Presenter n'écrit jamais `new`.

## 2. Visite guidée des packages

| Package | Contenu | À adapter le jour J |
|---|---|---|
| `models/entities` | `Personne` (abstraite), `Entraineur`, `Joueur`, `Conteneur<E>` (base générique), `Club`, `FactoryEntite` | Renommer selon les entités du sujet ; un `extends Conteneur<X>` par niveau |
| `models/references` | Enums (`Niveau`, `Poste`, `TypeMembre`), `ConstanteMetier`, `Resultat` | Valeurs des enums, constantes chiffrées, cas de `Resultat` |
| `models/exceptions` | `MetierException` (dormante) | Ne s'active que si le sujet demande une exception personnalisée |
| `models/dao` | `IDao<T>` (CRUD), `DaoMemoireImpl<T>` (générique), `FactoryDao` | Une méthode de 3 lignes dans FactoryDao par entité à cycle de vie propre (ici : clubs + catalogue des membres) |
| `models/facadeModel` | `IFacadeMetier`, `FacadeMetierImpl`, sa Factory | Les méthodes = les actions du menu |
| `presenter` | `Presenter` | Un `actionN...()` par option du menu |
| `views` | Façade vue générique + `ConstantesView` + utilitaires fournis en cours (ne pas modifier) | Les libellés du menu dans `ConstantesView` |
| `start` | `ProgMain` | Rien |

## 3. Validation : les TROIS mécanismes d'échec

C'est LE choix de conception à savoir justifier. La question à se poser :
*qui peut se tromper, et qui doit réagir ?*

| Situation | Mécanisme | Qui lève / retourne | Qui réagit |
|---|---|---|---|
| Un CHAMP est invalide (nom vide, borne, âge) | `MetierException` (checked, ACTIVE) — `throws` propagé jusqu'à l'interface façade | le setter de l'entité | try/catch dans le Presenter |
| Une règle de COLLECTION échoue (quota, doublon) — un échec *attendu* | `Resultat` (enum) | la façade métier | switch dans le Presenter |
| Le sujet ne demande PAS d'exception personnalisée | revenir à `IllegalArgumentException` (unchecked) : mêmes throw, zéro `throws` à propager | le setter | try/catch dans le Presenter |

Ils se COMBINENT : `action1CreerClub` montre try/catch + switch
sur la même action. Jamais d'exception pour un échec attendu (doublon →
`Resultat`), jamais d'exception brute vers l'IHM (règle du cours) : la vue
affiche `e.getMessage()`, point.

## 4. Ce que montre chaque action du Presenter

| Action | Mécanisme démontré |
|---|---|
| 1 — créer un club | combo try/catch + switch `Resultat` ; enum SIMPLE via la variante par défaut de `choisirDansEnum` |
| 2 — créer un membre (catalogue) | enum de type + switch expression → saisie orientée → `FactoryEntite` → son propre DAO |
| 3 — affecter un membre à un club | DEUX listes à garder ; règle INTER-CONTENEURS (un seul club par joueur) ; le joueur arrive toujours NON titulaire |
| 4 — afficher | POLYMORPHISME : `presentation()` sans connaître le type concret |
| 5 — retirer un membre d'un club | 2 niveaux, LAMBDA ; façade DEFENSIVE (`INTROUVABLE`) ; quitter le club REVOQUE le statut titulaire |
| 6 — supprimer un club | le cas simple, via la factorisation `choisirClub()` |
| 7 — afficher les titulaires | le FINDER du DAO spécifique + club d'appartenance via `rechercherClubDeMembre` |
| 8 — passer un joueur titulaire | INVARIANT inter-entités : titulaire seulement DANS un club ; règles max 11 + gardien unique vérifiées à la promotion |
| 9 — afficher les joueurs triés | TRI PARAMETRABLE : enum de critère -> switch expression -> fabrique `ComparateurUtils` |
| 10 — statistiques d'un club | vitrine de `compter` / `plusPetit` / `plusGrand` ; pure consultation, la façade ne change pas |

## 5. Workflow jour J (ordre conseillé)

1. **Lire tout le sujet**, surligner : entités + phrases d'unicité, règles
   chiffrées, principes exigés, options du menu.
2. **`models/references`** : enums du sujet, `ConstanteMetier`, cas de
   `Resultat` (un par règle de collection).
3. **`models/entities`** : renommer les entités ; `equals/hashCode`
   UNIQUEMENT sur la phrase d'unicité (« Le X et le Y permettent de rendre
   ... unique ») ; validations chiffrées dans les setters ; un
   `extends Conteneur<X>` par niveau.
4. **`FactoryDao`** : une méthode par entité à cycle de vie propre (créée/listée indépendamment de tout conteneur).
5. **`IFacadeMetier` + impl** : une méthode par action du menu ; règles de
   collection → `Resultat`.
6. **`ConstantesView`** : libellés du menu dans l'ordre du sujet.
7. **`Presenter`** : un `actionN` par option ; adapter les 5 modèles.
8. **Tester CHAQUE option et CHAQUE règle** (déclencher chaque échec au
   moins une fois). Garder 30 min pour ça.
9. Supprimer ce qui n'a pas servi (Niveau si inutilisé, MetierException si non
   demandée, TypeMembre si une seule sous-classe) : le code mort se voit.

## 6. Les pièges déjà rencontrés (et déjà câblés)

- `choisirElement` sur une liste vide = **boucle infinie** → toujours
  `isEmpty()` avant. Les gardes sont dans chaque action.
- `==` / `!=` sur des String ou des wrappers (`Double`, `Boolean`) compare
  les **références** → `equals()`, et primitifs plutôt que wrappers.
- `equals` sans `hashCode` (ou sur des champs différents) → contrat violé,
  `contains`/`rechercher` cassés. Le DAO repose sur `equals`.
- Le constructeur `(message, cause)` d'une exception doit appeler
  `super(message, cause)` — pas `super(message)`.
- Reculer `LocalDate.now()` pour comparer un âge, jamais la date saisie.
- La couleur des messages est un service de la VUE (codes ANSI dans
  ViewFacadeConsoleImpl), jamais du Presenter : afficherSucces/afficherEchec.
- STREAMS (cours "Les Flux", ADC POTACZALA) : ils vivent DANS les boîtes
  à outils. Correspondance avec le cours : filtrer=filter, compter=count,
  trier=sorted, filtrerParType=filter+map, somme=map+reduce(0, somme),
  affichage=forEach(Consumer), résultats via collect(Collectors.toList())
    - la FORME DU COURS, préférée à toList() qui n'y figure pas. Hors cours
      mais standard (à savoir justifier) : anyMatch (existe), min/max
      (plusPetit/plusGrand) - l'Optional qu'ils rendent est celui vu sur
      reduce. Rappels du cours : un stream ne stocke rien, ne modifie pas sa
      source, est à USAGE UNIQUE ; intermédiaire (filter, map) vs terminale
      (forEach, count, reduce, collect) - rien ne s'appelle après une
      terminale. En contrôle : streams directs OU via les outils, au choix -
      mais un pipeline doit s'expliquer maillon par maillon.
- BOITES A OUTILS - CIBLE INTER-PROJETS : chaque outil vise le maximum de
  cas de TOUS les sujets possibles, pas seulement les besoins du template
  courant (ex : tousVerifient existe pour les règles "que des X dans Y"
  même si aucune règle actuelle ne l'appelle ; parCle est la forme
  générale dont parTexte/parDate sont les spécialisations lisibles).
- BOITES A OUTILS (CollectionUtils, DateUtils, ChoixConsole) : uniquement
  des méthodes STATIQUES, SANS ETAT, GENERIQUES (<T> dès qu'il y a un type
  à abstraire) ou purement techniques. Test d'admission : la méthode
  compilerait telle quelle dans n'importe quel autre sujet. Une classe
  générique avec un ROLE (Conteneur<E>, IDao<T>) n'est PAS de la boîte à
  outils : c'est de l'architecture.
- Une Factory créée mais non utilisée (`new` restant dans le Presenter)
  ne compte pas comme principe mis en œuvre.
- Le `throws` d'une checked se déclare sur l'INTERFACE aussi, pas
  seulement l'implémentation.

## 7. Où est chaque « principe à mettre en œuvre »

| Principe du sujet | Dans le template |
|---|---|
| Polymorphisme | `Personne.presentation()` abstraite + action 3 |
| Énumération | `references` (simple, complexe avec libellé + toString du cours) |
| Facade | `IFacadeMetier`/`IFacadeView` + implémentations |
| Factory | `FactoryEntite`, `FactoryFacadeMetier`, `FactoryFacadeView`, `FactoryDao` |
| List | `Conteneur<E>` (déclaré interface, instancié `ArrayList`) |
| MVP | les 3 packages `models` / `views` / `presenter` |
| Exception | `models/exceptions/MetierException` — ACTIVE dans toutes les validations de champ |
| Singleton | canonique sur `FacadeMetierImpl` (état) ; constantes dans `FactoryDao` (générique) ; volontairement ABSENT de la vue (sans état) |
| DAO / CRUD | `models/dao` (à activer si demandé ou une fois le cours passé) |

---

*Attention : ce template va plus loin que les 7 cours actuels (héritage,
génériques, DAO non encore vus). Règle absolue : ne rendre que ce qu'on
peut expliquer ligne par ligne — replier vers la v4 si besoin.*
