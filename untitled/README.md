# Version 6

## Preserving a type argument

Read the declaration as
`ZModElement<M extends Modulus> implements RingElement<ZModElement<M>>`.

`M extends Modulus` makes `value()` available on the modulus object. Reusing `M`
in `ZModElement<M>` ties arithmetic operands and results to that type argument.
`ZModElement<Mod3>` therefore cannot add a `ZModElement<Mod5>`.

`Matrix<ZModElement<Mod3>>` carries the same information into its entries.
The matrix implementation needs no changes. This is composition of generic types.

## Interfaces and generics

`Modulus` is a non-generic interface declaring `value()`. `RingElement<T>` is a generic
interface. `ZModElement<M>` and `Matrix<T>` are generic classes implementing that
generic interface with their own parameterized types.
`Mod3` and `Mod5` are concrete classes implementing `Modulus` with fixed values 3 and 5.

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

Use `new ZModElement<>(2, new Mod3())` to create a `ZModElement<Mod3>`. Input values
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

Keep the specific modulus type. Constructing elements as
`ZModElement<Modulus>` loses the distinction between `Mod3` and `Mod5`, so runtime
checks remain. `ZModElement<Mod3>` is not assignable to `ZModElement<Modulus>`
because Java generic types are invariant.

`equals(Object)` still accepts any object and checks moduli at runtime. Matrix
sizes and numeric limits also remain runtime checks. Modulus implementations must
be immutable with one fixed value per class. A bound cannot enforce that promise.

## In class

Uncomment an incompatible call in `Main` and inspect the compiler error. Restore the comment before running tests.

From this directory, use JDK 24 and Maven to run the tests and demonstration.

```sh
mvn clean test
java -cp target/classes io.github.nathanjrussell.Main
```
