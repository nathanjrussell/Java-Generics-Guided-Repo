package io.github.nathanjrussell;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        ZMod a = new ZMod(3, 7);
        ZMod b = new ZMod(4, 7);

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
        System.out.println("a equals -4 modulo 7 = " + a.equals(new ZMod(-4, 7)));

        // These elements have the same value but belong to different rings.
        try {
            ZMod otherRing = new ZMod(3, 5);
            a.equals(otherRing);
        } catch (IllegalArgumentException exception) {
            System.out.println("Cannot compare different rings. " + exception.getMessage());
        }

        // Every entry in these matrices is an integer modulo 3.
        Matrix<ZMod> matrixA = new Matrix<>(List.of(
                List.of(new ZMod(1, 3), new ZMod(2, 3)),
                List.of(new ZMod(0, 3), new ZMod(1, 3))
        ));
        Matrix<ZMod> matrixB = new Matrix<>(List.of(
                List.of(new ZMod(2, 3), new ZMod(0, 3)),
                List.of(new ZMod(1, 3), new ZMod(1, 3))
        ));

        System.out.println("\nMatrices over integers modulo 3");
        printMatrix("A", matrixA);
        printMatrix("B", matrixB);
        printMatrix("A + B", matrixA.add(matrixB));
        printMatrix("A * B", matrixA.multiply(matrixB));
        printMatrix("B * A", matrixB.multiply(matrixA));
        printMatrix("negate(A)", matrixA.negate());
        printMatrix("negate(B) using negate(other)", matrixA.negate(matrixB));
        printMatrix("zero", matrixA.zero());
        printMatrix("one", matrixA.one());
    }

    private static void printMatrix(String label, Matrix<ZMod> matrix) {
        System.out.println(label);
        for (int row = 0; row < matrix.getSize(); row++) {
            for (int column = 0; column < matrix.getSize(); column++) {
                System.out.print(matrix.get(row, column).getValue() + " ");
            }
            System.out.println();
        }
    }
}
