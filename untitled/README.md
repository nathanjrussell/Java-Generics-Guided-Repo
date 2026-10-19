# Generic Ring Elements

Version `v0.2.0` introduces `RingElement<T>` and `Matrix<T>`.

## Rings

Rings can be formed from many structures, including integers, modular integers,
polynomials, and square matrices. A ring has a set with addition and multiplication.
It has an additive identity, a multiplicative identity, and an additive inverse
for every element.

Addition is associative and commutative. Multiplication is associative and
distributes over addition. Multiplication does not have to be commutative.

Square matrices of a fixed size with entries from the same ring also form a ring.
Addition works entry by entry. Multiplication uses rows and columns.
The zero matrix is the additive identity. The matrix with ones on its diagonal
and zeros elsewhere is the multiplicative identity.

## Generic interface

A structure representing a ring element should implement a common interface
that requires its ring operations. This project uses
`RingElement<T extends RingElement<T>>`.

`T` identifies the type of element used by the operations. `ZMod` implements
`RingElement<ZMod>`. `Matrix<T>` implements `RingElement<Matrix<T>>`, and its
entries must also implement the ring interface.

This allows different structures to share the same requirements while keeping
their operands separate. You cannot directly add a matrix to a simple constant.
For example, passing a `ZMod` to `Matrix<ZMod>.add` fails at compile time.
The interface requires the operations but cannot prove that they obey the ring laws.

## ZMod

`ZMod` represents an integer modulo a positive integer. Each object stores a
`value` and a `modulus`.

Create an element with `new ZMod(value, modulus)`. The input value must be less
than `1_000` and may be negative. The modulus must be between `1` and `999`.
The stored value is normalized to the range from zero through `modulus - 1`.
For example, `new ZMod(-6, 5)` stores the value `4`.

## Methods

| Method | Purpose |
| --- | --- |
| `getValue()` | Returns the normalized value |
| `getModulus()` | Returns the modulus |
| `add(other)` | Adds two elements in the same ring |
| `multiply(other)` | Multiplies two elements in the same ring |
| `negate()` | Returns the additive inverse |
| `negate(other)` | Returns the additive inverse of the supplied element |
| `zero()` | Returns the additive identity in the same ring |
| `one()` | Returns the multiplicative identity in the same ring |
| `equals(other)` | Returns true or false by comparing values in the same ring |

Addition, multiplication, and equality throw `IllegalArgumentException` when
the moduli differ. Equality throws `ClassCastException` for null or another Java type.
Arithmetic returns new objects and leaves the operands unchanged.

`Matrix<T>` provides the same ring operations. Its `getSize()` returns the number
of rows and columns, and `get(row, column)` retrieves an entry using zero based indexes.
The constructor requires a nonempty square matrix. Addition and multiplication
require matching matrix sizes.

### Negate

`negate()` returns the element that adds to the original element to give zero.
For modulus `5`, negating `2` gives `3` because `2 + 3` reduces to `0`.
For a `ZMod` element `a`, the expression `a.add(a.negate()).equals(a.zero())` is true.

The interface also provides `negate(other)`, which calls `other.negate()`.
Matrix negation negates every entry.

## ZMod as a ring

For a fixed modulus `n`, the elements represent the integers modulo `n`.
Addition and multiplication wrap around using that modulus.

Both operations are associative and commutative. Multiplication distributes
over addition. Every element has an additive inverse given by `negate()`.
The methods `zero()` and `one()` provide the additive and multiplicative identities.
Adding zero or multiplying by one leaves an element unchanged.
These properties make this a commutative ring.
When the modulus is `1`, both identities have value `0`.

## Matrix demonstration

`Main` creates two `Matrix<ZMod>` objects with every entry using modulus `3`.
It displays their sum, products in both orders, negations, zero matrix,
and identity matrix. The two multiplication orders give different results.
This shows that a matrix ring can have noncommutative multiplication even when
its entry ring is commutative.

The matrix class is generic and is not tied to modulus `3` or to `ZMod`.
Its entries can be another structure implementing the interface, including matrices.

## Type Caveat

The value `3` in `ZMod` with modulus `7` is not the same element as `3` in `ZMod`
with modulus `5`. They cannot be compared by this class because they come from
different rings. Calling `equals()` between them throws an exception.

Every ring uses the same Java type `ZMod`, so checking that elements belong to
the same ring happens at runtime instead of compile time. This construction
makes ring type checking a runtime operation and could restrict flexibility
as more features are added.

This construction still needs work. `Matrix<ZMod>` can accept entries with mixed
modulus values because all those entries have the Java type `ZMod`.
The constructor does not check their moduli. Such a matrix does not describe
elements from one common ring, and arithmetic may later throw an exception.
Generics prevent adding a scalar to a matrix, but they do not encode the modulus
or matrix size in the Java type. Matrix sizes are checked at runtime.

## Tests

Run the JUnit tests from this directory with JDK 24 and Maven.

```sh
mvn test
```

After compiling, run the demonstration from this directory.

```sh
java -cp target/classes io.github.nathanjrussell.Main
```
