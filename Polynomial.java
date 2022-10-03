import java.util.Arrays;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.Double;
import java.lang.Integer;
import java.lang.String;
import java.io.PrintStream;

public class Polynomial {
    double [] coeffs;
    int [] powers;


    public Polynomial() {
        this.coeffs = new double[0];
        this.powers = new int[0];
    }


    public Polynomial(double [] new_coeffs, int [] new_powers) {
        this.coeffs = new double[new_coeffs.length];
        this.powers = new int[new_powers.length];
        for (int i=0; i<coeffs.length; i++) {
            this.coeffs[i] = new_coeffs[i];
            this.powers[i] = new_powers[i];
        }
        this.sort_fields();
    }

    public Polynomial(File infile) throws Exception{
        this.coeffs = new double[0];
        this.powers = new int[0];
        if (infile.exists()) {
            FileReader fd = new FileReader(infile);
            BufferedReader br = new BufferedReader(fd);
            String line = br.readLine();
            br.close();
            double c = 0.;
            int n = 0;
            double operation = 1.;
            String [] sublines, term;
            if (line == null)
                return;
            while (!line.isEmpty()) {
                if (line.indexOf('-') == 0) {
                    sublines = line.split("-", 2);
                    line = sublines[1];
                    operation = -1.;
                } else if (line.indexOf('+') == 0) {
                    sublines = line.split("\\+", 2);
                    line = sublines[1];
                    operation = 1.;
                } else if ((line.indexOf('-') != -1) && ((line.indexOf('-') < line.indexOf('+')) || (line.indexOf('+') == -1))) {
                    sublines = line.split("-", 2);
                    if (sublines[0].indexOf('x') > 0) {
                        term = sublines[0].split("x", 2);
                        c = operation * Double.parseDouble(term[0]);
                        if (!term[1].isEmpty())
                            n = Integer.parseInt(term[1]);
                        else
                            n = 1;
                    } else if (sublines[0].indexOf('x') == 0) {
                        term = sublines[0].split("x", 2);
                        c = operation * 1.;
                        if (!term[1].isEmpty())
                            n = Integer.parseInt(term[1]);
                        else
                            n = 1;
                    } else {
                        term = sublines[0].split("x", 2);
                        c = operation * Double.parseDouble(term[0]);
                        n = 0;
                    }
                    operation = -1.;
                    line = sublines[1];
                } else if (line.indexOf('+') != -1) {
                    sublines = line.split("\\+", 2);
                    if (sublines[0].indexOf('x') > 0) {
                        term = sublines[0].split("x", 2);
                        c = operation * Double.parseDouble(term[0]);
                        if (!term[1].isEmpty())
                            n = Integer.parseInt(term[1]);
                        else
                            n = 1;
                    } else if (sublines[0].indexOf('x') == 0) {
                        term = sublines[0].split("x", 2);
                        c = operation * 1.;
                        if (!term[1].isEmpty())
                            n = Integer.parseInt(term[1]);
                        else
                            n = 1;
                    } else {
                        term = sublines[0].split("x", 2);
                        c = operation * Double.parseDouble(term[0]);
                        n = 0;
                    }
                    operation = 1.;
                    line = sublines[1];
                } else {
                    if (line.indexOf('x') > 0) {
                        term = line.split("x", 2);
                        c = operation * Double.parseDouble(term[0]);
                        if (!term[1].isEmpty())
                            n = Integer.parseInt(term[1]);
                        else
                            n = 1;
                    } else if (line.indexOf('x') == 0) {
                        term = line.split("x", 2);
                        c = operation * 1.;
                        if (!term[1].isEmpty())
                            n = Integer.parseInt(term[1]);
                        else
                            n = 1;
                    } else {
                        term = line.split("x", 2);
                        c = operation * Double.parseDouble(term[0]);
                        n = 0;
                    }
                    line = "";
                }
                this.set_coeff_to_power(this.get_coeff_from_power(n) + c, n);
            }
        } else {
            System.out.println("File not found. Initializing with 0.");
            this.coeffs = new double[0];
            this.powers = new int[0];
        }
        this.remove_redundant();
    }

    public Polynomial(Polynomial init) {
        //makes a copy
        this.coeffs = new double[init.coeffs.length];
        this.powers = new int[init.powers.length];
        for (int i=0; i<coeffs.length; i++) {
            this.coeffs[i] = init.coeffs[i];
            this.powers[i] = init.powers[i];
        }
        this.sort_fields();
    }

