# Version 1

## Modular arithmetic

`ZModElement` stores a value and an integer modulus. There are no generic
classes or shared interfaces yet. `Main` demonstrates arithmetic, identities,
and one comparison between different rings.

## Values

Input values must be less than `1_000` and may be negative. Moduli must be between
`1` and `999`. Stored values range from zero through `modulus - 1`.
For example, negative 6 modulo 5 becomes 4.

Create an element with `new ZModElement(value, modulus)`.

## Methods

| Method | Result |
| --- | --- |
| `getValue()` and `getModulus()` | The normalized integer value and integer modulus |
| `add(other)` | The sum in the same ring |
| `multiply(other)` | The product in the same ring |
| `negate()` | The additive inverse |
| `zero()` and `one()` | The additive and multiplicative identities |
| `equals(other)` | Whether two scalar values in the same ring are equal |

Arithmetic returns new objects. `negate()` returns the element that adds to the
original to give zero. Modulo 5, negating 2 gives 3.

Scalar arithmetic and equality reject different moduli with
`IllegalArgumentException`. Equality throws `ClassCastException` for null or
another Java type.

## Rings

A ring is a set with addition and multiplication satisfying associativity and
distributivity. Addition commutes and has an identity and inverses. We also require
a multiplicative identity.

Integers modulo one positive modulus form a commutative ring. Adding zero or
multiplying by one leaves an element unchanged. Modulo 1, zero and one coincide.

## Type Caveat

The value 3 modulo 7 differs from 3 modulo 5. Both have Java type `ZModElement`,
so their comparison is rejected at runtime. Runtime ring checking could restrict
flexibility as features are added.

## Run

From this directory with JDK 24 and Maven

```sh
mvn clean test
java -cp target/classes io.github.nathanjrussell.Main
```
