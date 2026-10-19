package io.github.nathanjrussell;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Mod3 mod3 = new Mod3();
        Mod5 mod5 = new Mod5();
        ZModElement<Mod3> a = new ZModElement<>(1, mod3);
        ZModElement<Mod3> b = new ZModElement<>(2, mod3);

        System.out.println("Working modulo " + a.getModulus());
        System.out.println("a = " + a.getValue());
        System.out.println("b = " + b.getValue());
        System.out.println("a + b = " + a.add(b).getValue());
        System.out.println("a * b = " + a.multiply(b).getValue());
        System.out.println("negate(a) = " + a.negate().getValue());
        System.out.println("zero = " + a.zero().getValue());
        System.out.println("one = " + a.one().getValue());
        System.out.println("a + negate(a) = " + a.add(a.negate()).getValue());
        System.out.println("a equals b = " + a.equals(b));
        System.out.println("a equals -2 modulo 3 = " + a.equals(new ZModElement<>(-2, mod3)));

        // These elements have the same value but belong to different rings.
        try {
            ZModElement<Mod5> otherRing = new ZModElement<>(1, mod5);
            // a.add(otherRing);       // Does not compile
            // a.multiply(otherRing);  // Does not compile
            a.equals(otherRing);
        } catch (IllegalArgumentException exception) {
            System.out.println("Cannot compare different rings. " + exception.getMessage());
        }

        // Every entry in these matrices is an integer modulo 3.
        Matrix<ZModElement<Mod3>> matrixA = new Matrix<>(2, 2, List.of(
                List.of(new ZModElement<>(1, mod3), new ZModElement<>(2, mod3)),
                List.of(new ZModElement<>(0, mod3), new ZModElement<>(1, mod3))
        ));
        Matrix<ZModElement<Mod3>> matrixB = new Matrix<>(2, 2, List.of(
                List.of(new ZModElement<>(2, mod3), new ZModElement<>(0, mod3)),
                List.of(new ZModElement<>(1, mod3), new ZModElement<>(1, mod3))
        ));

        System.out.println("\nMatrices over integers modulo 3");
        printMatrix("A", matrixA);
        printMatrix("B", matrixB);
        printMatrix("A + B", matrixA.add(matrixB));
        printMatrix("A * B", matrixA.multiply(matrixB));
        printMatrix("B * A", matrixB.multiply(matrixA));
        printMatrix("negate(A)", matrixA.negate());
        printMatrix("negate(B)", matrixB.negate());
        printMatrix("zero", matrixA.zero());
        printMatrix("one", matrixA.one());

        // matrixA.setValue(0, 1, new ZModElement<>(1, mod5)); // Does not compile
        matrixA.setValue(0, 1, new ZModElement<>(0, mod3));
        printMatrix("A after setting row 0 column 1 to zero", matrixA);
    }

    private static void printMatrix(String label, Matrix<ZModElement<Mod3>> matrix) {
        System.out.println(label);
        for (int row = 0; row < matrix.getRows(); row++) {
            for (int column = 0; column < matrix.getColumns(); column++) {
                System.out.print(matrix.getValue(row, column).getValue() + " ");
            }
            System.out.println();
        }
    }
}
