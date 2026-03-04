package org.example.model;

public enum Operateur {
    ADDITION("+") {
        @Override
        public int calculer(int a, int b) {
            return a + b;
        }

        @Override
        public void valider(int a, int b) throws ArithmeticException {
            // Toujours valide pour des entiers positifs
        }
    },
    SOUSTRACTION("-") {
        @Override
        public int calculer(int a, int b) {
            return a - b;
        }

        @Override
        public void valider(int a, int b) throws ArithmeticException {
            if (a < b) {
                throw new ArithmeticException("Le résultat d'une soustraction ne peut pas être négatif.");
            }
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