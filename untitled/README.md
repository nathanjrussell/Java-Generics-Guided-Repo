# Version 4

## A generic matrix

`Matrix<T>` replaces `ZModMatrix`. Its declaration is
`Matrix<T extends RingElement<T>> implements RingElement<Matrix<T>>`.

The bound says that entries provide ring operations. The `implements` clause
says the matrix is also a ring element, with matrices as its operands and results.
An unrestricted `T` would not provide `add` or `multiply`.
`Main` uses `Matrix<ZModElement>`. The tests also use matrices as entries.

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

## Matrices

`new Matrix<>(rows, columns, defaultValues)` accepts a nonempty square
`List<List<T>>` with exactly those dimensions. The lists are copied, entry references
are shared, and dimensions stay fixed.

`getRows()` and `getColumns()` return dimensions. `getValue(row, column)` and
`setValue(row, column, value)` use indexes starting at zero and reject invalid
indexes or null entries. Arithmetic requires matching sizes. Matrices provide
all five ring operations but do not override `equals`.

## Rings

A ring is a set with addition and multiplication satisfying associativity and
distributivity. Addition commutes and has an identity and inverses. We also require
a multiplicative identity.

Integers modulo one positive modulus form a commutative ring. Adding zero or
multiplying by one leaves an element unchanged. Modulo 1, zero and one coincide.

Square matrices of one size over a ring also form a ring. Zero is the zero matrix
and one has ones on the diagonal. Multiplication need not commute, as `Main` shows.

## Type Caveat

The value 3 modulo 7 differs from 3 modulo 5. Both have Java type `ZModElement`,
so their comparison is rejected at runtime. Runtime ring checking could restrict
flexibility as features are added.

Matrices can still accept mixed moduli through construction or assignment.
Those entries do not belong to one ring, and arithmetic may throw later.

## Run

From this directory with JDK 24 and Maven

```sh
mvn clean test
java -cp target/classes io.github.nathanjrussell.Main
```
