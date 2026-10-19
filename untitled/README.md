# Version 4

## A bound on a generic class

`Matrix<T>` replaces the concrete `ZModMatrix`. Read its declaration
in two parts.

- `T extends RingElement<T>` requires entries to support the interface methods
- `implements RingElement<Matrix<T>>` makes matrix operations accept and return matrices

An unrestricted `T` would not provide `add` or `multiply`. The bound makes those
calls legal. Here `extends` limits a type parameter even though `RingElement`
is an interface. It does not mean that `Matrix` inherits from an entry class.

## Interfaces and generics

`RingElement<T>` is the generic interface. `Matrix<T>` is a generic
class implementing `RingElement<Matrix<T>>`. `ZModElement` is still a non-generic
class implementing `RingElement<ZModElement>`.
The generic interface itself needs no bound because it only declares signatures.

## Methods

| Method | What it returns |
| --- | --- |
| `add(other)` and `multiply(other)` | A new result of the same element type |
| `negate()` | The value that adds to the original to give zero |
| `zero()` and `one()` | The additive and multiplicative identities |
| `getValue()` and `getModulus()` | The scalar's stored integers |
| `equals(other)` | Whether scalar values with matching moduli are equal |

Modulo 5, `negate()` turns 2 into 3. Arithmetic leaves operands unchanged.
These operations come from rings, which have a set and two operations, addition
and multiplication, obeying arithmetic laws. They give our Java classes a useful
common API.

## Using the classes

Create a scalar with `new ZModElement(value, modulus)`. Input values
must be less than `1_000` and may be negative. Moduli range from `1` to `999`.
Values are normalized, so negative 6 modulo 5 becomes 4.

`new Matrix<>(rows, columns, defaultValues)` takes a square `List<List<T>>`.
It copies the lists and fixes the dimensions. `getRows()` and `getColumns()` return
dimensions. `getValue(row, column)` and `setValue(row, column, value)` use indexes
starting at zero. Matrices also provide the five arithmetic operations.

Scalar arithmetic and equality reject different moduli with
`IllegalArgumentException`. Equality rejects null or another Java type with
`ClassCastException`.

## Type Caveat

The compiler checks that entries implement `RingElement<T>` and that
matrix operations use the expected entry type. It cannot distinguish 3 modulo 7
from 3 modulo 5 when both have type `ZModElement`. Mixed moduli and matrix sizes
still need runtime checks. Construction and assignment do not reject mixed moduli.

## In class

Temporarily remove the bound on `T` and inspect the compiler errors. Then find the test using matrices as entries.

From this directory, use JDK 24 and Maven to run the tests and demonstration.

```sh
mvn clean test
java -cp target/classes io.github.nathanjrussell.Main
```
