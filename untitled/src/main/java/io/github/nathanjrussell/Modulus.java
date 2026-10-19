package io.github.nathanjrussell;

/**
 * A fixed positive modulus less than 1,000.
 * Implementations must be immutable and return the same value for every instance
 * of their class. Java cannot enforce this numerical rule through a type bound.
 */
public interface Modulus {
    int value();
}
