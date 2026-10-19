package io.github.nathanjrussell;

import java.util.ArrayList;
import java.util.List;

/**
 * A square matrix whose entries are elements of a ring.
 * Matrices of the same size over the same ring form another ring.
 * The constructor checks shape, but does not check whether all entries
 * belong to the same ring, such as having the same ZMod modulus.
 */
public class Matrix<T extends RingElement<T>> implements RingElement<Matrix<T>> {
    private final List<List<T>> entries;

    public Matrix(List<List<T>> entries) {
        if (entries.isEmpty()) {
            throw new IllegalArgumentException("A matrix must contain at least one row");
        }

        List<List<T>> rows = new ArrayList<>();
        for (List<T> row : entries) {
            if (row.size() != entries.size()) {
                throw new IllegalArgumentException("A ring matrix must be square");
            }
            rows.add(List.copyOf(row));
        }
        this.entries = List.copyOf(rows);
    }

    public int getSize() {
        return entries.size();
    }

    public T get(int row, int column) {
        return entries.get(row).get(column);
    }

    @Override
    public Matrix<T> add(Matrix<T> other) {
        requireSameSize(other);
        List<List<T>> result = new ArrayList<>();
        for (int row = 0; row < getSize(); row++) {
            List<T> resultRow = new ArrayList<>();
            for (int column = 0; column < getSize(); column++) {
                resultRow.add(get(row, column).add(other.get(row, column)));
            }
            result.add(resultRow);
        }
        return new Matrix<>(result);
    }

    @Override
    public Matrix<T> multiply(Matrix<T> other) {
        requireSameSize(other);
        List<List<T>> result = new ArrayList<>();
        for (int row = 0; row < getSize(); row++) {
            List<T> resultRow = new ArrayList<>();
            for (int column = 0; column < getSize(); column++) {
                T sum = get(row, 0).zero();
                for (int k = 0; k < getSize(); k++) {
                    T product = get(row, k).multiply(other.get(k, column));
                    sum = sum.add(product);
                }
                resultRow.add(sum);
            }
            result.add(resultRow);
        }
        return new Matrix<>(result);
    }

    @Override
    public Matrix<T> negate() {
        List<List<T>> result = new ArrayList<>();
        for (int row = 0; row < getSize(); row++) {
            List<T> resultRow = new ArrayList<>();
            for (int column = 0; column < getSize(); column++) {
                resultRow.add(get(row, column).negate());
            }
            result.add(resultRow);
        }
        return new Matrix<>(result);
    }

    @Override
    public Matrix<T> zero() {
        List<List<T>> result = new ArrayList<>();
        for (int row = 0; row < getSize(); row++) {
            List<T> resultRow = new ArrayList<>();
            for (int column = 0; column < getSize(); column++) {
                resultRow.add(get(0, 0).zero());
            }
            result.add(resultRow);
        }
        return new Matrix<>(result);
    }

    @Override
    public Matrix<T> one() {
        List<List<T>> result = new ArrayList<>();
        for (int row = 0; row < getSize(); row++) {
            List<T> resultRow = new ArrayList<>();
            for (int column = 0; column < getSize(); column++) {
                if (row == column) {
                    resultRow.add(get(0, 0).one());
                } else {
                    resultRow.add(get(0, 0).zero());
                }
            }
            result.add(resultRow);
        }
        return new Matrix<>(result);
    }

    private void requireSameSize(Matrix<T> other) {
        if (getSize() != other.getSize()) {
            throw new IllegalArgumentException("The matrices must have the same size");
        }
    }
}
