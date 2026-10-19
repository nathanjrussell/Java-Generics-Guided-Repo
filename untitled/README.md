# Version 6

## Modulus types on elements

`ZModElement<M extends Modulus>` implements
`RingElement<ZModElement<M>>`. The bound gives access to the modulus value.
Using the same `M` in operands and results preserves the modulus type.

For example, `ZModElement<Mod3> a = new ZModElement<>(2, new Mod3())`
declares an element of the ring modulo 3. Its arithmetic returns that same type.
Passing a `ZModElement<Mod5>` to `a.add` or `a.multiply` does not compile.

`Matrix<ZModElement<Mod3>>` carries the distinction into matrices without changing
the matrix implementation. Its constructor and setter reject entries typed with
`Mod5` at compilation. `Main` shows valid operations and comments out the invalid
ones. Compiler tests check those failures.

## Values

Input values must be less than `1_000` and may be negative. Moduli must be between
`1` and `999`. Stored values range from zero through `modulus - 1`.
For example, negative 6 modulo 5 becomes 4.

The constructor validates the modulus. A generic bound cannot prove positivity.
Modulus implementations must be immutable with one fixed value per class.

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

Keep the concrete modulus type in declarations. `ZModElement<Mod3>` and
`ZModElement<Mod5>` have different arithmetic operand types. Constructing elements
as `ZModElement<Modulus>` loses that distinction, so runtime checks remain.
Boundary tests deliberately use this broader type.

The value 3 modulo 7 and the value 3 modulo 5 still belong to different rings.
`equals(Object)` accepts any Java object, so its check remains at runtime.
Matrix dimensions are also checked at runtime. Interfaces cannot prove ring laws
or that a custom modulus class always returns its promised value.

## Run

From this directory with JDK 24 and Maven

```sh
mvn clean test
java -cp target/classes io.github.nathanjrussell.Main
```
