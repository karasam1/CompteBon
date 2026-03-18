Partie :   
- supression de la classe Tirage car elle represente seulement une liste de plaques
- par consequent historisation se passera plus sur les tirage mais sur les operations car on est capable de retouver son etat precedent avec ses operations
- creation enumeration Operateur

Implémentation de la logique métier (Partie, Operateur, Operation) :
- Initialisation du tirage avec la génération d'une cible atteignable.
- Méthode d'exécution des opérations paramétrées et création des données dans l'historique gérant les entités `Plaque` produites.
- Ajout de l'opérateur "MULTIPLICATION" dans `Operateur`.
- Refactorisation de la classe `Operation` pour désormais lier les deux plaques originelles et l'opérateur utilisé avec la nouvelle plaque résultante permettant l'annulation.
- Ajout de classes de tests pour tester la logique mathématique et de gestion de la partie.
