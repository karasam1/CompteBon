package org.example.model;

public class Config {
    private int nbPlaque = 6;
    private int plageCibleMin = 100;
    private int plageCibleMax = 999;
    private boolean optionChrono = false;

    /**
     * Crée une configuration avec les valeurs par défaut.
     */
    public Config() {
    }

    /**
     * Crée une configuration personnalisée.
     * Veiller que la configuration saisie soit coherente
     */
    public Config(int nbPlaque, int plageCibleMin, int plageCibleMax, boolean chrono) {
        this.nbPlaque = nbPlaque;
        this.plageCibleMin = plageCibleMin;
        this.plageCibleMax = plageCibleMax;
        this.optionChrono = chrono;
    }

    // Getters pour accéder aux paramètres de configuration
    public int getNbPlaque() {
        return nbPlaque;
    }

    public int getPlageCibleMin() {
        return plageCibleMin;
    }

    public int getPlageCibleMax() {
        return plageCibleMax;
    }

    public boolean isOptionChrono() {
        return optionChrono;
    }

    // Setters (optionnels, si on veut modifier la config après création)
    public void setNbPlaque(int nbPlaque) {
        this.nbPlaque = nbPlaque;
    }

    public void setPlageCibleMin(int plageCibleMin) {
        this.plageCibleMin = plageCibleMin;
    }

    public void setPlageCibleMax(int plageCibleMax) {
        this.plageCibleMax = plageCibleMax;
    }

    public void setOptionChrono(boolean optionChrono) {
        this.optionChrono = optionChrono;
    }
}
