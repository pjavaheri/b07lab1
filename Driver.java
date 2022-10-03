import java.io.File;

public class Driver {
    public static void main(String [] args) throws Exception{
        Polynomial p = new Polynomial();
        System.out.println(p.evaluate(3));
        double [] c1 = {6., 5.};
        int [] n1 = {0, 3};
        Polynomial p1 = new Polynomial(c1, n1);
        p1.saveToFile("p1.txt");
        double [] c2 = {-2,-9};
        int [] n2 = {1, 4};
        Polynomial p2 = new Polynomial(c2, n2);
        p2.saveToFile("p2.txt");
        Polynomial s = p1.add(p2);
        System.out.println("s(0.1) = " + s.evaluate(0.1));
        if(s.hasRoot(1))
            System.out.println("1 is a root of s");
        else
            System.out.println("1 is not a root of s");
        Polynomial p1_reread = new Polynomial(new File("p1.txt"));
        Polynomial p2_reread = new Polynomial(new File("p2.txt"));
        if ((p1.multiply(p1).evaluate(17.) == p1.multiply(p1_reread).evaluate(17.)) &&
            (p2.add(p2).evaluate(17.) == p2.add(p2_reread).evaluate(17.)))
            System.out.println("Re-read, add, and multiplication passed.");
        else
            System.out.println("Re-read, add, or multiplication failed.");
    }
}
