package io.github.nathanjrussell;

import java.net.URI;
import java.nio.file.Path;
import java.util.List;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CompileTimeSafetyTest {
    @TempDir
    Path output;

    @Test
    void matchingModulusTypesCompileAndPreserveResultTypes() throws Exception {
        assertCompiles(true, """
                ZModElement<Mod3> sum = a.add(a);
                ZModElement<Mod3> product = a.multiply(a);
                ZModElement<Mod3> negative = a.negate();
                ZModElement<Mod3> zero = a.zero();
                ZModElement<Mod3> one = a.one();
                Matrix<ZModElement<Mod3>> matrixSum = threes.add(threes);
                Matrix<ZModElement<Mod3>> matrixProduct = threes.multiply(threes);
                threes.setValue(0, 0, a);
                """);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "a.add(b);",
            "a.multiply(b);",
            "threes.add(fives);",
            "threes.multiply(fives);",
            "threes.setValue(0, 0, b);",
            "Matrix<ZModElement<Mod3>> mixed = new Matrix<>(2, 2, List.of(List.of(a, a), List.of(a, b)));",
            "ZModElement<Mod3> wrong = new ZModElement<>(1, new Mod5());",
            "threes.add(a);"
    })
    void incompatibleTypesDoNotCompile(String statement) throws Exception {
        assertCompiles(false, statement);
    }

    private void assertCompiles(boolean expected, String statements) throws Exception {
        String source = """
                import io.github.nathanjrussell.*;
                import java.util.List;

                class Example {
                    void use() {
                        ZModElement<Mod3> a = new ZModElement<>(2, new Mod3());
                        ZModElement<Mod5> b = new ZModElement<>(2, new Mod5());
                        Matrix<ZModElement<Mod3>> threes = new Matrix<>(1, 1, List.of(List.of(a)));
                        Matrix<ZModElement<Mod5>> fives = new Matrix<>(1, 1, List.of(List.of(b)));
                """ + statements + "\n    }\n}\n";

        var compiler = ToolProvider.getSystemJavaCompiler();
        assertNotNull(compiler, "Run these tests with a JDK");
        var diagnostics = new DiagnosticCollector<JavaFileObject>();
        var file = new SimpleJavaFileObject(URI.create("string:///Example.java"), JavaFileObject.Kind.SOURCE) {
            @Override
            public CharSequence getCharContent(boolean ignoreEncodingErrors) {
                return source;
            }
        };
        String classes = Path.of(ZModElement.class.getProtectionDomain()
                .getCodeSource().getLocation().toURI()).toString();

        try (var manager = compiler.getStandardFileManager(diagnostics, null, null)) {
            boolean compiled = compiler.getTask(null, manager, diagnostics,
                    List.of("-classpath", classes, "-d", output.toString(), "-proc:none"),
                    null, List.of(file)).call();
            if (expected) {
                assertTrue(compiled, () -> diagnostics.getDiagnostics().toString());
            } else {
                assertFalse(compiled, "Java unexpectedly accepted " + statements);
                assertTrue(diagnostics.getDiagnostics().stream()
                        .anyMatch(diagnostic -> diagnostic.getKind() == Diagnostic.Kind.ERROR),
                        () -> "Expected a compiler error but got " + diagnostics.getDiagnostics());
            }
        }
    }
}
