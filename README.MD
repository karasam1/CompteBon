CHANGEMENT DE L'HISTORIQUE

1. Le Nouveau Trio de Choc

Ton modèle métier repose désormais sur ces trois piliers :

    L'Enum Operateur (Le Cerveau) : * Contient les 4 types (+, −, ∗, /).

        Porte la logique de validation (ex: pas de division avec reste) et le calcul.

    La Classe Operation (La Mémoire) : * C'est ton "objet commande".

        Elle possède : Plaque p1, Plaque p2, Operateur type, et Plaque resultat.

        Elle fait le lien entre les ressources et l'action effectuée.

    La Classe Plaque (La Ressource) : * Contient la valeur et un booléen isDisponible.

2. Ce qui change pour l'Historique (Bye-bye TirageMemento)

Tu as parfaitement raison : la classe TirageMemento devient obsolète et peut être supprimée.

    Avant : Tu sauvegardais une "photo" de toutes les plaques (lourd).

    Maintenant : La classe Partie contient simplement une Stack<Operation> historique.

        Pour Jouer : Tu crées une Operation, tu l'exécutes, et tu la push dans la pile.

        Pour Annuler : Tu pop la dernière Operation. Comme elle connaît ses "parents" (p1 et p2), elle sait exactement lesquelles remettre à isDisponible = true et laquelle supprimer (le résultat).

4. Ce qu'il reste à gérer : Le DTO pour le JSON

Puisque tu n'as plus de "Memento" global, ton objet PartieSave (DTO) va simplement évoluer :

    Il contiendra la liste des plaques initiales.

    Il contiendra la liste des opérations effectuées (transformées de Stack en List pour Jackson).

Au rechargement, l'application pourra "rejouer" les opérations très vite pour reconstruire l'état visuel, ou simplement afficher le résultat final.