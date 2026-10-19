# Version 1

## Concrete classes and method types

Start with `ZModElement`. Its `add` method accepts a `ZModElement` and
returns a `ZModElement`. These types are fixed in the method declaration.
The compiler rejects a string argument, but it cannot tell whether two elements
have different modulus values.

## Interfaces and generics

This version declares no interfaces or generic classes. `ZModElement`
is a concrete class. Its `int modulus` field is a value, not a type argument.

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

Scalar arithmetic and equality reject different moduli with
`IllegalArgumentException`. Equality rejects null or another Java type with
`ClassCastException`.

## Type Caveat

The value 3 modulo 7 differs from 3 modulo 5, but both objects have
type `ZModElement`. Their comparison must be rejected at runtime. Keeping this
distinction only in fields could limit flexibility as the program grows.

## In class

Compare a wrong argument type with a different modulus. Which error appears before the program runs?

From this directory, use JDK 24 and Maven to run the tests and demonstration.

```sh
mvn clean test
java -cp target/classes io.github.nathanjrussell.Main
```
