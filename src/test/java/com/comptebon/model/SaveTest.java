package com.comptebon.model;

import org.junit.jupiter.api.BeforeEach;

import com.comptebon.model.Config;
import com.comptebon.model.Partie;
import com.comptebon.util.SaveManager;

public class SaveTest {
    private Config config;
    private Partie partie;

    @BeforeEach
    public void setUp() {
        config = new Config(6, 100, 999);
        partie = new Partie(config);
    }

    // implementer la logique avec l'interface graphique avant de faire les tests

    // @Test
    // public void testSave() {
    // partie.effectuerOperation(new Plaque(10), Operateur.DIVISION, new Plaque(2));
    // partie.effectuerOperation(new Plaque(5), Operateur.ADDITION, new Plaque(3));
    // partie.effectuerOperation(new Plaque(15), Operateur.MULTIPLICATION, new
    // Plaque(2));
    // partie.effectuerOperation(new Plaque(30), Operateur.SOUSTRACTION, new
    // Plaque(5));

    // SaveManager.save(partie);

    // }

    // @Test
    // public void testLoad() {
    // // il faut deja faire la logique de save pour faire le stest

    // }
}
