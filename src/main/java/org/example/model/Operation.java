package org.example.model;

public class Operation {
    private final String operateur;

    public Operation(String operateur) {
        this.operateur = operateur;
    }

    public int calculer(int a, int b) {
        switch (operateur) {
            case "+": return a + b;
            case "-": return a - b;
            case "*": 
            case "×": return a * b;
            case "/": 
                if (b == 0) throw new ArithmeticException("Division par zéro");
                if (a % b != 0) throw new ArithmeticException("Division non entière");
                return a / b;
            default: throw new IllegalArgumentException("Opérateur inconnu : " + operateur);
        }
    }

    public String getOperateur() {
        return operateur;
    }

    @Override
    public String toString() {
        return operateur;
    }
}
