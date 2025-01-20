# TP Mise en pratique des principes SOLID

## Mise en place

Récupérez le dossier `PokedexProject`. Il s'agit d'un projet configuré pour l'IDE IntelliJ, et le gestionnaire de build Gradle. 

Normalement le projet est correctement configuré pour pouvoir tout de suite compiler, lancer l'application et les tests unitaires sur votre machine
tant que l'environnement Java est correctement installé (systèmes Unix-like, c'est à dire Linux ou MacOS). 

Dans le dossier du projet, un fichier `README.md` donne les instructions pour compiler, lancer le projet, lancer les tests unitaires.

Si vous maitrisez bien l'environnement et les outils Java, vous pouvez utiliser d'autres outils de votre choix (autre IDE, système de build…),
mais il faudra que vous fournissiez un dossier de rendu avec un `README.md` similaire qui permette en quelques commandes simples de lancer
l'application et les tests unitaires.

## Sujet du TP

### Description du Pokédex 

On souhaite réaliser un pokédex basique en ligne de commande. Une invite interactive 
permet à l'utilisateur de paramétrer quelques options du Pokédex, et de demander à 
afficher les informations relatives à un ID de pokémon.

On attends un affichage texte qui ressemble à ça :

```
$ PokedexApplication
=============================
Pokémon # 1
Nom : Bulbizarre
Description : Il a une graine qui 
pousse sur son dos
Taille : 7
Poids : 69
=============================
```

### 1 - Obtention des données via l'API HTTP publique PokéAPI

Dans un premier temps, notre pokédex va utiliser l'API publique de Pokeapi pour obtenir les données du pokémon demandé. (voir la section Aides
de ce sujet, ainsi que le code example dans le projet PokedexProject pour voir comment utiliser l'API).

**Pour l'instant, nous allons seulement récupérer les données en anglais.**

L'API nous renvoit beaucoup d'infos pour un pokémon, mais pour cette application nous aurons besoin du
nom (en ang), de la description (en anglais ou en français), de la taille et du 
poids. Voir la section "Aide", à la fin de ce document.



#### Étapes d'intégration

##### PokedexRunner

Votre point d'entrée est la classe `PokedexRunner` qui hérite de `AbstractPokedexRunner`. Vous n'avez
pas à modifier cette dernière. `AbstractPokedexRunner` contient le code qui permet d'avoir l'invite
interactive avec l'utilisateur et de stocker les options choisies. La méthode `runPokedex()` est 
ensuite appelée. 

Dans un premier temps, on n'implémente pas les options du choix de la source (PokéAPI ou base de données
locale), on se contente d'utiliser PokéAPI.

On n'implémente pas non plus le choix de la langue, on se content de prendre les données en anglais.

Vous remplacerez le code de `PokedexRunner#runPokedex()` pour générer l'affichage attendu.

##### Suivre les principes SOLID

- Pour respecter le principe **Single Purpose Responsibility**, nous allons séparer
 notre code en classes qui assurent chacun une responsabilité bien définie : 

- Des classes de type "model", qui servent à instancier des objets qui représentent les entités de notre modèle métier (ici les Pokémon), elles stockent
  les données associées au modèle, et peut être des méthodes intrinsèquement liées à ces données. On les regroupes dans un package `models`. 
- Des classes de type "service", qui servent à effectuer des actions technique bas niveau, tel que l'accès à l'API HTTP, et qui renvoient de la donnée
  brute non modélisée. On les regroupe dans un package `services`
- Des classes de type "controller", qui font le lien entre les services et les modèles, elles utilisent les classes service pour récupérer des données
  brutes sans avoir à se soucier du fonctionnement bas niveau de l'accès à ces données, et instancient les classes modèles à partir des données récupérées. 
  On les regroupe dans un package `controllers`.
- Des classes de type "view", qui servent à génerer des représentations à destination de l'utilisateur final, à partir des instances des classe model.
  On les regroupe dans un package `views`.

