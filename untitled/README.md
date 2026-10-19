# Version 2

## Similar APIs in different classes

`ZModMatrix` stores `ZModElement` entries. Compare the two classes.
Both have `add`, `multiply`, `negate`, `zero`, and `one`, but their parameter and
return types differ. Similar method names alone do not create a shared Java type.

## Interfaces and generics

Neither example class implements a shared interface yet, and neither
is generic. `List<E>` is a generic interface from Java.
`List<List<ZModElement>>` uses it twice to represent a list of rows.

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

The value 3 modulo 7 differs from 3 modulo 5, but both have type
`ZModElement`. Comparisons and arithmetic check moduli at runtime. Matrix entries
can have mixed moduli, so later arithmetic may fail. Java types do not yet express
this restriction.

## In class

Compare the two `add` signatures. Which type would a shared interface need to vary?

From this directory, use JDK 24 and Maven to run the tests and demonstration.

```sh
mvn clean test
java -cp target/classes io.github.nathanjrussell.Main
```
