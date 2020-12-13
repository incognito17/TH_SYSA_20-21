package chaos_min;

public class Ljapunov2DFuncLogist {

    public double P(double p, double x) {
        return (p * x * (1 - x));
    }

    public double PAbleitung(double p, double x) {
        return (p * (1 - 2.0 * x));
    }

    public double Q(double q, double x) {
        return (q * x * (1 - x));
    }

    public double QAbleitung(double q, double x) {
        return (q * (1 - 2.0 * x));
    }

    public double f(double p, double q, double x) {
        return Q(q, Q(q, P(p, x)));
    }

    public double lambdaPQ(double p, double q) {
        double returnValue = 0;
        double x_new = 0.5;
        // je höher N, desto genauer die Berechnung
        int N = 1000;
        for (int i = 0; i < N; i++) {
            x_new = f(p, q, x_new);
            // Berechne Wert der Ableitung QAbl(P(x))*PAbl(x)
            double derivation = QAbleitung(q, Q(q, P(p, x_new))) * QAbleitung(q, P(p, x_new)) * PAbleitung(p, x_new);

            // ln(0) nicht definiert
            if (Math.abs(derivation) != 0) {
                returnValue = returnValue + Math.log(Math.abs(derivation));
            } else {
                // System.out.println("seltsam");
            }
        }
        returnValue = returnValue / N;
        return returnValue;
    }

}
