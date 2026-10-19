# Java Generics Guided Repo

Six versions of the same Java example, starting with modular arithmetic and
ending with matrices whose entry types identify their ring.

## How the READMEs work

This README is the guide to the whole sequence and is the same in every teaching
version. The README in `untitled` explains only the code in the version you have
checked out. Read that file, then run `Main` and the tests.

Each version is a Git tag. On GitHub, use the tag selector to choose one.
Locally, start with `git switch --detach version-1` and change the number as you
work through the lessons. `git switch main` returns to the final version.

## Versions

| Tag | What changes |
| --- | --- |
| `version-1` | `ZModElement` stores an integer value and modulus. Arithmetic and ring identities are ordinary methods. Different moduli are rejected at runtime |
| `version-2` | `ZModMatrix` stores square matrices of those elements. It provides the same operations, but there is no shared interface yet |
| `version-3` | `RingElement<T>` names the shared operations. Each class supplies its own type for `T`, keeping scalar and matrix operands separate |
| `version-4` | `Matrix<T extends RingElement<T>>` replaces the concrete matrix. Its bound lets the implementation call ring operations on any suitable entry type |
| `version-5` | `Mod3` and `Mod5` represent fixed moduli through a `Modulus` interface. Elements accept modulus objects but still share one Java type |
| `version-6` | `ZModElement<M extends Modulus>` carries the modulus type. Elements and matrices using `Mod3` cannot be added to ones using `Mod5` when those concrete types are retained |

Follow the versions in order. Compare adjacent tags to see which problem prompted
the next change. The last version still checks matrix dimensions at runtime.
Generic interfaces describe available operations but cannot prove the ring laws.

## Run a version

Use JDK 24 and Maven. These commands run from the repository root.

```sh
mvn -f untitled/pom.xml clean test
java -cp untitled/target/classes io.github.nathanjrussell.Main
```

Use `clean` when switching versions so that compiled classes from an earlier
lesson do not remain in the build.
