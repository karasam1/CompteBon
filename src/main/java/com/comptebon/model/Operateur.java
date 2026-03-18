package com.comptebon.model;

public enum Operateur {
    ADDITION("+") {
        @Override
        public int calculer(int a, int b) {
            return a + b;
        }

        @Override
        public void valider(int a, int b) throws ArithmeticException {
            // tjr valide
        }
    },
    SOUSTRACTION("-") {
        @Override
        public int calculer(int a, int b) {
            return a - b;
        }

        @Override
        public void valider(int a, int b) throws ArithmeticException {
            // tjr valide
        }
    },
    DIVISION("/") {
        @Override
        public int calculer(int a, int b) {
            return a / b;
        }

        @Override
        public void valider(int a, int b) throws ArithmeticException {
            if (b == 0 || a % b != 0) {
                throw new ArithmeticException(
                        "La division doit donner un résultat entier et le diviseur ne peut être nul.");
            }
        }
    },
    MULTIPLICATION("*") {
        @Override
        public int calculer(int a, int b) {
            return a * b;
        }

        @Override
        public void valider(int a, int b) throws ArithmeticException {
            if (a == 0 || b == 0) {
                throw new ArithmeticException("La multiplication par 0 n'est pas autorisée.");
            }
        }
    };

    private final String symbole;

    Operateur(String symbole) {
        this.symbole = symbole;
    }

    public abstract int calculer(int a, int b);

    public abstract void valider(int a, int b) throws ArithmeticException;

    public String getSymbole() {
        return symbole;
    }
}