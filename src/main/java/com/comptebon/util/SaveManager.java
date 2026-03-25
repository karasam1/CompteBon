package com.comptebon.util;

import com.comptebon.model.Partie;
import com.comptebon.model.Plaque;
import com.comptebon.model.Operation;
import com.comptebon.model.Operateur;
import com.comptebon.model.Config;
import com.comptebon.dto.PartieSave;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.stage.FileChooser;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class SaveManager {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static void save(Partie partie) {
        if (partie == null) return;
        
        PartieSave dto = new PartieSave();
        dto.setCible(partie.getValeurCible());
        dto.setCfgNbPlaques(partie.getConfig().getNbPlaque());
        dto.setCfgMin(partie.getConfig().getPlageCibleMin());
        dto.setCfgMax(partie.getConfig().getPlageCibleMax());

        Stack<Operation> historyClone = new Stack<>();
        while (!partie.getHistorique().isEmpty()) {
            historyClone.push(partie.getHistorique().peek());
            partie.annulerDerniereOperation();
        }

        List<Integer> inits = new ArrayList<>();
        for (Plaque p : partie.getTirage()) {
            inits.add(p.getValeur());
        }
        dto.setInitialPlaques(inits);

        List<String> actions = new ArrayList<>();
        while (!historyClone.isEmpty()) {
            Operation op = historyClone.pop();
            actions.add(op.getP1().getValeur() + "|" + op.getOperateur().name() + "|" + op.getP2().getValeur());
            partie.effectuerOperation(op.getP1(), op.getOperateur(), op.getP2());
        }
        dto.setOperations(actions);

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sauvegarder la partie");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Tous les fichiers", "*"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers JSON", "*.json"));
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try {
                mapper.writeValue(file, dto);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Partie load() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Charger une partie");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Tous les fichiers", "*"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers JSON", "*.json"));
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            try {
                PartieSave dto = mapper.readValue(file, PartieSave.class);
                Config cfg = new Config(dto.getCfgNbPlaques(), dto.getCfgMin(), dto.getCfgMax());
                Partie partie = new Partie(cfg, dto.getCible(), dto.getInitialPlaques());
                
                if (dto.getOperations() != null) {
                    for (String action : dto.getOperations()) {
                        String[] parts = action.split("\\|");
                        if (parts.length == 3) {
                            int v1 = Integer.parseInt(parts[0]);
                            Operateur op = Operateur.valueOf(parts[1]);
                            int v2 = Integer.parseInt(parts[2]);
                            
                            Plaque p1 = trouverPlaque(partie, v1);
                            if (p1 != null) p1.setDisponible(false);
                            Plaque p2 = trouverPlaque(partie, v2);
                            if (p2 != null) p2.setDisponible(false);
                            
                            if (p1 != null && p2 != null) {
                                p1.setDisponible(true); p2.setDisponible(true);
                                partie.effectuerOperation(p1, op, p2);
                            }
                        }
                    }
                }
                return partie;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static Plaque trouverPlaque(Partie p, int val) {
        for (Plaque plaque : p.getTirage()) {
            if (plaque.isDisponible() && plaque.getValeur() == val) return plaque;
        }
        return null;
    }
}
