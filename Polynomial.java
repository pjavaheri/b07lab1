public class Polynomial {
    double [] coeffs;

    public Polynomial() {
        this.coeffs = new double[1];
        this.coeffs[0] = 0.;
    }


    public Polynomial(double [] new_coeffs) {
        this.coeffs = new double[new_coeffs.length];
        for (int i=0; i<coeffs.length; i++) {
            this.coeffs[i] = new_coeffs[i];
        }
    }

    public Polynomial add(Polynomial other_pol) {
        int this_len = this.coeffs.length;
        int other_len = other_pol.coeffs.length;
        Polynomial result = new Polynomial();
        result.coeffs = new double[Math.max(this_len, other_len)];

        for (int i=0; i<Math.max(this_len, other_len); i++) {
            if ((i < this_len) && (i < other_len)) {
                result.coeffs[i] = this.coeffs[i] + other_pol.coeffs[i];
            } else if (i < this_len) {
                result.coeffs[i] = this.coeffs[i];
            } else {
                result.coeffs[i] = other_pol.coeffs[i];
            }
        }

        return result;
    }

    public double evaluate(double x) {
        double ans = 0.;
        for (int i=0; i<this.coeffs.length; i++) {
            ans += this.coeffs[i] * Math.pow(x, i);
        }
        return ans;
    }

    public boolean hasRoot(double possible_root) {
        /*
        // To avoid issues in floating-point comparison:
        double tolerance = 1e-10
        return Math.abs(this.evaluate(possible_root)) < tolerance
        */

        return this.evaluate(possible_root) == 0.;
    }
}
