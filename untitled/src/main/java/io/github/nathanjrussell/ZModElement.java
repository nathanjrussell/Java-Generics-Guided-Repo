package io.github.nathanjrussell;

import java.util.Objects;

/**
 * Represents an element of the ring of integers modulo a fixed positive modulus.
 *
 * A ring is a mathematical object consisting of a set and two operations,
 * addition and multiplication, whose results remain in that set.
 * Addition is associative and commutative, multiplication is associative,
 * and multiplication distributes over addition.
 *
 * Integers wrapped in modular arithmetic with the same modulus form a ring.
 * This class provides the operations through {@link #add(ZModElement)} and
 * {@link #multiply(ZModElement)}. The additive identity is returned by {@link #zero()},
 * and the multiplicative identity is returned by {@link #one()}.
 * Adding zero or multiplying by one leaves an element unchanged.
 * Every element has an additive inverse, returned by {@link #negate()}.
 */
public class ZModElement implements RingElement<ZModElement> {
    private final int value;
    private final Modulus modulus;

    /**
     * @param value the element's input value (must be less than 1,000 and may be negative)
     * @param modulus the modulus (must be greater than zero and less than 1,000)
     */
    public ZModElement(int value, Modulus modulus) {
        Objects.requireNonNull(modulus, "modulus must not be null");
        int modulusValue = modulus.value();
        if (modulusValue <= 0 || modulusValue >= 1_000) {
            throw new IllegalArgumentException("modulus must be greater than zero and less than 1,000");
        }
        if (value >= 1_000) {
            throw new IllegalArgumentException("value must be less than 1,000");
        }

        // Java's remainder can be negative, so add the modulus to normalize it.
        value = value % modulusValue;
        if (value < 0) {
            value = value + modulusValue;
        }

        this.modulus = modulus;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public int getModulus() {
        return modulus.value();
    }

    @Override
    public ZModElement add(ZModElement other) {
        if (other.getModulus() != this.getModulus()) {
            throw new IllegalArgumentException("The modulus value of the two operands must be equal");
        }
        int sum = this.value + other.value;
        return new ZModElement(sum % this.getModulus(), this.modulus);
    }

    /**
     * Returns the additive inverse in the same ring.
     * Adding this element to its inverse produces zero.
     */
    @Override
    public ZModElement negate() {
        return new ZModElement(-this.value, this.modulus);
    }

    @Override
    public ZModElement multiply(ZModElement other) {
        if (other.getModulus() != this.getModulus()) {
            throw new IllegalArgumentException("The modulus value of the two operands must be equal");
        }
        int product = this.value * other.value;
        return new ZModElement(product % this.getModulus(), this.modulus);
    }

    /** Returns the additive identity in the same ring. */
    @Override
    public ZModElement zero() {
        return new ZModElement(0, this.modulus);
    }

    /** Returns the multiplicative identity in the same ring. */
    @Override
    public ZModElement one() {
        return new ZModElement(1, this.modulus);
    }

    /**
     * Compares values within the same ring.
     *
     * @throws ClassCastException if the other object is null or is not a ZModElement
     * @throws IllegalArgumentException if the other ZModElement has a different modulus
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof ZModElement otherElement)) {
            throw new ClassCastException("Cannot compare ZModElements with different types");
        }
        if (otherElement.getModulus() != this.getModulus()) {
            throw new IllegalArgumentException("The modulus value of the two operands must be equal");
        }
        return this.value == otherElement.value;
    }


}