    public void saveToFile(String filename) throws Exception{
        String line = "";
        int n = 0;
        double c = 0.;
        String c_s = "";
        String operation = "";
        PrintStream ps = new PrintStream(filename);
        if (this.isZero()) {
            ps.println("0");
            ps.close();
            return;
        }
        for (int i = 0; i < this.powers.length; i++) {
            n = this.powers[i];

            c = Math.abs(this.coeffs[i]);
            if ((double)((int)c) - c == 0.)
                c_s = "" + (int)c;
            else
                c_s = "" + c;

            operation = "";
            if (this.coeffs[i] > 0.)
                operation = "+";
            else
                operation = "-";

            if ((i > 0) || (operation.indexOf('-') != -1))
                line = line + operation;

            if (n == 0)
                line = line + c_s;
            else if (n == 1) {
                if (c == 1.)
                    line = line + "x";
                else if (c > 1.)
                    line = line + c_s + "x";
            } else {
                if (c == 1.)
                    line = line + "x" + n;
                else if (c > 1.)
                    line = line + c_s + "x" + n;
            }
        }
        ps.println(line);
        ps.close();
    }


    public Polynomial add(Polynomial other_pol) {
        Polynomial result = new Polynomial();

        for (int power:other_pol.powers)
            result.set_coeff_to_power(
                this.get_coeff_from_power(power)
                    + other_pol.get_coeff_from_power(power),
                power);

        for (int power:this.powers)
            result.set_coeff_to_power(
                this.get_coeff_from_power(power)
                    + other_pol.get_coeff_from_power(power),
                power
            );

        result.remove_redundant();
        return result;
    }


    public double evaluate(double x) {
        double ans = 0.;
        for (int i=0; i<this.coeffs.length; i++)
            ans += this.coeffs[i] * Math.pow(x, this.powers[i]);

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


    private static boolean array_contians(int [] array, int val) {
        for (int a:array)
            if (a == val)
                return true;
        return false;
    }

    private static boolean array_contians(double [] array, double val) {
        for (double a:array)
            if (a == val)
                return true;
        return false;
    }

    public Polynomial multiply(Polynomial other_pol) {
        Polynomial result = new Polynomial();

        for (int i = 0; i < this.powers.length; i++)
            for (int j = 0; j < other_pol.powers.length; j++) {
                double coeff = this.get_coeff_from_power(this.powers[i])
                                * other_pol.get_coeff_from_power(other_pol.powers[j]);
                int power = this.powers[i] + other_pol.powers[j];
                result.set_coeff_to_power(
                    result.get_coeff_from_power(power) + coeff,
                    power
                );

            }
        result.remove_redundant();
        return result;
    }

    private void remove_redundant() {
        int i = this.powers.length - 1;
        while (Polynomial.array_contians(this.coeffs, 0.) && i >= 0) {
            if (this.coeffs[i] == 0.)
                this.remove_power(this.powers[i]);
            i--;
        }
    }

    private void remove_power(double power) {
        if (this.powers.length < 1)
            return;
        int [] new_powers = new int[this.powers.length - 1];
        double [] new_coeffs = new double[this.powers.length - 1];
        boolean found = false;

        for (int i = 0; i < this.powers.length; i++)
            if (this.powers[i] == power)
                found = true;
            else if (found) {
                new_coeffs[i-1] = this.coeffs[i];
                new_powers[i-1] = this.powers[i];
            } else {
                new_coeffs[i] = this.coeffs[i];
                new_powers[i] = this.powers[i];
            }

        this.powers = new_powers;
        this.coeffs = new_coeffs;
    }

    public void set_coeff_to_power(double new_coeff, int power) {
        //searches through this
        boolean found = false;
        for (int i = 0; i < this.powers.length; i++)
            if (power == this.powers[i]) {
                this.coeffs[i] = new_coeff;
                found = true;
            }
        if (!found) {
            this.powers = Arrays.copyOf(this.powers, this.powers.length + 1);
            this.coeffs = Arrays.copyOf(this.coeffs, this.coeffs.length + 1);
            this.powers[this.powers.length-1] = power;
            this.coeffs[this.coeffs.length-1] = new_coeff;
        }
        this.sort_fields();
    }


    public double get_coeff_from_power(int power) {
        for (int i = 0; i < this.powers.length; i++)
            if (this.powers[i] == power)
                return this.coeffs[i];
        return 0.;
    }


    private void sort_fields() {
        double temp_coeff = 0.;
        int temp_power = 0;
        for (int i = 0; i < this.powers.length-1; i++)
            for (int j = 0; j < this.powers.length-i-1; j++)
                if (this.powers[j] > this.powers[j+1]) {
                    temp_power = this.powers[j+1];
                    temp_coeff = this.coeffs[j+1];
                    this.powers[j+1] = this.powers[j];
                    this.coeffs[j+1] = this.coeffs[j];
                    this.powers[j] = temp_power;
                    this.coeffs[j] = temp_coeff;
                }
    }

    private boolean isZero() {
        if (this.powers.length == 0)
            return true;
        else
            for (double coeff:this.coeffs)
                if (coeff != 0.)
                    return false;
        return true;
    }
}
