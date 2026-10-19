# Version 5

## An interface for modulus objects

The constructor now accepts a `Modulus` object instead of an integer.
`Mod3` and `Mod5` implement that interface and return fixed values of 3 and 5.

An interface lets the constructor accept either implementation. However, storing
different object types does not automatically give the enclosing `ZModElement`
objects different Java types.

## Interfaces and generics

`Modulus` is a non-generic interface declaring `value()`.
`RingElement<T>` is a generic interface using `T` for operands and results.
`Mod3` and `Mod5` are concrete classes implementing `Modulus`.
`Matrix<T>` remains a generic class implementing `RingElement<Matrix<T>>`.

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

Create a scalar with `new ZModElement(2, new Mod3())`. Input values
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

An interface promises methods, not numeric ranges. The constructor
still checks the modulus. Implementations must be immutable with one fixed value
per class. Elements with different moduli still share type `ZModElement`, and
matrices can still accept mixed moduli. These mistakes surface at runtime.

## In class

Create elements using `Mod3` and `Mod5`. Why does their `add` call still compile?

From this directory, use JDK 24 and Maven to run the tests and demonstration.

```sh
mvn clean test
java -cp target/classes io.github.nathanjrussell.Main
```
