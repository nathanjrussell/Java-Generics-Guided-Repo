package io.github.nathanjrussell;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        ZModElement a = new ZModElement(3, 7);
        ZModElement b = new ZModElement(4, 7);

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
        System.out.println("a equals -4 modulo 7 = " + a.equals(new ZModElement(-4, 7)));

        // These elements have the same value but belong to different rings.
        try {
            ZModElement otherRing = new ZModElement(3, 5);
            a.equals(otherRing);
        } catch (IllegalArgumentException exception) {
            System.out.println("Cannot compare different rings. " + exception.getMessage());
        }

        RingElement<ZModElement> element = a;
        System.out.println("negate through the interface = " + element.negate().getValue());

        // Every entry in these matrices is an integer modulo 3.
        ZModMatrix matrixA = new ZModMatrix(2, 2, List.of(
                List.of(new ZModElement(1, 3), new ZModElement(2, 3)),
                List.of(new ZModElement(0, 3), new ZModElement(1, 3))
        ));
        ZModMatrix matrixB = new ZModMatrix(2, 2, List.of(
                List.of(new ZModElement(2, 3), new ZModElement(0, 3)),
                List.of(new ZModElement(1, 3), new ZModElement(1, 3))
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

        matrixA.setValue(0, 1, new ZModElement(0, 3));
        printMatrix("A after setting row 0 column 1 to zero", matrixA);
    }

    private static void printMatrix(String label, ZModMatrix matrix) {
        System.out.println(label);
        for (int row = 0; row < matrix.getRows(); row++) {
            for (int column = 0; column < matrix.getColumns(); column++) {
                System.out.print(matrix.getValue(row, column).getValue() + " ");
            }
            System.out.println();
        }
    }
}
