package io.github.nathanjrussell;

/**
 * Represents an element of the ring of integers modulo a fixed positive modulus.
 *
 * A ring is a mathematical object consisting of a set and two operations,
 * addition and multiplication, whose results remain in that set.
 * Addition is associative and commutative, multiplication is associative,
 * and multiplication distributes over addition.
 *
 * Integers wrapped in modular arithmetic with the same modulus form a ring.
 * This class provides the operations through {@link #add(ZMod)} and
 * {@link #multiply(ZMod)}. The additive identity is returned by {@link #zero()},
 * and the multiplicative identity is returned by {@link #one()}.
 * Adding zero or multiplying by one leaves an element unchanged.
 * Every element has an additive inverse, returned by {@link #negate()}.
 */
public class ZMod implements RingElement<ZMod> {
    private final int value;
    private final int modulus;

    /**
     * @param value the element's input value (must be less than 1,000 and may be negative)
     * @param modulus the modulus (must be greater than zero and less than 1,000)
     */
    public ZMod(int value, int modulus) {
        if (modulus <= 0 || modulus >= 1_000) {
            throw new IllegalArgumentException("modulus must be greater than zero and less than 1,000");
        }
        if (value >= 1_000) {
            throw new IllegalArgumentException("value must be less than 1,000");
        }

        // Java's remainder can be negative, so add the modulus to normalize it.
        value = value % modulus;
        if (value < 0) {
            value = value + modulus;
        }

        this.modulus = modulus;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public int getModulus() {
        return modulus;
    }

    @Override
    public ZMod add(ZMod other) {
        if (other.modulus != this.modulus) {
            throw new IllegalArgumentException("The modulus value of the two operands must be equal");
        }
        int sum = this.value + other.value;
        return new ZMod(sum % this.modulus, this.modulus);
    }

    /**
     * Returns the additive inverse in the same ring.
     * Adding this element to its inverse produces zero.
     */
    @Override
    public ZMod negate() {
        return new ZMod(-this.value, this.modulus);
    }

    @Override
    public ZMod multiply(ZMod other) {
        if (other.modulus != this.modulus) {
            throw new IllegalArgumentException("The modulus value of the two operands must be equal");
        }
        int product = this.value * other.value;
        return new ZMod(product % this.modulus, this.modulus);
    }

    /** Returns the additive identity in the same ring. */
    @Override
    public ZMod zero() {
        return new ZMod(0, this.modulus);
    }

    /** Returns the multiplicative identity in the same ring. */
    @Override
    public ZMod one() {
        return new ZMod(1, this.modulus);
    }

    /**
     * Compares values within the same ring.
     *
     * @throws ClassCastException if the other object is null or is not a ZMod
     * @throws IllegalArgumentException if the other ZMod has a different modulus
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof ZMod otherElement)) {
            throw new ClassCastException("Cannot compare ZMods with different types");
        }
        if (otherElement.modulus != this.modulus) {
            throw new IllegalArgumentException("The modulus value of the two operands must be equal");
        }
        return this.value == otherElement.value;
    }


}