Vous implémenterez donc, dans les packages adéquats, les classes suivantes `Pokemon` (modèle, sert 
à stocker les données du pokémon), `PokemonController` (controlleur, sert à instancier le modèle à 
partir du service bas niveau), `PokemonHttpProvider` (service, qui sert à implémenter la logique
bas niveau d'accès HTTP à l'API PokéAPI, et à fournir une interface abstraite pour accéder aux données
de la source), `PokemonView` (vue, qui sert à générer la représentation texte à destination de l'utilisateur,
en utilisant le modèle).

La classe `PokemonHttpProvider` doit implémente l'interface `PropertyProviderInterface` qui vous est
fournie. Dans un premier temps, vous ferez une implémentation vide pour les méthode 
`void setStringPropertyLocale(String localeCode);` et `String getStringPropertyLocale();`.
Elles serviront plus tard.

Du code d'example vous est donné dans le package `codesamples` pour voir comment utiliser
les librairies spécifiques pour faire des requêtes HTTP et manipuler le JSON renvoyé.

### 2 - Ajout du choix de la langue

Désormais, on va chercher à respecter le choix de l'utilisateur pour la langue, entre `"fr"` et `"en"`.

#### Utiliser la mécanique de AbstractPokedexRunner pour prendre en compte l'option utilisateur

Lorsque l'utilisateur choisit des options (dont la langue), `AbstractPokedexRunner` appelle
la méthode `onOptionsChange(DataSource dataSource, String dbPath)`.

Dans votre implémentation concrète `PokedexRunner` vous allez surcharger cette méthode afin
de pouvoir réagir aux changements d'options. Votre code ressemblera à ça :

```java
public class PokedexRunner extends AbstractPokedexRunner  {

    @Override
    public void onOptionsChange(DataSource dataSource, String dbPath) throws Exception {
        // write here your code that is run when the user changes the options
    }

    /* ... */
}
```

#### Rendre votre service "localizable"

Actuellement, l'interface `PropertyProviderInterface` défini 2 méthodes qui permettent de 
configurer un service pour prendre en compte une locale (càd la langue).

Cependant, ceci ne respecte pas le principe "Interface Segregation". Divisez cette interface
en deux interfaces distinctes : `PropertyProviderInterface` et  `LocalizedPropertyProviderInterface` 
pour respecter le principe.

Maintenant faites évoluer votre service pour qu'il implémente les deux interfaces. La méthode
`getStringProperty()` doit maintenant tenir compte de la locale qui aura été définie via
`setStringPropertyLocale()`.

Réfactor la méthode `AbstractPokemonRunner#setupServiceLocale()` pour qu'elle utilise l'interface
adaptée, c'est à dire `LocalizedPropertyProviderInterface`.

Enfin, pour mettre à jour la locale de votre service, voici à quoi devrait ressembler le code de 
votre `PokedexRunner` :

```java
public class PokedexRunner extends AbstractPokedexRunner  {

    PokemonHttpProvider myPokemonService;

    @Override
    public void onOptionsChange(DataSource dataSource, String dbPath) throws Exception {
        setupServiceLocale(myPokemonService);
    }

    /* ... */
}
```

### 3 - Nouvelle source de donnée possible : base de donnée locale

