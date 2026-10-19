package io.github.nathanjrussell;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ModulusTest {
    @Test
    void providedTypesHaveFixedModuli() {
        assertEquals(3, new Mod3().value());
        assertEquals(5, new Mod5().value());
    }

    @Test
    void elementsRejectMissingModuli() {
        assertThrows(NullPointerException.class, () -> new ZModElement(1, null));
    }

    @Test
    void separateModulusInstancesRepresentTheSameRing() {
        ZModElement first = new ZModElement(2, new Mod3());
        ZModElement second = new ZModElement(2, new Mod3());
        assertEquals(1, first.add(second).getValue());
        assertEquals(1, first.multiply(second).getValue());
    }
}
