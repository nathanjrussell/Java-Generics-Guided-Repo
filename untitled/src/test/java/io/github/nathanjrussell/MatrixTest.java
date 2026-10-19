package io.github.nathanjrussell;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MatrixTest {
    private final Matrix<ZModElement> a = matrix(new int[][]{{1, 2}, {0, 1}});
    private final Matrix<ZModElement> b = matrix(new int[][]{{2, 0}, {1, 1}});

    @Test
    void constructorRejectsEmptyMatrices() {
        assertThrows(IllegalArgumentException.class, () -> new Matrix<ZModElement>(1, 1, List.of()));
    }

    @Test
    void constructorRejectsRectangularMatrices() {
        assertThrows(IllegalArgumentException.class,
                () -> new Matrix<>(1, 2, List.of(List.of(new ZModElement(1, () -> 3), new ZModElement(2, () -> 3)))));
    }

    @Test
    void constructorRejectsRaggedMatrices() {
        assertThrows(IllegalArgumentException.class, () -> new Matrix<>(2, 2, List.of(
                List.of(new ZModElement(1, () -> 3), new ZModElement(2, () -> 3)),
                List.of(new ZModElement(0, () -> 3))
        )));
    }

    @Test
    void constructorRejectsNullEntries() {
        List<ZModElement> row = Arrays.asList((ZModElement) null);

        assertThrows(NullPointerException.class, () -> new Matrix<>(1, 1, List.of(row)));
    }

    @Test
    void constructorCopiesInputLists() {
        List<ZModElement> row = new ArrayList<>(List.of(new ZModElement(1, () -> 3)));
        List<List<ZModElement>> rows = new ArrayList<>(List.of(row));
        Matrix<ZModElement> matrix = new Matrix<>(1, 1, rows);

        row.set(0, new ZModElement(2, () -> 3));
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
    void zeroIsTheAdditiveIdentityOnBothSides() {
        Matrix<ZModElement> zero = a.zero();

        assertMatrix(new int[][]{{0, 0}, {0, 0}}, zero);
        assertSameEntries(a, a.add(zero));
        assertSameEntries(a, zero.add(a));
    }

    @Test
    void oneIsTheMultiplicativeIdentityOnBothSides() {
        Matrix<ZModElement> one = a.one();

        assertMatrix(new int[][]{{1, 0}, {0, 1}}, one);
        assertSameEntries(a, a.multiply(one));
        assertSameEntries(a, one.multiply(a));
    }

    @Test
    void operationsRejectDifferentMatrixSizes() {
        Matrix<ZModElement> smaller = matrix(new int[][]{{1}});

        assertThrows(IllegalArgumentException.class, () -> a.add(smaller));
        assertThrows(IllegalArgumentException.class, () -> a.multiply(smaller));
        assertThrows(IllegalArgumentException.class, () -> smaller.add(a));
        assertThrows(IllegalArgumentException.class, () -> smaller.multiply(a));
    }

    @Test
    void operationsRejectDifferentEntryRings() {
        Matrix<ZModElement> modulusFive = new Matrix<>(2, 2, List.of(
                List.of(new ZModElement(1, () -> 5), new ZModElement(2, () -> 5)),
                List.of(new ZModElement(0, () -> 5), new ZModElement(1, () -> 5))
        ));

        assertThrows(IllegalArgumentException.class, () -> a.add(modulusFive));
        assertThrows(IllegalArgumentException.class, () -> a.multiply(modulusFive));
    }

    @Test
    void mixedModuliAreAcceptedButCanFailDuringArithmetic() {
        Matrix<ZModElement> mixed = new Matrix<>(2, 2, List.of(
                List.of(new ZModElement(1, () -> 3), new ZModElement(2, () -> 5)),
                List.of(new ZModElement(0, () -> 3), new ZModElement(1, () -> 3))
        ));

        assertEquals(3, mixed.getValue(0, 0).getModulus());
        assertEquals(5, mixed.getValue(0, 1).getModulus());
        assertThrows(IllegalArgumentException.class, () -> a.add(mixed));
        assertThrows(IllegalArgumentException.class, () -> a.multiply(mixed));
    }

    @Test
    void operationsLeaveOperandsUnchanged() {
        a.add(b);
        a.multiply(b);
        a.negate();
        a.zero();
        a.one();

        assertMatrix(new int[][]{{1, 2}, {0, 1}}, a);
        assertMatrix(new int[][]{{2, 0}, {1, 1}}, b);
    }

    @Test
    void oneByOneMatricesWork() {
        Matrix<ZModElement> first = matrix(new int[][]{{2}});
        Matrix<ZModElement> second = matrix(new int[][]{{1}});

        assertMatrix(new int[][]{{0}}, first.add(second));
        assertMatrix(new int[][]{{2}}, first.multiply(second));
        assertMatrix(new int[][]{{1}}, first.negate());
    }

    @Test
    void identitiesUseTheEntryRingRatherThanIntegerConstants() {
        Matrix<ZModElement> trivialRing = new Matrix<>(1, 1, List.of(List.of(new ZModElement(0, () -> 1))));

        assertEquals(0, trivialRing.zero().getValue(0, 0).getValue());
        assertEquals(0, trivialRing.one().getValue(0, 0).getValue());
        assertEquals(1, trivialRing.one().getValue(0, 0).getModulus());
        assertSameEntries(trivialRing, trivialRing.multiply(trivialRing.one()));
    }

    @Test
    void matrixEntriesCanThemselvesBeMatrices() {
        Matrix<Matrix<ZModElement>> first = new Matrix<>(1, 1, List.of(List.of(a)));
        Matrix<Matrix<ZModElement>> second = new Matrix<>(1, 1, List.of(List.of(b)));

        assertMatrix(new int[][]{{0, 2}, {1, 2}}, first.add(second).getValue(0, 0));
        assertMatrix(new int[][]{{1, 2}, {1, 1}}, first.multiply(second).getValue(0, 0));
        assertMatrix(new int[][]{{2, 1}, {1, 0}}, second.multiply(first).getValue(0, 0));
        assertMatrix(new int[][]{{2, 1}, {0, 2}}, first.negate().getValue(0, 0));
        assertMatrix(new int[][]{{0, 0}, {0, 0}}, first.zero().getValue(0, 0));
        assertMatrix(new int[][]{{1, 0}, {0, 1}}, first.one().getValue(0, 0));
    }

    @Test
    void additionIsAssociativeAndCommutative() {
        Matrix<ZModElement> c = matrix(new int[][]{{0, 1}, {2, 2}});

        assertSameEntries(a.add(b), b.add(a));
        assertSameEntries(a.add(b).add(c), a.add(b.add(c)));
    }

    @Test
    void multiplicationIsAssociativeAndDistributesOverAddition() {
        Matrix<ZModElement> c = matrix(new int[][]{{0, 1}, {2, 2}});

        assertSameEntries(a.multiply(b).multiply(c), a.multiply(b.multiply(c)));
        assertSameEntries(a.multiply(b.add(c)), a.multiply(b).add(a.multiply(c)));
        assertSameEntries(a.add(b).multiply(c), a.multiply(c).add(b.multiply(c)));
    }

    @ParameterizedTest
    @CsvSource({"0, 0", "0, 1", "1, 0", "-1, 1", "1, -1"})
    void constructorRejectsNonpositiveDimensions(int rows, int columns) {
        assertThrows(IllegalArgumentException.class, () -> new Matrix<ZModElement>(rows, columns, List.of()));
    }

    @Test
    void constructorRequiresTheExactNumberOfDefaultRows() {
        List<ZModElement> row = List.of(new ZModElement(1, () -> 3), new ZModElement(2, () -> 3));

        assertThrows(IllegalArgumentException.class, () -> new Matrix<>(2, 2, List.of(row)));
        assertThrows(IllegalArgumentException.class, () -> new Matrix<>(2, 2, List.of(row, row, row)));
    }

    @Test
    void constructorRejectsExtraDefaultColumns() {
        assertThrows(IllegalArgumentException.class,
                () -> new Matrix<>(1, 1, List.of(List.of(new ZModElement(1, () -> 3), new ZModElement(2, () -> 3)))));
    }

    @Test
    void constructorRejectsNullDefaultValues() {
        assertThrows(NullPointerException.class, () -> new Matrix<ZModElement>(1, 1, null));
    }

    @Test
    void getterReturnsEveryDefaultValueAtItsCoordinates() {
        Matrix<ZModElement> matrix = new Matrix<>(3, 3, List.of(
                List.of(new ZModElement(0, () -> 11), new ZModElement(1, () -> 11), new ZModElement(2, () -> 11)),
                List.of(new ZModElement(3, () -> 11), new ZModElement(4, () -> 11), new ZModElement(5, () -> 11)),
                List.of(new ZModElement(6, () -> 11), new ZModElement(7, () -> 11), new ZModElement(8, () -> 11))
        ));
        int[][] expected = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}};

        assertEquals(3, matrix.getRows());
        assertEquals(3, matrix.getColumns());
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                assertEquals(expected[row][column], matrix.getValue(row, column).getValue());
            }
        }
    }

    @Test
    void setterChangesOnlySelectedCellsAndKeepsDimensionsFixed() {
        a.setValue(0, 1, new ZModElement(0, () -> 3));
        a.setValue(1, 0, new ZModElement(2, () -> 3));

        assertMatrix(new int[][]{{1, 0}, {2, 1}}, a);
    }

    @ParameterizedTest
    @CsvSource({"-1, 0", "0, -1", "2, 0", "0, 2", "2, 2", "2147483647, 0", "0, 2147483647"})
    void accessorsRejectInvalidCoordinatesWithoutChangingValues(int row, int column) {
        assertThrows(IndexOutOfBoundsException.class, () -> a.getValue(row, column));
        assertThrows(IndexOutOfBoundsException.class, () -> a.setValue(row, column, new ZModElement(2, () -> 3)));
        assertMatrix(new int[][]{{1, 2}, {0, 1}}, a);
    }

    @Test
    void setterRejectsNullWithoutChangingTheEntry() {
        assertThrows(NullPointerException.class, () -> a.setValue(0, 1, null));

        assertMatrix(new int[][]{{1, 2}, {0, 1}}, a);
    }

    @Test
    void setterDoesNotChangeTheDefaultList() {
        List<ZModElement> row = new ArrayList<>(List.of(new ZModElement(1, () -> 3)));
        Matrix<ZModElement> matrix = new Matrix<>(1, 1, List.of(row));

        matrix.setValue(0, 0, new ZModElement(2, () -> 3));

        assertEquals(1, row.get(0).getValue());
        assertMatrix(new int[][]{{2}}, matrix);
    }

    @Test
    void arithmeticResultsHaveIndependentStorage() {
        List<Matrix<ZModElement>> results = List.of(a.add(b), a.multiply(b), a.negate(), a.zero(), a.one());
        for (Matrix<ZModElement> result : results) {
            result.setValue(0, 0, new ZModElement(2, () -> 3));
        }

        assertMatrix(new int[][]{{1, 2}, {0, 1}}, a);
        assertMatrix(new int[][]{{2, 0}, {1, 1}}, b);
    }

    @Test
    void setterStillAllowsMixedModuli() {
        a.setValue(0, 1, new ZModElement(2, () -> 5));

        assertEquals(5, a.getValue(0, 1).getModulus());
        assertThrows(IllegalArgumentException.class, () -> a.add(b));
        assertThrows(IllegalArgumentException.class, () -> a.multiply(b));
    }

    private static Matrix<ZModElement> matrix(int[][] values) {
        List<List<ZModElement>> rows = new ArrayList<>();
        for (int[] row : values) {
            List<ZModElement> entries = new ArrayList<>();
            for (int value : row) {
                entries.add(new ZModElement(value, () -> 3));
            }
            rows.add(entries);
        }
        return new Matrix<>(values.length, values[0].length, rows);
    }

    private static void assertMatrix(int[][] expected, Matrix<ZModElement> actual) {
        assertEquals(expected.length, actual.getRows());
        assertEquals(expected[0].length, actual.getColumns());
        for (int row = 0; row < expected.length; row++) {
            for (int column = 0; column < expected[row].length; column++) {
                ZModElement entry = actual.getValue(row, column);
                assertEquals(expected[row][column], entry.getValue(), "entry at " + row + ", " + column);
                assertEquals(3, entry.getModulus());
            }
        }
    }

    private static void assertSameEntries(Matrix<ZModElement> expected, Matrix<ZModElement> actual) {
        assertEquals(expected.getRows(), actual.getRows());
        assertEquals(expected.getColumns(), actual.getColumns());
        for (int row = 0; row < expected.getRows(); row++) {
            for (int column = 0; column < expected.getColumns(); column++) {
                assertEquals(expected.getValue(row, column), actual.getValue(row, column));
            }
        }
    }
}
