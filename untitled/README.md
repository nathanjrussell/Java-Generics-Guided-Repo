# Version 3

## A generic interface

`RingElement<T>` captures the shared method signatures. Substituting
`ZModElement` for `T` makes `T add(T other)` accept and return `ZModElement`.
Substituting `ZModMatrix` gives the corresponding matrix signature.
The interface shares a contract, while each class supplies its own implementation.

## Interfaces and generics

`RingElement<T>` is a generic interface. `T` is a type parameter.
`ZModElement` and `ZModMatrix` are non-generic classes that implement it with
different type arguments.

| Class | Implemented generic interface |
| --- | --- |
| `ZModElement` | `RingElement<ZModElement>` |
| `ZModMatrix` | `RingElement<ZModMatrix>` |

`Main` calls `negate()` through a `RingElement<ZModElement>` reference.

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

`new ZModMatrix(rows, columns, defaultValues)` takes a square `List<List<ZModElement>>`.
It copies the lists and fixes the dimensions. `getRows()` and `getColumns()` return
dimensions. `getValue(row, column)` and `setValue(row, column, value)` use indexes
starting at zero. Matrices also provide the five arithmetic operations.

Scalar arithmetic and equality reject different moduli with
`IllegalArgumentException`. Equality rejects null or another Java type with
`ClassCastException`.

## Type Caveat

The compiler rejects adding a scalar to a matrix because their
operand types differ. It still accepts elements with different moduli because
both have type `ZModElement`. The value 3 modulo 7 differs from 3 modulo 5, so
their comparison fails at runtime. Matrices can still contain mixed moduli.

## In class

Trace how `T` changes each method signature. Try passing a scalar to the matrix `add` method.

From this directory, use JDK 24 and Maven to run the tests and demonstration.

```sh
mvn clean test
java -cp target/classes io.github.nathanjrussell.Main
```
