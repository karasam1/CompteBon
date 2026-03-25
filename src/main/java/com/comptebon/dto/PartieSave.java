package com.comptebon.dto;

import java.util.List;
import java.util.ArrayList;

public class PartieSave {
    private int cible;
    private int cfgNbPlaques;
    private int cfgMin;
    private int cfgMax;
    private List<Integer> initialPlaques = new ArrayList<>();
    private List<String> operations = new ArrayList<>();

    public PartieSave() {} // Pour Jackson

    public int getCible() { return cible; }
    public void setCible(int cible) { this.cible = cible; }
    public int getCfgNbPlaques() { return cfgNbPlaques; }
    public void setCfgNbPlaques(int cfgNbPlaques) { this.cfgNbPlaques = cfgNbPlaques; }
    public int getCfgMin() { return cfgMin; }
    public void setCfgMin(int cfgMin) { this.cfgMin = cfgMin; }
    public int getCfgMax() { return cfgMax; }
    public void setCfgMax(int cfgMax) { this.cfgMax = cfgMax; }
    public List<Integer> getInitialPlaques() { return initialPlaques; }
    public void setInitialPlaques(List<Integer> initialPlaques) { this.initialPlaques = initialPlaques; }
    public List<String> getOperations() { return operations; }
    public void setOperations(List<String> operations) { this.operations = operations; }
}
