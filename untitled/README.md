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
`RingElement<T>`.

`T` identifies the type of element used by the operations. `ZModElement` implements
`RingElement<ZModElement>`. `Matrix<T>` implements `RingElement<Matrix<T>>`, and its
entries must also implement the ring interface.

The matrix declaration uses `T extends RingElement<T>` so that it can call ring
operations on its entries. The interface itself does not need that bound because
it only declares methods using `T` as a parameter or return type.

This allows different structures to share the same requirements while keeping
their operands separate. You cannot directly add a matrix to a simple constant.
For example, passing a `ZModElement` to `Matrix<ZModElement>.add` fails at compile time.
The interface requires the operations but cannot prove that they obey the ring laws.

## ZModElement

`ZModElement` represents an integer modulo a positive integer. Each object stores a
`value` and a `modulus`.

Create an element with `new ZModElement(value, modulus)`. The input value must be less
than `1_000` and may be negative. The modulus must be between `1` and `999`.
The stored value is normalized to the range from zero through `modulus - 1`.
For example, `new ZModElement(-6, 5)` stores the value `4`.

## Methods

| Method | Purpose |
| --- | --- |
| `getValue()` | Returns the normalized value |
| `getModulus()` | Returns the modulus |
| `add(other)` | Adds two elements in the same ring |
| `multiply(other)` | Multiplies two elements in the same ring |
| `negate()` | Returns the additive inverse |
| `zero()` | Returns the additive identity in the same ring |
| `one()` | Returns the multiplicative identity in the same ring |
| `equals(other)` | Returns true or false by comparing values in the same ring |

Addition, multiplication, and equality throw `IllegalArgumentException` when
the moduli differ. Equality throws `ClassCastException` for null or another Java type.
Arithmetic returns new objects and leaves the operands unchanged.

`Matrix<T>` provides the same ring operations. Create one with
`new Matrix<>(rows, columns, defaultValues)`, where `defaultValues` is a
`List<List<T>>` matching those dimensions exactly. The matrix must be nonempty
and square so that its operations form a ring.

The dimensions stay fixed after construction. Entries are stored in a private
`List<List<T>>`. The constructor copies the outer list and each row while keeping
the entry references. Changing the original lists later does not change the matrix.

| Matrix method | Purpose |
| --- | --- |
| `getRows()` | Returns the fixed row count |
| `getColumns()` | Returns the fixed column count |
| `getValue(row, column)` | Retrieves an entry using zero based indexes |
| `setValue(row, column, value)` | Replaces an entry without changing the dimensions |

Invalid coordinates and null entries are rejected. Arithmetic returns new
matrices with separate storage. Addition and multiplication require matching sizes.

### Negate

`negate()` returns the element that adds to the original element to give zero.
For modulus `5`, negating `2` gives `3` because `2 + 3` reduces to `0`.
For a `ZModElement` element `a`, the expression `a.add(a.negate()).equals(a.zero())` is true.

Matrix negation negates every entry.

## ZModElement as a ring

For a fixed modulus `n`, the elements represent the integers modulo `n`.
Addition and multiplication wrap around using that modulus.

Both operations are associative and commutative. Multiplication distributes
over addition. Every element has an additive inverse given by `negate()`.
The methods `zero()` and `one()` provide the additive and multiplicative identities.
Adding zero or multiplying by one leaves an element unchanged.
These properties make this a commutative ring.
When the modulus is `1`, both identities have value `0`.

## Matrix demonstration

`Main` creates two `Matrix<ZModElement>` objects with every entry using modulus `3`.
It displays their sum, products in both orders, negations, zero matrix,
and identity matrix. It also changes an entry with `setValue`.
The two multiplication orders give different results.
This shows that a matrix ring can have noncommutative multiplication even when
its entry ring is commutative.

The matrix class is generic and is not tied to modulus `3` or to `ZModElement`.
Its entries can be another structure implementing the interface, including matrices.

## Type Caveat

The value `3` in `ZModElement` with modulus `7` is not the same element as `3` in `ZModElement`
with modulus `5`. They cannot be compared by this class because they come from
different rings. Calling `equals()` between them throws an exception.

Every ring uses the same Java type `ZModElement`, so checking that elements belong to
the same ring happens at runtime instead of compile time. This construction
makes ring type checking a runtime operation and could restrict flexibility
as more features are added.

This construction still needs work. `Matrix<ZModElement>` can accept entries with mixed
modulus values because all those entries have the Java type `ZModElement`.
The constructor and `setValue` do not check their moduli. Such a matrix does not describe
elements from one common ring, and arithmetic may later throw an exception.
Generics prevent adding a scalar to a matrix, but they do not encode the modulus
or matrix size in the Java type. Matrix sizes are checked at runtime.

## Type bounds and positive values

Java generic bounds restrict types. They cannot express an integer greater than
zero. `T extends Integer` would still allow zero and negative values, and `Integer`
is final so it cannot have a positive integer subclass. A type argument must be a
reference type, so neither `int` nor a literal such as `3` can be used as `T`.

This project checks the modulus in the constructor and requires it to be between
`1` and `999`. Zero remains a valid element value because a ring needs its additive
identity.

A validated wrapper such as `PositiveModulus` could check that range when created.
Methods accepting that wrapper would then require an already validated modulus.
The check still happens at runtime.

A further design could use distinct types such as `Mod3` and `Mod5`, each with a
fixed positive modulus. A generic element class could then distinguish
`ZModElement<Mod3>` from `ZModElement<Mod5>` at compile time. This would require a
different class design. The generic bound alone would still not prove that an
implementation supplies a positive modulus.

## Tests

Run the JUnit tests from this directory with JDK 24 and Maven.

```sh
mvn test
```

After compiling, run the demonstration from this directory.

```sh
java -cp target/classes io.github.nathanjrussell.Main
```