On veut maintenant pouvoir obtenir les données des pokémons depuis une autre source de données. On a une petite base de données locale
(fichier `pokemondatabase.sqlite`, fournit dans le dossier `ressources`). Il présente l'avantage d'être disponible en local 
(pas de risque d'indisponibilité du réseau ou du serveur distant).

En revanche, une seule langue est disponible dans la base de donnée. Donc la langue demandée par l'utilisateur
n'aura pas d'impact (afficher les données de la base de données, quelque soit la langue demandée en
option).

#### Intégration

Vous allez créer un nouveau service `PokemonSqliteProvider`. Pensez au principe de "Interface Segregation",
quelles interfaces doit implémenter ce service ?

#### Principe Dependency Inversion (et Open-Closed)

Notre classe controlleur dépend des services qui font l'accès bas niveau à la source de données. Pour respecter Dependency Inversion,
on veut que le controller dépende d'une **abstraction** de ce service, c'est à dire qu'on puisse facilement lui remplacer une implémentation
bas niveau concrète par une autre.

Si `PokemonController` a une dépendence "en dur" à `PokemonHttpProvider` ou `PokemonSqliteProvider`,
alors le principe n'est pas respecté.

La solution est de faire dépendre `PokemonController` d'une abstraction, qu'on nommera `AbstractPokemonProviderService`. 
`PokemonHttpProvider` et `PokemonSqliteProvider` héritent de cette classe
abstraite.

Ainsi, c'est le programme client `PokedexRunner` qui va "injecter" la dépendence concrète à 
`PokemonController`, en fonction des options choisies par l'utilisateur.


Celà permet aussi de respecter le principe Open-Closed, car une modification ultérieure du code (avec une source de données différente)
permettre de ne pas réfactorer le code du controller, ni celui des services déjà existants.



## Consignes pour le rendu

Livrables à rendre : 

- Le dossier avec l'application (l'équivalent du `PokedexProject` de départ). L'application doit être fonctionnelle, et pouvoir
  être lancée facilement en ligne de commande (laissez des instrutions claires dans un README, si vous n'avez pas repris la configuration
  Gradle fournie)

Consignes supplémentaires : 

- Commentez votre code. C'est une pratique indispensable à tout bon développeur, et ça permet à celui qui vous relit de comprendre ce que vous 
  avez voulu faire (dans ce cas c'est moi, et dans la vraie vie ça sera le vous du futur qui devra comprendre plusieurs mois après coup ce qu'il a codé)
- Pour les commentaires, suivre le standard Javadoc permet de structurer ses commentaires (et éventuellement de génerer de la documentation).
- Prenez l'habitude de coder en anglais, que ce soit pour le nommage de vos élément ou le commentaire du code.

Date de rendu : 20 janvier 2025 (à minuit).


Méthode de rendu : Sur la plateforme [eCampus](https://ecampus.emse.fr/).

## Aides

### Utilisation de la base de données SQLite

Base de données minimaliste stockée dans un seul fichier `pokemondatabase.sqlite`.

Voir <https://www.sqlite.org/index.html> pour en savoir plus sur SQLite.

Client graphique pour explorer et manipuler facilement la base de donnée : SQLiteBrowser https://sqlitebrowser.org/

Schéma de la base de donnée :

```
CREATE TABLE IF NOT EXISTS "pokemons" (
	`id`	INTEGER NOT NULL,
	`name`	TEXT,
	`description`	TEXT,
	`height`	INTEGER,
	`weight`	INTEGER,
	PRIMARY KEY(`id`)
);
```

Il y a des entrées pour les 5 premiers pokémons (ID de 1 à 5).

### Utilisation de l'API pokeapi

API au format REST publique, disponible à <https://pokeapi.co/>

Voici les 2 endpoints qui permettent de récupérer les infos nécessaire au TP.

#### Description de l'API HTTP

##### Taille et poids

`GET https://pokeapi.co/api/v2/pokemon/:id`  

Détail du JSON de réponse : 

```json
{
  height: 11,
  weight: 190
}
```

##### Nom et description

`GET https://pokeapi.co/api/v2/pokemon-species/:id`  

Détail du JSON de réponse  :

```json
{
  "names": [{
    "language": {
      "name": "en"
    },
    "name": "Charmander"
  }, {
    "language": {
      "name": "fr"
    },
    "name": "Salamèche"
  }],
  "flavor_text_entries": [{
    "language": {
      "name": "en"
    },
    "flavor_text": "Obviously prefers hot places. When it rains, steam is said to spout from the tip of its tail"
  }, {
    "language": {
      "name": "fr"
    },
    "flavor_text": "La flamme de sa queue symbolise sa vitalité. Elle est intense quand il est en bonne santé"
  }]
}
```


### Gradle avec Intellij

Normalement la configuration Gradle devrait marcher en l'état sans avoir à y toucher. Cependant pour ceux que ça intéresse, voici le guide 
de configuration d'un projet avec Gradle : <https://www.vogella.com/tutorials/JUnit/article.html>
