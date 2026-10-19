package io.github.nathanjrussell;

/**
 * Common operations for a ring element whose concrete type is T.
 * Implementations must obey the ring laws and return results without changing operands.
 * The interface specifies operations, but cannot enforce the mathematical laws.
 */
public interface RingElement<T extends RingElement<T>> {
    T zero();

    T one();

    T add(T other);

    T multiply(T other);

    /** Returns the additive inverse of this element. */
    T negate();

    /** Returns the additive inverse of the supplied element. */
    default T negate(T other) {
        return other.negate();
    }
}
