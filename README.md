# Java Generics Guided Repo

A Java generics lecture in six runnable versions. Start with concrete classes,
introduce a generic interface, and then use bounds and nested type arguments.
Modular numbers and matrices give us a consistent example throughout.

## How the READMEs work

This root README explains the whole sequence and is the same in every teaching
version. The README in `untitled` explains the Java concepts and code for the
version you have checked out. Read it alongside `Main` and the tests.

Each version is a Git tag. Use the tag selector on GitHub or run
`git switch --detach version-1` locally. Change the number to move to another
lesson. `git switch main` returns to the final version.

## Lecture sequence

| Tag | Java concept | What to notice |
| --- | --- | --- |
| `version-1` | Concrete classes and method signatures | `ZModElement` accepts its own type, but the modulus is just an integer field |
| `version-2` | Repeated APIs and collection types | `ZModMatrix` has similar methods and stores `List<List<ZModElement>>` |
| `version-3` | A generic interface | `RingElement<T>` lets each implementing class choose its operand and result type |
| `version-4` | A bounded generic class | `Matrix<T extends RingElement<T>>` can call methods promised by the bound |
| `version-5` | A non-generic interface | `Modulus` is implemented by `Mod3` and `Mod5`, but the enclosing elements still share one type |
| `version-6` | Type information carried through a class | `ZModElement<M>` preserves the modulus type, including inside `Matrix<ZModElement<Mod3>>` |

## Interfaces and generic interfaces

An interface describes methods a class must provide. A generic interface also
has type parameters, allowing those method signatures to use a chosen type.

| Declaration | Kind | Purpose |
| --- | --- | --- |
| `Modulus` | Non-generic interface | Declares `value()` |
| `RingElement<T>` | Generic interface | Uses `T` for operands and results |
| `List<E>` | Generic interface from Java | Describes a collection with entry type `E` |
| `Matrix<T>` | Generic class | Implements `RingElement<Matrix<T>>` |

`implements` connects a class to an interface. `extends` inside a type parameter
declaration restricts the types allowed for that parameter, including interfaces.
In each lesson, distinguish what the compiler rejects from what needs a runtime
check. Type bounds cannot express a positive integer or a matrix dimension.

## Run a version

Use JDK 24 and Maven. Run these commands from the repository root.

```sh
mvn -f untitled/pom.xml clean test
java -cp untitled/target/classes io.github.nathanjrussell.Main
```

Use `clean` after switching versions to remove compiled classes from the previous
lesson. Compare adjacent tags to see how the declarations and method calls change.
