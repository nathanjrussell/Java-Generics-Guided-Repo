# ZMod

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
| `zero()` | Returns the additive identity in the same ring |
| `one()` | Returns the multiplicative identity in the same ring |
| `equals(other)` | Returns true or false by comparing values in the same ring |

Addition, multiplication, and equality throw `IllegalArgumentException` when
the moduli differ. Equality throws `ClassCastException` for null or another Java type.
Arithmetic returns new objects and leaves the operands unchanged.

### Negate

`negate()` returns the element that adds to the original element to give zero.
For modulus `5`, negating `2` gives `3` because `2 + 3` reduces to `0`.
The expression `a.add(a.negate()).equals(a.zero())` is true.

## ZMod as a ring

For a fixed modulus `n`, the elements represent the integers modulo `n`.
Addition and multiplication wrap around using that modulus.

Both operations are associative and commutative. Multiplication distributes
over addition. Every element has an additive inverse given by `negate()`.
The methods `zero()` and `one()` provide the additive and multiplicative identities.
Adding zero or multiplying by one leaves an element unchanged.
These properties make this a commutative ring.
When the modulus is `1`, both identities have value `0`.

## Type Caveat

The value `3` in `ZMod` with modulus `7` is not the same element as `3` in `ZMod`
with modulus `5`. They cannot be compared by this class because they come from
different rings. Calling `equals()` between them throws an exception.

Every ring uses the same Java type `ZMod`, so checking that elements belong to
the same ring happens at runtime instead of compile time. This construction
makes ring type checking a runtime operation and could restrict flexibility
as more features are added.

## Tests

Run the JUnit tests from this directory with JDK 24 and Maven.

```sh
mvn test
```
