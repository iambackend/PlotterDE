import java.awt.geom.Point2D;
import java.util.ArrayList;

import static java.lang.Math.exp;

public class Equations {

    private final double X0 = -5;
    private final double X = 0;
    private final double Y0 = 2;
    private final double XODUS = (5 - 9 * exp(5)) / (2 * exp(5) - 1);// discontinuity point
    public int n = 50;
    public ArrayList<Point2D.Double> exact, euler, improved, runge;
    private double[] xs = new double[10000];
    private double h;

    public void calculateAllSolutions() {
        calculateXs();
        calculateExactSolution();
        calculateEulerSolution();
        calculateImprovedEulerSolution();
        calculateRungeKuttaSolution();
    }

    private void calculateXs() {
        double x = X0;
        h = (X - X0) / (n - 1);
        for (int i = 0; i < n; i++, x += h)
            xs[i] = x;
    }

    private double exactSolution(double x) {
        return exp(x) + (2 * exp(5) - 1) / (x + 5 - exp(5) * (2 * x + 9));
    }

    private void calculateExactSolution() {
        exact = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            exact.add(new Point2D.Double(xs[i], exactSolution(xs[i])));
        }
    }

    private double derivative(double x, double y) {
        return (1 - 2 * y) * exp(x) + y * y + exp(2 * x);
    }

    private void calculateEulerSolution() {
        euler = new ArrayList<>();
        euler.add(new Point2D.Double(X0, Y0));
        for (int i = 1; i < n; i++) {
            if (xs[i] >= XODUS) break;   // it is meaningless to use Euler Method after discontinuity point.
            // Also JavaFX plotter don't work with infinity value so better stop earlier.
            double prev = euler.get(i - 1).y;
            euler.add(new Point2D.Double(xs[i], prev + derivative(xs[i - 1], prev) * h));
        }
    }

    private void calculateImprovedEulerSolution() {
        improved = new ArrayList<>();
        improved.add(new Point2D.Double(X0, Y0));
        for (int i = 1; i < n; i++) {
            if (xs[i] >= XODUS) break;   // it is meaningless to use Euler Method after discontinuity point.
            // Also JavaFX plotter don't work with infinity value so better stop earlier.
            double prev = improved.get(i - 1).y;
            double firstIteration = derivative(xs[i - 1], prev);
            double yFirstApproximation = prev + firstIteration * h;
            improved.add(new Point2D.Double(xs[i], prev + (firstIteration + derivative(xs[i], yFirstApproximation)) * h / 2));
        }
    }

    private void calculateRungeKuttaSolution() {
        runge = new ArrayList<>();
        runge.add(new Point2D.Double(X0, Y0));
        for (int i = 1; i < n; i++) {
            if (xs[i] >= XODUS) break;   // it is meaningless to use Runge-Kutta Method after discontinuity point.
            // Also JavaFX plotter don't work with infinity value so better stop earlier.
            double prev = runge.get(i - 1).y;
            double k1 = h * derivative(xs[i - 1], prev);
            double k2 = h * derivative(xs[i - 1] + h / 2, prev + k1 / 2);
            double k3 = h * derivative(xs[i - 1] + h / 2, prev + k2 / 2);
            double k4 = h * derivative(xs[i], prev + k3);
            runge.add(new Point2D.Double(xs[i], prev + (k1 + 2 * k2 + 2 * k3 + k4) / 6));
            System.out.format("%d — x = %f, ey = %f,   iy = %f,    rky = %f     cy = %f%n", i, xs[i], euler.get(i).y,
                    improved.get(i).y, runge.get(i).y, exact.get(i).y);
        }
    }
}
