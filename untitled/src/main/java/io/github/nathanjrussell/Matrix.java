package io.github.nathanjrussell;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A square matrix with fixed dimensions and entries stored in lists of rows.
 * Matrices of the same size over the same ring form another ring.
 * The constructor checks shape, but does not check whether all entries
 * belong to the same ring, such as having the same ZModElement modulus.
 */
public class Matrix<T extends RingElement<T>> implements RingElement<Matrix<T>> {
    private final int rows;
    private final int columns;
    private final List<List<T>> values;

    public Matrix(int rows, int columns, List<List<T>> defaultValues) {
        if (rows <= 0 || columns <= 0) {
            throw new IllegalArgumentException("Matrix dimensions must be greater than zero");
        }
        if (rows != columns) {
            throw new IllegalArgumentException("A ring matrix must be square");
        }
        Objects.requireNonNull(defaultValues, "Default values must not be null");
        if (defaultValues.size() != rows) {
            throw new IllegalArgumentException("Default values must match the number of rows");
        }
        this.rows = rows;
        this.columns = columns;
        this.values = new ArrayList<>();
        for (List<T> row : defaultValues) {
            if (row.size() != columns) {
                throw new IllegalArgumentException("Each default row must match the number of columns");
            }
            List<T> storedRow = new ArrayList<>();
            for (T value : row) {
                storedRow.add(Objects.requireNonNull(value, "Matrix entries must not be null"));
            }
            values.add(storedRow);
        }
    }

    private Matrix(Matrix<T> source) {
        this(source.rows, source.columns, source.values);
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public T getValue(int row, int column) {
        return values.get(row).get(column);
    }

    public void setValue(int row, int column, T value) {
        values.get(row).set(column, Objects.requireNonNull(value, "Matrix entries must not be null"));
    }

    @Override
    public Matrix<T> add(Matrix<T> other) {
        requireSameSize(other);
        Matrix<T> result = new Matrix<>(this);
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                result.setValue(row, column, getValue(row, column).add(other.getValue(row, column)));
            }
        }
        return result;
    }

    @Override
    public Matrix<T> multiply(Matrix<T> other) {
        requireSameSize(other);
        Matrix<T> result = new Matrix<>(this);
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                T sum = getValue(row, 0).zero();
                for (int k = 0; k < columns; k++) {
                    T product = getValue(row, k).multiply(other.getValue(k, column));
                    sum = sum.add(product);
                }
                result.setValue(row, column, sum);
            }
        }
        return result;
    }

    @Override
    public Matrix<T> negate() {
        Matrix<T> result = new Matrix<>(this);
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                result.setValue(row, column, getValue(row, column).negate());
            }
        }
        return result;
    }

    @Override
    public Matrix<T> zero() {
        Matrix<T> result = new Matrix<>(this);
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                result.setValue(row, column, getValue(0, 0).zero());
            }
        }
        return result;
    }

    @Override
    public Matrix<T> one() {
        Matrix<T> result = zero();
        for (int index = 0; index < rows; index++) {
            result.setValue(index, index, getValue(0, 0).one());
        }
        return result;
    }

    private void requireSameSize(Matrix<T> other) {
        if (rows != other.rows || columns != other.columns) {
            throw new IllegalArgumentException("The matrices must have the same size");
        }
    }
}
