package io.github.nathanjrussell;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MatrixTest {
    private final Matrix<ZMod> a = matrix(new int[][]{{1, 2}, {0, 1}});
    private final Matrix<ZMod> b = matrix(new int[][]{{2, 0}, {1, 1}});

    @Test
    void constructorRejectsEmptyMatrices() {
        assertThrows(IllegalArgumentException.class, () -> new Matrix<ZMod>(List.of()));
    }

    @Test
    void constructorRejectsRectangularMatrices() {
        assertThrows(IllegalArgumentException.class,
                () -> new Matrix<>(List.of(List.of(new ZMod(1, 3), new ZMod(2, 3)))));
    }

    @Test
    void constructorRejectsRaggedMatrices() {
        assertThrows(IllegalArgumentException.class, () -> new Matrix<>(List.of(
                List.of(new ZMod(1, 3), new ZMod(2, 3)),
                List.of(new ZMod(0, 3))
        )));
    }

    @Test
    void constructorRejectsNullEntries() {
        List<ZMod> row = Arrays.asList((ZMod) null);

        assertThrows(NullPointerException.class, () -> new Matrix<>(List.of(row)));
    }

    @Test
    void constructorCopiesInputLists() {
        List<ZMod> row = new ArrayList<>(List.of(new ZMod(1, 3)));
        List<List<ZMod>> rows = new ArrayList<>(List.of(row));
        Matrix<ZMod> matrix = new Matrix<>(rows);

        row.set(0, new ZMod(2, 3));
        rows.clear();

        assertMatrix(new int[][]{{1}}, matrix);
    }

    @Test
    void additionWorksEntryByEntryModuloThree() {
        assertMatrix(new int[][]{{0, 2}, {1, 2}}, a.add(b));
    }

    @Test
    void multiplicationUsesRowsAndColumnsModuloThree() {
        assertMatrix(new int[][]{{1, 2}, {1, 1}}, a.multiply(b));
    }

    @Test
    void multiplicationOrderMatters() {
        assertMatrix(new int[][]{{1, 2}, {1, 1}}, a.multiply(b));
        assertMatrix(new int[][]{{2, 1}, {1, 0}}, b.multiply(a));
    }

    @Test
    void negationGivesAnAdditiveInverse() {
        assertMatrix(new int[][]{{2, 1}, {0, 2}}, a.negate());
        assertMatrix(new int[][]{{0, 0}, {0, 0}}, a.add(a.negate()));
    }

    @Test
    void negateOtherUsesTheSuppliedMatrix() {
        assertMatrix(new int[][]{{1, 0}, {2, 2}}, a.negate(b));
    }

    @Test
    void zeroIsTheAdditiveIdentityOnBothSides() {
        Matrix<ZMod> zero = a.zero();

        assertMatrix(new int[][]{{0, 0}, {0, 0}}, zero);
        assertSameEntries(a, a.add(zero));
        assertSameEntries(a, zero.add(a));
    }

    @Test
    void oneIsTheMultiplicativeIdentityOnBothSides() {
        Matrix<ZMod> one = a.one();

        assertMatrix(new int[][]{{1, 0}, {0, 1}}, one);
        assertSameEntries(a, a.multiply(one));
        assertSameEntries(a, one.multiply(a));
    }

    @Test
    void operationsRejectDifferentMatrixSizes() {
        Matrix<ZMod> smaller = matrix(new int[][]{{1}});

        assertThrows(IllegalArgumentException.class, () -> a.add(smaller));
        assertThrows(IllegalArgumentException.class, () -> a.multiply(smaller));
        assertThrows(IllegalArgumentException.class, () -> smaller.add(a));
        assertThrows(IllegalArgumentException.class, () -> smaller.multiply(a));
    }

    @Test
    void operationsRejectDifferentEntryRings() {
        Matrix<ZMod> modulusFive = new Matrix<>(List.of(
                List.of(new ZMod(1, 5), new ZMod(2, 5)),
                List.of(new ZMod(0, 5), new ZMod(1, 5))
        ));

        assertThrows(IllegalArgumentException.class, () -> a.add(modulusFive));
        assertThrows(IllegalArgumentException.class, () -> a.multiply(modulusFive));
    }

    @Test
    void mixedModuliAreAcceptedButCanFailDuringArithmetic() {
        Matrix<ZMod> mixed = new Matrix<>(List.of(
                List.of(new ZMod(1, 3), new ZMod(2, 5)),
                List.of(new ZMod(0, 3), new ZMod(1, 3))
        ));

        assertEquals(3, mixed.get(0, 0).getModulus());
        assertEquals(5, mixed.get(0, 1).getModulus());
        assertThrows(IllegalArgumentException.class, () -> a.add(mixed));
        assertThrows(IllegalArgumentException.class, () -> a.multiply(mixed));
    }

    @Test
    void operationsLeaveOperandsUnchanged() {
        a.add(b);
        a.multiply(b);
        a.negate();
        a.negate(b);
        a.zero();
        a.one();

        assertMatrix(new int[][]{{1, 2}, {0, 1}}, a);
        assertMatrix(new int[][]{{2, 0}, {1, 1}}, b);
    }

    @Test
    void oneByOneMatricesWork() {
        Matrix<ZMod> first = matrix(new int[][]{{2}});
        Matrix<ZMod> second = matrix(new int[][]{{1}});

        assertMatrix(new int[][]{{0}}, first.add(second));
        assertMatrix(new int[][]{{2}}, first.multiply(second));
        assertMatrix(new int[][]{{1}}, first.negate());
    }

    @Test
    void identitiesUseTheEntryRingRatherThanIntegerConstants() {
        Matrix<ZMod> trivialRing = new Matrix<>(List.of(List.of(new ZMod(0, 1))));

        assertEquals(0, trivialRing.zero().get(0, 0).getValue());
        assertEquals(0, trivialRing.one().get(0, 0).getValue());
        assertEquals(1, trivialRing.one().get(0, 0).getModulus());
        assertSameEntries(trivialRing, trivialRing.multiply(trivialRing.one()));
    }

    @Test
    void matrixEntriesCanThemselvesBeMatrices() {
        Matrix<Matrix<ZMod>> first = new Matrix<>(List.of(List.of(a)));
        Matrix<Matrix<ZMod>> second = new Matrix<>(List.of(List.of(b)));

        assertMatrix(new int[][]{{0, 2}, {1, 2}}, first.add(second).get(0, 0));
        assertMatrix(new int[][]{{1, 2}, {1, 1}}, first.multiply(second).get(0, 0));
        assertMatrix(new int[][]{{2, 1}, {1, 0}}, second.multiply(first).get(0, 0));
        assertMatrix(new int[][]{{2, 1}, {0, 2}}, first.negate().get(0, 0));
        assertMatrix(new int[][]{{0, 0}, {0, 0}}, first.zero().get(0, 0));
        assertMatrix(new int[][]{{1, 0}, {0, 1}}, first.one().get(0, 0));
    }

    @Test
    void additionIsAssociativeAndCommutative() {
        Matrix<ZMod> c = matrix(new int[][]{{0, 1}, {2, 2}});

        assertSameEntries(a.add(b), b.add(a));
        assertSameEntries(a.add(b).add(c), a.add(b.add(c)));
    }

    @Test
    void multiplicationIsAssociativeAndDistributesOverAddition() {
        Matrix<ZMod> c = matrix(new int[][]{{0, 1}, {2, 2}});

        assertSameEntries(a.multiply(b).multiply(c), a.multiply(b.multiply(c)));
        assertSameEntries(a.multiply(b.add(c)), a.multiply(b).add(a.multiply(c)));
        assertSameEntries(a.add(b).multiply(c), a.multiply(c).add(b.multiply(c)));
    }

    private static Matrix<ZMod> matrix(int[][] values) {
        List<List<ZMod>> rows = new ArrayList<>();
        for (int[] row : values) {
            List<ZMod> entries = new ArrayList<>();
            for (int value : row) {
                entries.add(new ZMod(value, 3));
            }
            rows.add(entries);
        }
        return new Matrix<>(rows);
    }

    private static void assertMatrix(int[][] expected, Matrix<ZMod> actual) {
        assertEquals(expected.length, actual.getSize());
        for (int row = 0; row < expected.length; row++) {
            for (int column = 0; column < expected.length; column++) {
                ZMod entry = actual.get(row, column);
                assertEquals(expected[row][column], entry.getValue(), "entry at " + row + ", " + column);
                assertEquals(3, entry.getModulus());
            }
        }
    }

    private static void assertSameEntries(Matrix<ZMod> expected, Matrix<ZMod> actual) {
        assertEquals(expected.getSize(), actual.getSize());
        for (int row = 0; row < expected.getSize(); row++) {
            for (int column = 0; column < expected.getSize(); column++) {
                assertEquals(expected.get(row, column), actual.get(row, column));
            }
        }
    }
}
