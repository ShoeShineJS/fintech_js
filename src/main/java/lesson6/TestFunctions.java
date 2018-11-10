package lesson6;

public abstract class TestFunctions {

    public static double rectangleArea(double x, double y) {
        if (x <= 0 || y <= 0) {
            throw new IllegalArgumentException("Side must be positive");
        }

        if (Double.MAX_VALUE / x < y || Double.MAX_VALUE / y < x) {
            throw new IllegalArgumentException("Side is too big");
        }

        return x*y;
    }

    public static double circleArea(double r) {
        if (r <= 0) {
            throw new IllegalArgumentException("Radius must be positive");
        }

        if (Double.MAX_VALUE / Math.PI / r < r) {
            throw new IllegalArgumentException("Radius is too big");
        }

        return Math.PI*r*r;
    }

    public static double triangleArea(double a, double h) {
        if (a <= 0) {
            throw new IllegalArgumentException("Side must be positive");
        }

        if (h <= 0) {
            throw new IllegalArgumentException("Altitude must be positive");
        }

        if (Double.MAX_VALUE / a < h) {
            throw new IllegalArgumentException("Side is too big");
        }

        if (Double.MAX_VALUE / h < a) {
            throw new IllegalArgumentException("Altitude is too big");
        }

        return a*h/2;
    }
}
