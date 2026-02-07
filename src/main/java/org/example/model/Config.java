package org.example.model;

public class Config {
    private static int NB_PLAQUE = 6;
    private static int PLAGE_CIBLE_MIN = 100;
    private static int PLAGE_CIBLE_MAX = 999;
    private static boolean OPTION_CHRONO = false;
    // si on garde les attibut en static
    // alors il nest pas utilise d'instancier la classe config pour acceder a ces attributs et on ne pourrait plus faire une partie avec une config differente
    // soluton : faire une classe contenant les constantes et et attribut en non static 
    // dis moi tu as une autre solution

    public Config() {
    } // pour la config par defaut

    public Config(int nbPlaque, int plageCibleMin, int plageCibleMax, Boolean chrono) {
        this.NB_PLAQUE = nbPlaque;
        this.PLAGE_CIBLE_MIN = plageCibleMin;
        this.PLAGE_CIBLE_MAX = plageCibleMax;
        this.OPTION_CHRONO = chrono;
    }

    // Configuration par défaut standard "Compte est bon"

}
